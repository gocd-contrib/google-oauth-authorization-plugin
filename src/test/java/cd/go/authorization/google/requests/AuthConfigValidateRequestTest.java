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

import cd.go.authorization.google.models.GoogleConfiguration;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuthConfigValidateRequestTest {
    @Mock
    private GoPluginApiRequest apiRequest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldDeserializeGoPluginApiRequestToAuthConfigValidateRequest() throws Exception {
        String responseBody = "{\n" +
                "  \"AllowedDomains\": \"example.com\",\n" +
                "  \"ClientId\": \"client-id\",\n" +
                "  \"ClientSecret\": \"client-secret\"\n" +
                "}";

        when(apiRequest.requestBody()).thenReturn(responseBody);

        final AuthConfigValidateRequest request = AuthConfigValidateRequest.from(apiRequest);
        final GoogleConfiguration googleConfiguration = request.googleConfiguration();

        assertThat(googleConfiguration.allowedDomains(), Matchers.contains("example.com"));
        assertThat(googleConfiguration.clientId(), is("client-id"));
        assertThat(googleConfiguration.clientSecret(), is("client-secret"));
    }
}