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

import cd.go.authorization.google.exceptions.InvalidDomainException;
import cd.go.authorization.google.models.GoogleConfiguration;
import cd.go.authorization.google.models.TokenInfo;
import cd.go.authorization.google.models.User;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Permission;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.util.AccessGrant;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class GoogleProviderTest {

    private GoogleConfiguration googleConfiguration;
    private GoogleProvider googleProvider;
    private SocialAuthManager socialAuthManager;
    private URLCache urlCache;

    @Before
    public void setUp() throws Exception {
        googleConfiguration = new GoogleConfiguration("example.com",
                "client-id", "client-secret");
        socialAuthManager = mock(SocialAuthManager.class);
        urlCache = URLCache.getInstance();

        googleProvider = new GoogleProvider(googleConfiguration, socialAuthManager, urlCache);
    }

    @Test
    public void shouldBuildAuthorizationServerUrl() throws Exception {
        final String callbackUrl = "https://go.server.url/plugin/cd.go.authorization.google/authenticate";
        when(socialAuthManager.getAuthenticationUrl(googleProvider.getProviderName(), callbackUrl, googleProvider.permission())).thenReturn("https://authentication-url?state=foo");

        assertThat(googleProvider.authorizationServerUrl(callbackUrl), is("https://authentication-url?state=foo"));
        verify(socialAuthManager).getAuthenticationUrl(googleProvider.getProviderName(), callbackUrl, googleProvider.permission());
    }

    @Test
    public void shouldGetAccessTokenUsingCode() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("code", "some-code");
        params.put("state", "foo");
        final AccessGrant accessGrant = mock(AccessGrant.class);
        urlCache.cache("foo", "bar");

        when(accessGrant.getProviderId()).thenReturn(googleProvider.getProviderName());
        when(accessGrant.getKey()).thenReturn("access-token");
        when(accessGrant.getSecret()).thenReturn("secret");
        when(accessGrant.getPermission()).thenReturn(new Permission("scope"));
        when(accessGrant.getAttribute("token_type")).thenReturn("token_type");
        when(accessGrant.getAttribute("expires")).thenReturn("3600");
        when(accessGrant.getAttribute("id_token")).thenReturn("id_token");
        when(socialAuthManager.createAccessGrant(googleProvider.getProviderName(), "some-code", "bar")).thenReturn(accessGrant);

        final TokenInfo tokenInfo = googleProvider.accessToken(params);

        assertNotNull(tokenInfo);
        assertThat(tokenInfo.accessToken(), is("access-token"));
        verify(socialAuthManager).createAccessGrant(googleProvider.getProviderName(), "some-code", "bar");
        verify(socialAuthManager).disconnectProvider(googleProvider.getProviderName());
    }

    @Test
    public void shouldGetUserUsingAccessToken() throws Exception {
        final AccessGrant accessGrant = mock(AccessGrant.class);
        final AuthProvider authProvider = mock(AuthProvider.class);
        final Profile profile = mock(Profile.class);

        when(socialAuthManager.connect(accessGrant)).thenReturn(authProvider);
        when(authProvider.getUserProfile()).thenReturn(profile);
        when(profile.getEmail()).thenReturn("bob@example.com");
        when(profile.getFullName()).thenReturn("Bob Ford");

        final User user = googleProvider.userProfile(accessGrant);

        assertThat(user, is(new User("bob@example.com", "Bob Ford", "bob@example.com")));
    }

    @Test(expected = InvalidDomainException.class)
    public void shouldErrorOutIfDomainIsNotWhiteListed() throws Exception {
        final AccessGrant accessGrant = mock(AccessGrant.class);
        final AuthProvider authProvider = mock(AuthProvider.class);
        final Profile profile = mock(Profile.class);

        when(socialAuthManager.connect(accessGrant)).thenReturn(authProvider);
        when(authProvider.getUserProfile()).thenReturn(profile);
        when(profile.getEmail()).thenReturn("bob@foo.com");
        when(profile.getFullName()).thenReturn("Bob Ford");

        googleProvider.userProfile(accessGrant);
    }
}