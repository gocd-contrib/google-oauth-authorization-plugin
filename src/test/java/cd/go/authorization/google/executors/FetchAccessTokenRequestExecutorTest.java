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

import cd.go.authorization.google.Provider;
import cd.go.authorization.google.exceptions.NoAuthorizationConfigurationException;
import cd.go.authorization.google.models.AuthConfig;
import cd.go.authorization.google.models.GoogleConfiguration;
import cd.go.authorization.google.models.TokenInfo;
import cd.go.authorization.google.requests.FetchAccessTokenRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FetchAccessTokenRequestExecutorTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private FetchAccessTokenRequest request;
    @Mock
    private AuthConfig authConfig;
    @Mock
    private GoogleConfiguration pluginConfiguration;
    @Mock
    private Provider provider;
    private FetchAccessTokenRequestExecutor executor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(authConfig.getConfiguration()).thenReturn(pluginConfiguration);
        when(pluginConfiguration.provider()).thenReturn(provider);

        executor = new FetchAccessTokenRequestExecutor(request);
    }

    @Test
    public void shouldErrorOutIfAuthConfigIsNotProvided() throws Exception {
        when(request.authConfigs()).thenReturn(Collections.emptyList());

        thrown.expect(NoAuthorizationConfigurationException.class);
        thrown.expectMessage("[Get Access Token] No authorization configuration found.");

        executor.execute();
    }

    @Test
    public void shouldFetchAccessToken() throws Exception {
        final StubbedTokenInfo tokenInfo = new StubbedTokenInfo("googleplus", "access-token", "secret", "token", 3600, "profile", "id-token");

        when(request.authConfigs()).thenReturn(Collections.singletonList(authConfig));
        when(request.requestParameters()).thenReturn(Collections.singletonMap("code", "code-received-in-previous-step"));
        when(provider.accessToken(request.requestParameters())).thenReturn(tokenInfo);

        final GoPluginApiResponse response = executor.execute();


        String expectedJSON = "{\n" +
                "  \"provider_id\": \"googleplus\",\n" +
                "  \"access_token\": \"access-token\",\n" +
                "  \"secret\": \"secret\",\n" +
                "  \"token_type\": \"token\",\n" +
                "  \"expires_in\": 3600,\n" +
                "  \"scope\": \"profile\",\n" +
                "  \"id_token\": \"id-token\"\n" +
                "}";

        assertThat(response.responseCode(), is(200));
        JSONAssert.assertEquals(expectedJSON, response.responseBody(), true);
    }

    private class StubbedTokenInfo extends TokenInfo {
        public StubbedTokenInfo(String providerId, String accessToken, String secret, String tokenType, int expiresIn, String scope, String idToken) {
            super(providerId, accessToken, secret, tokenType, expiresIn, scope, idToken);
        }
    }
}