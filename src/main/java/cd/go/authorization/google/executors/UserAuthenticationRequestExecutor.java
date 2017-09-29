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
import cd.go.authorization.google.GoogleUser;
import cd.go.authorization.google.exceptions.NoAuthorizationConfigurationException;
import cd.go.authorization.google.models.GoogleConfiguration;
import cd.go.authorization.google.models.User;
import cd.go.authorization.google.requests.UserAuthenticationRequest;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static cd.go.authorization.google.GooglePlugin.LOG;
import static java.text.MessageFormat.format;

public class UserAuthenticationRequestExecutor implements RequestExecutor {
    private static final Gson GSON = new Gson();
    private final UserAuthenticationRequest request;

    public UserAuthenticationRequestExecutor(UserAuthenticationRequest request) {
        this.request = request;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        if (request.authConfigs().isEmpty()) {
            throw new NoAuthorizationConfigurationException("[Authenticate] No authorization configuration found.");
        }

        final GoogleConfiguration configuration = request.authConfigs().get(0).getConfiguration();
        final GoogleApiClient googleApiClient = configuration.googleApiClient();
        final GoogleUser googleUser = googleApiClient.userProfile(request.tokenInfo());

        if (configuration.allowedDomains().isEmpty() || configuration.allowedDomains().contains(googleUser.getHd())) {

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("user", new User(googleUser));
            userMap.put("roles", Collections.emptyList());

            return DefaultGoPluginApiResponse.success(GSON.toJson(userMap));
        }

        LOG.warn(format("[Authenticate] User `{0}` is not belongs to allowed domain list.", googleUser.getEmail()));
        return DefaultGoPluginApiResponse.error(format("[Authenticate] User `{0}` is not belongs to allowed domain list.", googleUser.getEmail()));
    }
}
