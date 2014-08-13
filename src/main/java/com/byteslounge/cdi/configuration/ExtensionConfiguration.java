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
package com.byteslounge.cdi.configuration;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.byteslounge.cdi.exception.ExtensionInitializationException;
import com.byteslounge.cdi.resolver.locale.LocaleResolverFactory;
import com.byteslounge.cdi.resolver.locale.StandaloneLocaleResolver;

/**
 * Defines the global application configuration.
 * 
 * @author Gonçalo Marques
 * @since 1.0.0
 */
public enum ExtensionConfiguration {

    INSTANCE;

    private static final String EXTENSION_PROPERTIES_FILE = "CDIProperties.properties";
    private static final Logger logger = LoggerFactory.getLogger(ExtensionConfiguration.class);

    private ExtensionConfiguration() {
        LocaleResolverFactory.setLocaleResolver(new StandaloneLocaleResolver());
        URL url = ExtensionConfiguration.class.getClassLoader().getResource(EXTENSION_PROPERTIES_FILE);
        if (url != null) {
            Properties properties = new Properties();
            try {
                properties.load(url.openStream());
                setResourceBundleDefaultBaseName(properties.getProperty(ExtensionInitializer.DEFAULT_RESOURCE_BUNDLE_BASE_NAME_PARAM));
            } catch (IOException e) {
                throw new ExtensionInitializationException("Error while loading CDI Properties configuration file", e);
            }
        }
    }

    private String resourceBundleDefaultBaseName;

    /**
     * Empty method to trigger the enum initialization.
     */
    public void init() {
    }

    /**
     * Get the configured default resource bundle name
     * 
     * @return The configured default resource bundle name
     */
    public String getResourceBundleDefaultBaseName() {
        return resourceBundleDefaultBaseName;
    }

    /**
     * Sets the configured default resource bundle name
     * 
     * @param resourceBundleDefaultBaseName
     *            The default resource bundle name to be configured in the
     *            application
     */
    public void setResourceBundleDefaultBaseName(String resourceBundleDefaultBaseName) {
        this.resourceBundleDefaultBaseName = resourceBundleDefaultBaseName;
        logger.info("Default resource bundle name was set as: " + this.resourceBundleDefaultBaseName);
    }

}
