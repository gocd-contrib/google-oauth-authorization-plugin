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
import org.brickred.socialauth.Permission;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.util.AccessGrant;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cd.go.authorization.google.GooglePlugin.LOG;
import static java.text.MessageFormat.format;

public class GoogleProvider implements Provider<GoogleConfiguration> {
    private final GoogleConfiguration googleConfiguration;
    private final SocialAuthManager authManager;
    private final URLCache urlCache;

    public GoogleProvider(GoogleConfiguration pluginConfiguration, SocialAuthManager authManager) {
        this(pluginConfiguration, authManager, URLCache.getInstance());
    }

    GoogleProvider(GoogleConfiguration pluginConfiguration, SocialAuthManager authManager, URLCache urlCache) {
        this.googleConfiguration = pluginConfiguration;
        this.authManager = authManager;
        this.urlCache = urlCache;
    }

    @Override
    public String getProviderName() {
        return "googleplus";
    }

    @Override
    public Permission permission() {
        return Permission.AUTHENTICATE_ONLY;
    }

    public void verifyConnection() throws Exception {
        //TODO:
    }

    @Override
    public String authorizationServerUrl(String callbackUrl) throws Exception {
        final String authenticationUrl = authManager.getAuthenticationUrl(getProviderName(), callbackUrl, permission());
        String state = urlParams(authenticationUrl).get("state");
        urlCache.getInstance().cache(state, callbackUrl);
        return authenticationUrl;
    }

    @Override
    public TokenInfo accessToken(Map<String, String> params) throws Exception {
        final AccessGrant accessGrant = authManager.createAccessGrant(getProviderName(), params.get("code"), urlCache.getAndRemove(params.get("state")));
        authManager.disconnectProvider(getProviderName());
        return new TokenInfo(accessGrant);
    }

    @Override
    public User userProfile(AccessGrant accessGrant) throws Exception {
        try {
            final Profile userProfile = authManager.connect(accessGrant).getUserProfile();
            final List<String> allowedDomains = googleConfiguration.allowedDomains();
            final User user = new User(userProfile);

            if (allowedDomains.isEmpty()) {
                return user;
            }

            final String domainOfLoggedInUser = userProfile.getEmail().substring(userProfile.getEmail().indexOf("@") + 1);
            for (String domainName : allowedDomains) {
                if (domainOfLoggedInUser.equals(domainName)) {
                    return user;
                }
            }

            LOG.error(format("Domain `{0}` is invalid. Supported domains are `{1}`", domainOfLoggedInUser, allowedDomains));
            throw InvalidDomainException.invalidDomain(domainOfLoggedInUser, allowedDomains.toString());
        } finally {
            authManager.disconnectProvider(getProviderName());
        }
    }

    @Override
    public GoogleConfiguration pluginConfiguration() {
        return googleConfiguration;
    }

    private Map<String, String> urlParams(String url) throws UnsupportedEncodingException, MalformedURLException {
        URL urlObject = new URL(url);
        Map<String, String> query_pairs = new LinkedHashMap();
        String query = urlObject.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}
