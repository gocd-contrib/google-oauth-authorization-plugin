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
import org.brickred.socialauth.Profile;

public class User {
    @Expose
    @SerializedName("username")
    private String username;

    @Expose
    @SerializedName("display_name")
    private String displayName;

    @Expose
    @SerializedName("email")
    private String emailId;

    public User(String username, String displayName, String emailId) {
        this.username = username;
        this.displayName = displayName;
        this.emailId = emailId == null ? null : emailId.toLowerCase().trim();
    }

    public User(Profile userProfile) {
        this(userProfile.getEmail(), userProfile.getFullName(), userProfile.getEmail());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (displayName != null ? !displayName.equals(user.displayName) : user.displayName != null) return false;
        return emailId != null ? emailId.equals(user.emailId) : user.emailId == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (emailId != null ? emailId.hashCode() : 0);
        return result;
    }
}