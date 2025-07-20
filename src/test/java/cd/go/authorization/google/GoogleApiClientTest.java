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

import cd.go.authorization.google.models.GoogleConfiguration;
import cd.go.authorization.google.models.TokenInfo;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class GoogleApiClientTest {

    @Mock
    private GoogleConfiguration googleConfiguration;
    private MockWebServer server;
    private GoogleApiClient googleApiClient;


    @BeforeEach
    public void setUp() throws Exception {
        openMocks(this);

        server = new MockWebServer();
        server.start();

        when(googleConfiguration.clientId()).thenReturn("client-id");
        when(googleConfiguration.clientSecret()).thenReturn("client-secret");

        CallbackURL.instance().updateRedirectURL("callback-url");

        googleApiClient = new GoogleApiClient(googleConfiguration);
    }

    @AfterEach
    public void tearDown() throws Exception {
        server.close();
    }

    @Test
    public void shouldReturnAuthorizationServerUrl() throws Exception {
        final String authorizationServerUrl = googleApiClient.authorizationServerUrl("call-back-url");

        assertThat(authorizationServerUrl, startsWith("https://accounts.google.com/o/oauth2/auth?client_id=client-id&response_type=code&redirect_uri=call-back-url&scope=email%20profile&state="));
    }

    @Test
    public void shouldFetchTokenInfoUsingAuthorizationCode() throws Exception {
        server.enqueue(new MockResponse.Builder()
                .code(200)
                .body(new TokenInfo("token-444248275346-5758603453985735", 7200, "bearer", "refresh-token").toJSON())
                .build());

        when(googleConfiguration.googleApiUrl()).thenReturn(server.url("/").toString());

        final TokenInfo tokenInfo = googleApiClient.fetchAccessToken(Collections.singletonMap("code", "some-code"));

        assertThat(tokenInfo.accessToken(), is("token-444248275346-5758603453985735"));

        RecordedRequest request = server.takeRequest();
        assertEquals("POST /oauth2/v4/token HTTP/1.1", request.getRequestLine());
        assertEquals("application/x-www-form-urlencoded", request.getHeaders().get("Content-Type"));
        assertEquals("client_id=client-id&client_secret=client-secret&code=some-code&grant_type=authorization_code&redirect_uri=callback-url", request.getBody().utf8());
    }

    @Test
    public void shouldFetchUserProfile() throws Exception {
        final TokenInfo tokenInfo = new TokenInfo("token-444248275346-5758603453985735", 7200, "bearer", "refresh-token");
        server.enqueue(new MockResponse.Builder()
                .code(200)
                .body(new GoogleUser("foo@bar.com", "Display Name").toJSON())
                .build());

        when(googleConfiguration.googleApiUrl()).thenReturn(server.url("/").toString());

        final GoogleUser googleUser = googleApiClient.userProfile(tokenInfo);

        assertThat(googleUser.getEmail(), is("foo@bar.com"));

        RecordedRequest request = server.takeRequest();
        assertEquals("GET /oauth2/v1/userinfo?access_token=token-444248275346-5758603453985735 HTTP/1.1", request.getRequestLine());
    }

    @Test
    public void shouldErrorOutWhenAPIRequestFails() throws Exception {
        final TokenInfo tokenInfo = new TokenInfo("token-444248275346-5758603453985735", 7200, "bearer", "refresh-token");

        server.enqueue(new MockResponse.Builder().code(403).body("Unauthorized").build());

        when(googleConfiguration.googleApiUrl()).thenReturn(server.url("/").toString());

        Throwable t = assertThrows(RuntimeException.class, () -> googleApiClient.userProfile(tokenInfo));
        assertThat(t.getMessage(), is("Api call to `/oauth2/v1/userinfo` failed with error: `Unauthorized`"));
    }
}