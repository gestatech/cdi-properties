/*
 * Copyright 2015 byteslounge.com (Gonçalo Marques).
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
package com.byteslounge.cdi.test.common.session;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;

/**
 * Session scoped bean that hold the locale for the current session (used in integration tests)
 * 
 * @author Gonçalo Marques
 * @since 1.1.0
 */
@SessionScoped
public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;
    private Locale locale;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}
