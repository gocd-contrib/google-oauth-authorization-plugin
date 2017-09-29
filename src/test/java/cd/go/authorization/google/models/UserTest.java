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

import cd.go.authorization.google.GoogleUser;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static cd.go.authorization.google.utils.Util.GSON;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UserTest {

    @Test
    public void shouldSerializeToJSON() throws Exception {
        final User user = new User("foo", "bar", "baz");
        final String expectedJSON = "{\"username\":\"foo\",\"display_name\":\"bar\",\"email\":\"baz\"}";

        JSONAssert.assertEquals(expectedJSON, GSON.toJson(user), true);
    }

    @Test
    public void shouldCreateUserFromGoogleUser() throws Exception {
        final GoogleUser googleUser = new GoogleUser("foo@bar.com", "Foo Bar");
        final User user = new User(googleUser);

        assertThat(user.username(), is("foo@bar.com"));
        assertThat(user.displayName(), is("Foo Bar"));
        assertThat(user.emailId(), is("foo@bar.com"));
    }
}