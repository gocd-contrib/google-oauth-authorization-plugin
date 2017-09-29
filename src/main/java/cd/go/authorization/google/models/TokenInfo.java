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

import static cd.go.authorization.google.utils.Util.GSON;

public class TokenInfo {
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("expires_in")
    @Expose
    private long expiresIn;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;

    TokenInfo() {
    }

    public TokenInfo(String accessToken, long expiresIn, String tokenType, String refreshToken) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
    }

    public String accessToken() {
        return accessToken;
    }

    public String tokenType() {
        return tokenType;
    }

    public long expiresIn() {
        return expiresIn;
    }

    public String refreshToken() {
        return refreshToken;
    }

    public String toJSON() {
        return GSON.toJson(this);
    }

    public static TokenInfo fromJSON(String json) {
        return GSON.fromJson(json, TokenInfo.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenInfo tokenInfo = (TokenInfo) o;

        if (expiresIn != tokenInfo.expiresIn) return false;
        if (accessToken != null ? !accessToken.equals(tokenInfo.accessToken) : tokenInfo.accessToken != null)
            return false;
        if (tokenType != null ? !tokenType.equals(tokenInfo.tokenType) : tokenInfo.tokenType != null) return false;
        return refreshToken != null ? refreshToken.equals(tokenInfo.refreshToken) : tokenInfo.refreshToken == null;
    }

    @Override
    public int hashCode() {
        int result = accessToken != null ? accessToken.hashCode() : 0;
        result = 31 * result + (int) (expiresIn ^ (expiresIn >>> 32));
        result = 31 * result + (tokenType != null ? tokenType.hashCode() : 0);
        result = 31 * result + (refreshToken != null ? refreshToken.hashCode() : 0);
        return result;
    }
}
