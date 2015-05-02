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
package com.byteslounge.cdi.test.epm;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Assert;

import com.byteslounge.cdi.annotation.PropertyBundle;
import com.byteslounge.cdi.annotation.PropertyKey;
import com.byteslounge.cdi.annotation.PropertyLocale;
import com.byteslounge.cdi.annotation.PropertyResolver;
import com.byteslounge.cdi.test.common.ApplicationScopedBean;
import com.byteslounge.cdi.test.common.DependentScopedBean;
import com.byteslounge.cdi.test.configuration.TestConstants;
import com.byteslounge.cdi.test.model.TestEntity;

/**
 * Used in CDI Properties integration tests. See WarDefaultMethodIT.java,
 * WarProvidedMethodIT.java, EjbDefaultMethodIT.java and
 * EjbProvidedMethodIT.java.
 * 
 * @author Gonçalo Marques
 * @since 1.0.0
 */
@ApplicationScoped
public class EjbProvidedMethodResolver implements Serializable {

    private static final long serialVersionUID = 1L;

    // Lets provide a set of completely mixed up parameters in what matters to "logical" definition order
    @PropertyResolver
    public String resolveProperty(@PropertyBundle String bundleName, DependentScopedBean dependentScopedBean,
            @PropertyLocale Locale locale, ApplicationScopedBean applicationScopedBean, @PropertyKey String key) {

        Assert.assertEquals(dependentScopedBean.getText(), TestConstants.BEAN_TEST_RETURN_VALUE);
        Assert.assertEquals(applicationScopedBean.getText(), TestConstants.BEAN_TEST_RETURN_VALUE);

        ServiceEjbProvidedMethod serviceEjbProvidedMethod = null;
        try {
            serviceEjbProvidedMethod = (ServiceEjbProvidedMethod) new InitialContext()
                    .lookup("java:global/cdiproperties/cdipropertiesejb/ServiceEjbProvidedMethodBean");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(serviceEjbProvidedMethod.getText(), TestConstants.BEAN_TEST_RETURN_VALUE);

        serviceEjbProvidedMethod.remove(1L);
        TestEntity testEntity = new TestEntity();
        testEntity.setId(1L);
        testEntity.setDescription("Description");
        serviceEjbProvidedMethod.persist(testEntity);
        TestEntity other = serviceEjbProvidedMethod.findById(1L);
        Assert.assertEquals(new Long(1L), other.getId());

        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
        String value = bundle.getString(key);
        return value + TestConstants.PROVIDED_RESOLVER_SUFFIX;
    }

}
