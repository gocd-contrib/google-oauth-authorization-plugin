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

import static org.hamcrest.Matchers.is;
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
    private GoogleConfiguration googleConfiguration;
    @Mock
    private GoogleApiClient googleApiClient;
    private FetchAccessTokenRequestExecutor executor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(authConfig.getConfiguration()).thenReturn(googleConfiguration);
        when(googleConfiguration.googleApiClient()).thenReturn(googleApiClient);

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
        final TokenInfo tokenInfo = new TokenInfo("31239032-xycs.xddasdasdasda", 7200, "foo-type", "refresh-xysaddasdjlascdas");

        when(request.authConfigs()).thenReturn(Collections.singletonList(authConfig));
        when(request.requestParameters()).thenReturn(Collections.singletonMap("code", "code-received-in-previous-step"));
        when(googleApiClient.fetchAccessToken(request.requestParameters())).thenReturn(tokenInfo);

        final GoPluginApiResponse response = executor.execute();


        final String expectedJSON = "{\n" +
                "  \"access_token\": \"31239032-xycs.xddasdasdasda\",\n" +
                "  \"expires_in\": 7200,\n" +
                "  \"token_type\": \"foo-type\",\n" +
                "  \"refresh_token\": \"refresh-xysaddasdjlascdas\"\n" +
                "}";

        assertThat(response.responseCode(), is(200));
        JSONAssert.assertEquals(expectedJSON, response.responseBody(), true);
    }
}