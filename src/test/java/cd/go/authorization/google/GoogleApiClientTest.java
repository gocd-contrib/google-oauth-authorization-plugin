package cd.go.authorization.google;

import cd.go.authorization.google.models.GoogleConfiguration;
import cd.go.authorization.google.models.TokenInfo;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GoogleApiClientTest {

    @Mock
    private GoogleConfiguration googleConfiguration;
    private MockWebServer server;
    private GoogleApiClient googleApiClient;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        server = new MockWebServer();
        server.start();

        when(googleConfiguration.clientId()).thenReturn("client-id");
        when(googleConfiguration.clientSecret()).thenReturn("client-secret");

        CallbackURL.instance().updateRedirectURL("callback-url");

        googleApiClient = new GoogleApiClient(googleConfiguration);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void shouldReturnAuthorizationServerUrl() throws Exception {
        final String authorizationServerUrl = googleApiClient.authorizationServerUrl("call-back-url");

        assertThat(authorizationServerUrl, startsWith("https://accounts.google.com/o/oauth2/auth?client_id=client-id&response_type=code&redirect_uri=call-back-url&scope=email%20profile&state="));
    }

    @Test
    public void shouldFetchTokenInfoUsingAuthorizationCode() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(new TokenInfo("token-444248275346-5758603453985735", 7200, "bearer", "refresh-token").toJSON()));

        when(googleConfiguration.googleApiUrl()).thenReturn(server.url("/").toString());

        final TokenInfo tokenInfo = googleApiClient.fetchAccessToken(Collections.singletonMap("code", "some-code"));

        assertThat(tokenInfo.accessToken(), is("token-444248275346-5758603453985735"));

        RecordedRequest request = server.takeRequest();
        assertEquals("POST /oauth2/v4/token HTTP/1.1", request.getRequestLine());
        assertEquals("application/x-www-form-urlencoded", request.getHeader("Content-Type"));
        assertEquals("client_id=client-id&client_secret=client-secret&code=some-code&grant_type=authorization_code&redirect_uri=callback-url", request.getBody().readUtf8());
    }

    @Test
    public void shouldFetchUserProfile() throws Exception {
        final TokenInfo tokenInfo = new TokenInfo("token-444248275346-5758603453985735", 7200, "bearer", "refresh-token");
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(new GoogleUser("foo@bar.com", "Display Name").toJSON()));

        when(googleConfiguration.googleApiUrl()).thenReturn(server.url("/").toString());

        final GoogleUser googleUser = googleApiClient.userProfile(tokenInfo);

        assertThat(googleUser.getEmail(), is("foo@bar.com"));

        RecordedRequest request = server.takeRequest();
        assertEquals("GET /oauth2/v1/userinfo?access_token=token-444248275346-5758603453985735 HTTP/1.1", request.getRequestLine());
    }

    @Test
    public void shouldErrorOutWhenAPIRequestFails() throws Exception {
        final TokenInfo tokenInfo = new TokenInfo("token-444248275346-5758603453985735", 7200, "bearer", "refresh-token");

        server.enqueue(new MockResponse().setResponseCode(403).setBody("Unauthorized"));

        when(googleConfiguration.googleApiUrl()).thenReturn(server.url("/").toString());

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Api call to `/oauth2/v1/userinfo` failed with error: `Unauthorized`");

        googleApiClient.userProfile(tokenInfo);
    }
}