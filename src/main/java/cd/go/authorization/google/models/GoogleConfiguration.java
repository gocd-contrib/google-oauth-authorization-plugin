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

package cd.go.authorization.google.models;

import cd.go.authorization.google.GoogleApiClient;
import cd.go.authorization.google.annotation.ProfileField;
import cd.go.authorization.google.utils.Util;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import static cd.go.authorization.google.utils.Util.GSON;

public class GoogleConfiguration {
    private static final String GOOGLE_API_URL = "https://www.googleapis.com";

    @Expose
    @SerializedName("ClientId")
    @ProfileField(key = "ClientId", required = true, secure = true)
    private String clientId;

    @Expose
    @SerializedName("ClientSecret")
    @ProfileField(key = "ClientSecret", required = true, secure = true)
    private String clientSecret;

    @Expose
    @SerializedName("AllowedDomains")
    @ProfileField(key = "AllowedDomains", required = false, secure = false)
    private String allowedDomains;

    private GoogleApiClient googleApiClient;

    public GoogleConfiguration() {
    }

    public GoogleConfiguration(String allowedDomains, String clientId, String clientSecret) {
        this.allowedDomains = allowedDomains;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public List<String> allowedDomains() {
        return Util.splitIntoLinesAndTrimSpaces(allowedDomains);
    }

    public String clientId() {
        return clientId;
    }

    public String clientSecret() {
        return clientSecret;
    }

    public String googleApiUrl() {
        return GOOGLE_API_URL;
    }

    public String toJSON() {
        return GSON.toJson(this);
    }

    public static GoogleConfiguration fromJSON(String json) {
        return GSON.fromJson(json, GoogleConfiguration.class);
    }

    public Map<String, String> toProperties() {
        return GSON.fromJson(toJSON(), new TypeToken<Map<String, String>>() {
        }.getType());
    }

    public GoogleApiClient googleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient(this);
        }

        return googleApiClient;
    }
}
