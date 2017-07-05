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

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import static cd.go.authorization.google.utils.Util.GSON;

public class OpenidConfiguration {

    @Expose
    @SerializedName("issuer")
    private String issuer;

    @Expose
    @SerializedName("authorization_endpoint")
    private String authorizationEndpoint;

    @Expose
    @SerializedName("token_endpoint")
    private String tokenEndpoint;

    @Expose
    @SerializedName("userinfo_endpoint")
    private String userinfoEndpoint;

    @Expose
    @SerializedName("jwks_uri")
    private String jwksUri;

    @Expose
    @SerializedName("response_types_supported")
    private List<String> responseTypesSupported;

    @Expose
    @SerializedName("response_modes_supported")
    private List<String> responseModesSupported;

    @Expose
    @SerializedName("grant_types_supported")
    private List<String> grantTypesSupported;

    @Expose
    @SerializedName("subject_types_supported")
    private List<String> subjectTypesSupported;

    @Expose
    @SerializedName("id_token_signing_alg_values_supported")
    private List<String> idTokenSigningAlgValuesSupported;

    @Expose
    @SerializedName("scopes_supported")
    private List<String> scopesSupported;

    @Expose
    @SerializedName("token_endpoint_auth_methods_supported")
    private List<String> tokenEndpointAuthMethodsSupported;

    @Expose
    @SerializedName("claims_supported")
    private List<String> claimsSupported;

    @Expose
    @SerializedName("code_challenge_methods_supported")
    private List<String> codeChallengeMethodsSupported;

    @Expose
    @SerializedName("introspection_endpoint_auth_methods_supported")
    private List<String> introspectionEndpointAuthMethodsSupported;

    @Expose
    @SerializedName("revocation_endpoint_auth_methods_supported")
    private List<String> revocationEndpointAuthMethodsSupported;

    @Expose
    @SerializedName("introspection_endpoint")
    private String introspectionEndpoint;

    @Expose
    @SerializedName("revocation_endpoint")
    private String revocationEndpoint;

    public static OpenidConfiguration fromJSON(Reader reader) {
        return GSON.fromJson(reader, OpenidConfiguration.class);
    }

    public String issuer() {
        return issuer;
    }

    public String authorizationEndpoint() {
        return authorizationEndpoint;
    }

    public String tokenEndpoint() {
        return tokenEndpoint;
    }

    public String userinfoEndpoint() {
        return userinfoEndpoint;
    }

    public String jwksUri() {
        return jwksUri;
    }

    public List<String> responseTypesSupported() {
        return Collections.unmodifiableList(responseTypesSupported);
    }

    public List<String> responseModesSupported() {
        return Collections.unmodifiableList(responseModesSupported);
    }

    public List<String> grantTypesSupported() {
        return Collections.unmodifiableList(grantTypesSupported);
    }

    public List<String> subjectTypesSupported() {
        return Collections.unmodifiableList(subjectTypesSupported);
    }

    public List<String> idTokenSigningAlgValuesSupported() {
        return Collections.unmodifiableList(idTokenSigningAlgValuesSupported);
    }

    public List<String> scopesSupported() {
        return Collections.unmodifiableList(scopesSupported);
    }

    public List<String> tokenEndpointAuthMethodsSupported() {
        return Collections.unmodifiableList(tokenEndpointAuthMethodsSupported);
    }

    public List<String> claimsSupported() {
        return Collections.unmodifiableList(claimsSupported);
    }

    public List<String> codeChallengeMethodsSupported() {
        return Collections.unmodifiableList(codeChallengeMethodsSupported);
    }

    public List<String> introspectionEndpointAuthMethodsSupported() {
        return Collections.unmodifiableList(introspectionEndpointAuthMethodsSupported);
    }

    public List<String> revocationEndpointAuthMethodsSupported() {
        return Collections.unmodifiableList(revocationEndpointAuthMethodsSupported);
    }

    public String introspectionEndpoint() {
        return introspectionEndpoint;
    }

    public String revocationEndpoint() {
        return revocationEndpoint;
    }
}
