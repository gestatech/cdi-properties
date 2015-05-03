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
package com.byteslounge.cdi.test.it;

import java.io.IOException;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import com.byteslounge.cdi.test.common.ApplicationScopedBean;
import com.byteslounge.cdi.test.common.DependentScopedBean;
import com.byteslounge.cdi.test.common.InjectedBean;
import com.byteslounge.cdi.test.edm.ServiceEjbDefaultMethod;
import com.byteslounge.cdi.test.edm.ServiceEjbDefaultMethodBean;
import com.byteslounge.cdi.test.edm.TestEjbDefaultBean;
import com.byteslounge.cdi.test.epm.EjbProvidedMethodResolver;
import com.byteslounge.cdi.test.epm.ServiceEjbProvidedMethod;
import com.byteslounge.cdi.test.epm.ServiceEjbProvidedMethodBean;
import com.byteslounge.cdi.test.it.common.IntegrationTestDeploymentUtils.DeploymentAppenderFactory;
import com.byteslounge.cdi.test.it.common.IntegrationTestDeploymentUtils.ServerType;
import com.byteslounge.cdi.test.model.IdGenerator;
import com.byteslounge.cdi.test.model.TestEntity;

/**
 * Integration Test
 * 
 * @author Gonçalo Marques
 * @since 1.0.0
 */
public class CommonEjbProvidedMethod {

    public static EnterpriseArchive createEnterpriseArchive(ServerType serverType) throws IOException {

        JavaArchive ejbModule = ShrinkWrap.create(JavaArchive.class, "cdipropertiesejb.jar").addClasses(
                ServiceEjbDefaultMethod.class, ServiceEjbDefaultMethodBean.class, IdGenerator.class,
                ServiceEjbProvidedMethodBean.class);
        DeploymentAppenderFactory.create(ejbModule).appendBeansXml().appendPersistenceXml(serverType);

        JavaArchive resourcesJar = ShrinkWrap.create(JavaArchive.class, "resources.jar");
        DeploymentAppenderFactory.create(resourcesJar).appendLogging().appendProperties().appendOtherProperties()
                .appendCDIPropertiesConfig();

        JavaArchive resolverJar = ShrinkWrap.create(JavaArchive.class, "resolver.jar").addClasses(
                EjbProvidedMethodResolver.class, ApplicationScopedBean.class, DependentScopedBean.class,
                InjectedBean.class, ServiceEjbProvidedMethod.class, TestEntity.class);
        DeploymentAppenderFactory.create(resolverJar).appendBeansXml();

        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "cdiproperties.ear").addAsModule(ejbModule)
                .addAsLibrary(resourcesJar).addAsLibrary(resolverJar);
        DeploymentAppenderFactory.create(ear).appendApplicationXml().appendCDIPropertiesLib();

        return ear;
    }

    public static Archive<?> createWebArchive() throws IOException {

        WebArchive webModule = ShrinkWrap.create(WebArchive.class, "cdipropertiestest.war").addClasses(
                ServiceEjbDefaultMethod.class, InjectedBean.class, TestEjbDefaultBean.class);
        DeploymentAppenderFactory.create(webModule)
                .appendWebXml(
                        "../cdi-properties-test-common/src/test/resources/assets/common/ejbCommon/WEB-INF/web.xml")
                .appendWebResource(
                        "../cdi-properties-test-common/src/test/resources/assets/common/ejbCommon/webapp/cditestejb.xhtml")
                .appendFacesConfig()
                .appendBeansXml();

        return webModule;
    }

}
