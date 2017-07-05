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

public class Capabilities {
    @Expose
    @SerializedName("supported_auth_type")
    private final SupportedAuthType supportedAuthType;
    @Expose
    @SerializedName("can_search")
    private final boolean canSearch;
    @Expose
    @SerializedName("can_authorize")
    private final boolean canAuthorize;

    public Capabilities(SupportedAuthType supportedAuthType, boolean canSearch, boolean canAuthorize) {
        this.supportedAuthType = supportedAuthType;
        this.canSearch = canSearch;
        this.canAuthorize = canAuthorize;
    }

    public String toJSON() {
        return GSON.toJson(this);
    }
}
