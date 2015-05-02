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
package com.byteslounge.cdi.annotation;

import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to decorate a property resolver method parameter.
 * When the property resolver method is invoked, the parameter annotated with
 * this annotation will receive the invocation's contextual resource bundle key.
 * 
 * @author Gonçalo Marques
 * @since 1.0.0
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ PARAMETER })
public @interface PropertyKey {

}
