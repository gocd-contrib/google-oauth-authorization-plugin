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

import cd.go.authorization.google.GoogleProvider;
import cd.go.authorization.google.Provider;
import cd.go.authorization.google.annotation.ProfileField;
import cd.go.authorization.google.utils.Util;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static cd.go.authorization.google.utils.Util.GSON;

public class GoogleConfiguration implements PluginConfiguration {

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

    public GoogleConfiguration() {
    }

    public GoogleConfiguration(String allowedDomains, String clientId, String clientSecret) {
        this.allowedDomains = allowedDomains;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public Properties configuration() {
        Properties properties = new Properties();
        properties.put("googleapis.com.consumer_key", clientId);
        properties.put("googleapis.com.consumer_secret", clientSecret);
        properties.put("www.google.com.custom_permissions", "https://www.googleapis.com/auth/userinfo.email,https://www.googleapis.com/auth/userinfo.profile,https://www.googleapis.com/auth/plus.me");
        return properties;
    }

   public List<String> allowedDomains() {
        return Util.splitIntoLinesAndTrimSpaces(allowedDomains);
    }

    @Override
    public String clientId() {
        return clientId;
    }

    @Override
    public String clientSecret() {
        return clientSecret;
    }

    @Override
    public String toJSON() {
        return GSON.toJson(this);
    }

    @Override
    public Provider provider() throws Exception {
        return new GoogleProvider(this, socialAuthManager());
    }

    private SocialAuthManager socialAuthManager() throws Exception {
        final SocialAuthConfig socialAuthConfiguration = SocialAuthConfig.getDefault();
        socialAuthConfiguration.load(configuration());
        final SocialAuthManager manager = new SocialAuthManager();
        manager.setSocialAuthConfig(socialAuthConfiguration);
        return manager;
    }

    public static GoogleConfiguration fromJSON(String json) {
        return GSON.fromJson(json, GoogleConfiguration.class);
    }

    public Map<String, String> toProperties() {
        return GSON.fromJson(toJSON(), new TypeToken<Map<String, String>>() {
        }.getType());
    }
}
