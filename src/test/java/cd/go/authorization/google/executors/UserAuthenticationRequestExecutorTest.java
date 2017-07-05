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

import cd.go.authorization.google.GoogleProvider;
import cd.go.authorization.google.exceptions.NoAuthorizationConfigurationException;
import cd.go.authorization.google.models.AuthConfig;
import cd.go.authorization.google.models.GoogleConfiguration;
import cd.go.authorization.google.models.TokenInfo;
import cd.go.authorization.google.models.User;
import cd.go.authorization.google.requests.UserAuthenticationRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.brickred.socialauth.util.AccessGrant;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserAuthenticationRequestExecutorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private UserAuthenticationRequest request;
    @Mock
    private AuthConfig authConfig;
    @Mock
    private GoogleConfiguration googleConfiguration;
    @Mock
    private GoogleProvider googleProvider;
    private UserAuthenticationRequestExecutor executor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(authConfig.getConfiguration()).thenReturn(googleConfiguration);
        when(googleConfiguration.provider()).thenReturn(googleProvider);

        executor = new UserAuthenticationRequestExecutor(request);
    }

    @Test
    public void shouldErrorOutIfAuthConfigIsNotProvided() throws Exception {
        when(request.authConfigs()).thenReturn(Collections.emptyList());

        thrown.expect(NoAuthorizationConfigurationException.class);
        thrown.expectMessage("[Authenticate] No authorization configuration found.");

        executor.execute();
    }

    @Test
    public void shouldAuthenticate() throws Exception {
        final StubbedTokenInfo tokenInfo = new StubbedTokenInfo("googleplus", "access-token", "token", "secret", 3600, "profile", "id-token");
        when(request.authConfigs()).thenReturn(Collections.singletonList(authConfig));
        when(request.tokenInfo()).thenReturn(tokenInfo);
        when(googleProvider.userProfile(any(AccessGrant.class))).thenReturn(new User("bford", "Bob", "email"));

        final GoPluginApiResponse response = executor.execute();

        String expectedJSON = "{\n" +
                "  \"roles\": [],\n" +
                "  \"user\": {\n" +
                "    \"username\": \"bford\",\n" +
                "    \"display_name\": \"Bob\",\n" +
                "    \"email\": \"email\"\n" +
                "  }\n" +
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