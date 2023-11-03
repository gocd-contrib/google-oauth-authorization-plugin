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
import cd.go.authorization.google.utils.Util;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static cd.go.authorization.google.GooglePlugin.LOG;
import static cd.go.authorization.google.utils.Util.isNotBlank;
import static java.text.MessageFormat.format;

public class GoogleApiClient {
    private static final String API_ERROR_MSG = "Api call to `{0}` failed with error: `{1}`";
    private final GoogleConfiguration googleConfiguration;
    private final OkHttpClient httpClient;

    public GoogleApiClient(GoogleConfiguration googleConfiguration) {
        this(googleConfiguration,
                new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build()
        );
    }

    public GoogleApiClient(GoogleConfiguration googleConfiguration, OkHttpClient httpClient) {
        this.googleConfiguration = googleConfiguration;
        this.httpClient = httpClient;
    }

    public String authorizationServerUrl(String callbackUrl) {
        LOG.debug("[GoogleApiClient] Generating google oauth url.");

        return HttpUrl.parse("https://accounts.google.com/o/oauth2/auth")
                .newBuilder()
                .addQueryParameter("client_id", googleConfiguration.clientId())
                .addQueryParameter("response_type", "code")
                .addQueryParameter("redirect_uri", callbackUrl)
                .addQueryParameter("scope", "email profile")
                .addQueryParameter("state", UUID.randomUUID().toString())
                .build().toString();
    }

    public TokenInfo fetchAccessToken(Map<String, String> params) throws Exception {
        final String code = params.get("code");
        if (Util.isBlank(code)) {
            throw new RuntimeException("[GoogleApiClient] Authorization code must not be null.");
        }

        LOG.debug("[GoogleApiClient] Fetching access token using authorization code.");

        final String accessTokenUrl = HttpUrl.parse(googleConfiguration.googleApiUrl())
                .newBuilder()
                .addPathSegments("oauth2")
                .addPathSegments("v4")
                .addPathSegments("token")
                .build()
                .toString();

        final FormBody formBody = new FormBody.Builder()
                .add("client_id", googleConfiguration.clientId())
                .add("client_secret", googleConfiguration.clientSecret())
                .add("code", code)
                .add("grant_type", "authorization_code")
                .add("redirect_uri", CallbackURL.instance().getCallbackURL()).build();

        final Request request = new Request.Builder()
                .url(accessTokenUrl)
                .addHeader("Accept", "application/json")
                .post(formBody)
                .build();

        return executeRequest(request, response -> TokenInfo.fromJSON(response.body().string()));
    }

    public GoogleUser userProfile(TokenInfo tokenInfo) throws Exception {
        validateTokenInfo(tokenInfo);

        LOG.debug("[GoogleApiClient] Fetching user profile using access token.");

        final String userProfileUrl = HttpUrl.parse(googleConfiguration.googleApiUrl())
                .newBuilder()
                .addPathSegments("oauth2")
                .addPathSegments("v1")
                .addPathSegments("userinfo")
                .addQueryParameter("access_token", tokenInfo.accessToken())
                .toString();

        final Request request = new Request.Builder().url(userProfileUrl).build();

        return executeRequest(request, response -> GoogleUser.fromJSON(response.body().string()));
    }

    private interface Callback<T> {
        T onResponse(Response response) throws IOException;
    }

    private <T> T executeRequest(Request request, Callback<T> callback) throws IOException {
        final Response response = httpClient.newCall(request).execute();

        if (!response.isSuccessful()) {
            final String responseBody = response.body().string();
            final String errorMessage = isNotBlank(responseBody) ? responseBody : response.message();
            throw new RuntimeException(format(API_ERROR_MSG, request.url().encodedPath(), errorMessage));
        }

        return callback.onResponse(response);
    }

    private void validateTokenInfo(TokenInfo tokenInfo) {
        if (tokenInfo == null) {
            throw new RuntimeException("[GoogleApiClient] TokenInfo must not be null.");
        }
    }
}
