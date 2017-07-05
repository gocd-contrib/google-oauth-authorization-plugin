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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.brickred.socialauth.Permission;
import org.brickred.socialauth.util.AccessGrant;

import static cd.go.authorization.google.utils.Util.GSON;

public class TokenInfo {

    @Expose
    @SerializedName("provider_id")
    private String providerId;

    @Expose
    @SerializedName("access_token")
    private String accessToken;

    @Expose
    @SerializedName("secret")
    private String secret;

    @Expose
    @SerializedName("token_type")
    private String tokenType;

    @Expose
    @SerializedName("expires_in")
    private int expiresIn;

    @Expose
    @SerializedName("scope")
    private String scope;

    @Expose
    @SerializedName("id_token")
    private String idToken;

    private TokenInfo() {
    }

    public TokenInfo(AccessGrant accessGrant) {
        this(
                accessGrant.getProviderId(),
                accessGrant.getKey(),
                accessGrant.getSecret(),
                attribute(accessGrant, "token_type"),
                Integer.parseInt(attribute(accessGrant, "expires")),
                accessGrant.getPermission().getScope(),
                attribute(accessGrant, "id_token")
        );
    }

    protected TokenInfo(String providerId, String accessToken, String secret, String tokenType, int expiresIn, String scope, String idToken) {
        this.providerId = providerId;
        this.accessToken = accessToken;
        this.secret = secret;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.idToken = idToken;
    }

    public String accessToken() {
        return accessToken;
    }

    public String tokenType() {
        return tokenType;
    }

    public int expiresIn() {
        return expiresIn;
    }

    public String scope() {
        return scope;
    }

    public String idToken() {
        return idToken;
    }

    public String toJSON() {
        return GSON.toJson(this);
    }

    public AccessGrant toAccessGrant() {
        final AccessGrant accessGrant = new AccessGrant(accessToken, secret);
        accessGrant.setProviderId(providerId);
        accessGrant.setPermission(new Permission(scope));
        accessGrant.setAttribute("expires", expiresIn);
        accessGrant.setAttribute("id_token", idToken);
        accessGrant.setAttribute("token_type", tokenType);
        return accessGrant;
    }

    private static String attribute(AccessGrant accessGrant, String attributeName) {
        return accessGrant.getAttribute(attributeName).toString();
    }
}
