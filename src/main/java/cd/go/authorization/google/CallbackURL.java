/*
 * Copyright 2017 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cd.go.authorization.google;

import cd.go.authorization.google.utils.Util;

public class CallbackURL {
    private static final CallbackURL CALLBACK_URL = new CallbackURL();
    private String callbackURL;

    public void updateRedirectURL(String redirectURL) {
        if (Util.isBlank(redirectURL)) {
            return;
        }

        if (!redirectURL.equals(this.callbackURL)) {
            synchronized (CALLBACK_URL) {
                if (!redirectURL.equals(this.callbackURL)) {
                    this.callbackURL = redirectURL;
                }
            }
        }
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public static CallbackURL instance() {
        return CALLBACK_URL;
    }
}
