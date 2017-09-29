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

import cd.go.authorization.google.GoogleApiClient;
import cd.go.authorization.google.exceptions.NoAuthorizationConfigurationException;
import cd.go.authorization.google.requests.GetAuthorizationServerUrlRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Collections;

import static cd.go.authorization.google.utils.Util.GSON;

public class GetAuthorizationServerUrlRequestExecutor implements RequestExecutor {
    private final GetAuthorizationServerUrlRequest request;

    public GetAuthorizationServerUrlRequestExecutor(GetAuthorizationServerUrlRequest request) {
        this.request = request;
    }

    public GoPluginApiResponse execute() throws Exception {
        if (request.authConfigs().isEmpty()) {
            throw new NoAuthorizationConfigurationException("[Authorization Server Url] No authorization configuration found.");
        }

        final GoogleApiClient googleApiClient = request.authConfigs().get(0).getConfiguration().googleApiClient();

        return DefaultGoPluginApiResponse.success(GSON.toJson(Collections.singletonMap("authorization_server_url", googleApiClient.authorizationServerUrl(request.callbackUrl()))));
    }
}
