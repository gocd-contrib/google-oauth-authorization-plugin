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

package cd.go.authorization.google.executors;


import cd.go.authorization.google.annotation.MetadataValidator;
import cd.go.authorization.google.requests.AuthConfigValidateRequest;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.List;
import java.util.Map;

import static cd.go.authorization.google.GooglePlugin.LOG;


public class AuthConfigValidateRequestExecutor implements RequestExecutor {
    private static final Gson GSON = new Gson();
    private final AuthConfigValidateRequest request;

    public AuthConfigValidateRequestExecutor(AuthConfigValidateRequest request) {
        this.request = request;
    }

    public GoPluginApiResponse execute() throws Exception {
        LOG.info("[Validate AuthConfig] Validating authorization configuration.");

        final List<Map<String, String>> validationResult = new MetadataValidator().validate(request.googleConfiguration());
        return DefaultGoPluginApiResponse.success(GSON.toJson(validationResult));
    }
}
