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

package cd.go.authorization.google.requests;

import cd.go.authorization.google.executors.UserAuthenticationRequestExecutor;
import cd.go.authorization.google.models.AuthConfig;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserAuthenticationRequestTest {
    @Mock
    private GoPluginApiRequest apiRequest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldDeserializeGoPluginApiRequestToUserAuthenticationRequest() throws Exception {
        String responseBody = "{\n" +
                "  \"authorization_server_callback_url\": \"https://redirect.url\",\n" +
                "  \"credentials\": {\n" +
                "    \"access_token\": \"access-token\",\n" +
                "    \"token_type\": \"token\",\n" +
                "    \"expires_in\": 3600,\n" +
                "    \"scope\": \"profile\",\n" +
                "    \"id_token\": \"id-token\"\n" +
                "  },\n" +
                "  \"auth_configs\": [\n" +
                "    {\n" +
                "      \"id\": \"google-config\",\n" +
                "      \"configuration\": {\n" +
                "        \"ClientId\": \"client-id\",\n" +
                "        \"AllowedDomains\": \"example.com\",\n" +
                "        \"ClientSecret\": \"client-secret\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        when(apiRequest.requestBody()).thenReturn(responseBody);

        final UserAuthenticationRequest request = UserAuthenticationRequest.from(apiRequest);

        assertThat(request.authConfigs(), hasSize(1));
        assertThat(request.executor(), instanceOf(UserAuthenticationRequestExecutor.class));

        final AuthConfig authConfig = request.authConfigs().get(0);

        assertThat(authConfig.getId(), is("google-config"));
        assertThat(authConfig.getConfiguration().clientId(), is("client-id"));
        assertThat(authConfig.getConfiguration().clientSecret(), is("client-secret"));
        assertThat(authConfig.getConfiguration().allowedDomains(), containsInAnyOrder("example.com"));
    }
}