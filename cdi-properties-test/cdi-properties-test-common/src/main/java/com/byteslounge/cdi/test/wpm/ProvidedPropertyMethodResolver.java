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
package com.byteslounge.cdi.test.wpm;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;

import org.junit.Assert;

import com.byteslounge.cdi.annotation.PropertyBundle;
import com.byteslounge.cdi.annotation.PropertyKey;
import com.byteslounge.cdi.annotation.PropertyLocale;
import com.byteslounge.cdi.annotation.PropertyResolver;
import com.byteslounge.cdi.test.common.ApplicationScopedBean;
import com.byteslounge.cdi.test.common.DependentScopedBean;
import com.byteslounge.cdi.test.configuration.TestConstants;
import com.byteslounge.cdi.test.model.TestEntity;

@ApplicationScoped
public class ProvidedPropertyMethodResolver implements Serializable {

    private static final long serialVersionUID = 1L;

    // Lets provide a set of completely mixed up parameters in what matters to "logical" definition order
    @PropertyResolver
    public String resolveProperty(DependentScopedBean dependentScopedBean,
            RequestScopedBean requestScopedBean, SessionScopedBean sessionScopedBean,
            @PropertyBundle String bundleName, @PropertyLocale Locale locale,
            ApplicationScopedBean applicationScopedBean, @PropertyKey String key, Service service) {

        service.remove(1L);
        TestEntity testEntity = new TestEntity();
        testEntity.setId(1L);
        testEntity.setDescription("Description");
        service.persist(testEntity);
        TestEntity other = service.findById(1L);
        Assert.assertEquals(new Long(1L), other.getId());

        Assert.assertEquals(dependentScopedBean.getText(), TestConstants.BEAN_TEST_RETURN_VALUE);
        Assert.assertEquals(requestScopedBean.getText(), TestConstants.BEAN_TEST_RETURN_VALUE);
        Assert.assertEquals(sessionScopedBean.getText(), TestConstants.BEAN_TEST_RETURN_VALUE);
        Assert.assertEquals(applicationScopedBean.getText(), TestConstants.BEAN_TEST_RETURN_VALUE);
        Assert.assertEquals(service.getText(), TestConstants.BEAN_TEST_RETURN_VALUE);

        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
        String value = bundle.getString(key);
        return value + TestConstants.PROVIDED_RESOLVER_SUFFIX;
    }

}
