/*
 * Copyright 2014 byteslounge.com (Gonçalo Marques).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.byteslounge.cdi.resolver.verifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;

import com.byteslounge.cdi.annotation.Property;
import com.byteslounge.cdi.exception.ExtensionInitializationException;

/**
 * Checks if the custom injected property method resolver parameters are
 * properly configured
 * 
 * @author Gonçalo Marques
 * @since 1.0.0
 */
public class ResolverMethodParametersVerifier implements ResolverMethodVerifier {

    private final AnnotatedMethod<?> propertyResolverMethod;
    private final int localeParameterIndex;
    private final int bundleNameParameterIndex;

    public ResolverMethodParametersVerifier(AnnotatedMethod<?> propertyResolverMethod, int localeParameterIndex, int bundleNameParameterIndex) {
        this.propertyResolverMethod = propertyResolverMethod;
        this.localeParameterIndex = localeParameterIndex;
        this.bundleNameParameterIndex = bundleNameParameterIndex;
    }

    /**
     * See {@link ResolverMethodVerifier#verify()}
     */
    @Override
    public void verify() {
        int startIndex = 1;
        if (localeParameterIndex > -1) {
            startIndex++;
        }
        if (bundleNameParameterIndex > -1) {
            startIndex++;
        }
        int currentIndex = 0;
        for (final AnnotatedParameter<?> parameter : propertyResolverMethod.getParameters()) {
            if (currentIndex++ < startIndex) {
                continue;
            }
            if (checkDependentScope((Class<?>) parameter.getBaseType())) {
                checkPropertyField((Class<?>) parameter.getBaseType(), (Class<?>) parameter.getBaseType());
            }
        }

    }

    /**
     * Checks if a custom injected parameter scope is {@link Dependent}
     * 
     * @param type
     *            The parameter type being checked
     * @return Returns true if the paraeter scope is {@link Dependent}
     */
    private boolean checkDependentScope(Class<?> type) {
        for (Annotation annotation : type.getAnnotations()) {
            if (annotation.annotationType().equals(SessionScoped.class) || annotation.annotationType().equals(RequestScoped.class)
                    || annotation.annotationType().equals(ApplicationScoped.class)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a custom injected parameter has any field annotated with
     * {@link Property} (this function calls itself recursively).
     * 
     * @param originalType
     *            The type being checked
     * @param type
     *            The current type or original type super class being checked
     *            (this function calls itself recursively).
     */
    private void checkPropertyField(Class<?> originalType, Class<?> type) {
        for (Field field : type.getDeclaredFields()) {
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation.annotationType().equals(Property.class)) {
                    throw new ExtensionInitializationException(
                            "Property resolver method is injecting a Dependent scoped bean which in turn also has an injected @"
                                    + Property.class.getSimpleName()
                                    + " (this would cause a stack overflow because Dependent scoped bean instances are injected directly and not proxied). Type: "
                                    + originalType.getSimpleName() + ", field: " + field.getName());
                }
            }
        }
        if (type.getSuperclass() != null && !type.getSuperclass().equals(Object.class)) {
            checkPropertyField(originalType, type.getSuperclass());
        }
    }

}
