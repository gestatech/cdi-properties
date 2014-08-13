package com.byteslounge.cdi.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Represents a property resolver method parameter to be injected by the CDI
 * container.
 * 
 * @author Gonçalo Marques
 * @since 1.0.0
 */
public class PropertyResolverParameter implements InjectionPoint {

    private final AnnotatedParameter<?> parameter;
    private final BeanManager beanManager;
    private final Bean<?> propertyResolverBean;

    public PropertyResolverParameter(AnnotatedParameter<?> parameter, BeanManager beanManager, Bean<?> propertyResolverBean) {
        this.parameter = parameter;
        this.beanManager = beanManager;
        this.propertyResolverBean = propertyResolverBean;
    }

    /**
     * See {@link InjectionPoint#getAnnotated()}
     */
    @Override
    public Annotated getAnnotated() {
        return parameter;
    }

    /**
     * See {@link InjectionPoint#getBean()}
     */
    @Override
    public Bean<?> getBean() {
        return propertyResolverBean;
    }

    /**
     * See {@link InjectionPoint#getMember()}
     */
    @Override
    public Member getMember() {
        return parameter.getDeclaringCallable().getJavaMember();
    }

    /**
     * See {@link InjectionPoint#getQualifiers()}
     */
    @Override
    public Set<Annotation> getQualifiers() {
        Set<Annotation> qualifiers = new HashSet<Annotation>();
        for (Annotation annotation : parameter.getAnnotations()) {
            if (beanManager.isQualifier(annotation.annotationType())) {
                qualifiers.add(annotation);
            }
        }
        return qualifiers;
    }

    /**
     * See {@link InjectionPoint#getType()}
     */
    @Override
    public Type getType() {
        return parameter.getBaseType();
    }

    /**
     * See {@link InjectionPoint#isDelegate()}
     */
    @Override
    public boolean isDelegate() {
        return false;
    }

    /**
     * See {@link InjectionPoint#isTransient()}
     */
    @Override
    public boolean isTransient() {
        return false;
    }

}
