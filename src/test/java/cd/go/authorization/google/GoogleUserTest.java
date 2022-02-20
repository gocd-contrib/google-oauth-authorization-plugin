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

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GoogleUserTest {

    @Test
    public void shouldDeserializeJSON() throws Exception {
        final GoogleUser googleUser = GoogleUser.fromJSON("{\n" +
                " \"id\": \"10536933078\",\n" +
                " \"email\": \"foo@example.com\",\n" +
                " \"verified_email\": true,\n" +
                " \"name\": \"Foo Bar\",\n" +
                " \"given_name\": \"Bar\",\n" +
                " \"family_name\": \"Foo\",\n" +
                " \"picture\": \"xfeowjflsad.jpg\",\n" +
                " \"locale\": \"en\",\n" +
                " \"hd\": \"example.com\"\n" +
                "}");

        assertThat(googleUser.getId(), is("10536933078"));
        assertThat(googleUser.getEmail(), is("foo@example.com"));
        assertThat(googleUser.isVerifiedEmail(), is(true));
        assertThat(googleUser.getName(), is("Foo Bar"));
        assertThat(googleUser.getGivenName(), is("Bar"));
        assertThat(googleUser.getFamilyName(), is("Foo"));
        assertThat(googleUser.getPicture(), is("xfeowjflsad.jpg"));
        assertThat(googleUser.getLocale(), is("en"));
        assertThat(googleUser.getHd(), is("example.com"));
    }

    @Test
    public void shouldExtractHdFromEmailWhenNotPresent() throws Exception {
        final GoogleUser googleUser = GoogleUser.fromJSON("{\n" +
                " \"id\": \"10536933078\",\n" +
                " \"email\": \"foo@gmail.com\",\n" +
                " \"verified_email\": true,\n" +
                " \"name\": \"Foo Bar\",\n" +
                " \"given_name\": \"Bar\",\n" +
                " \"family_name\": \"Foo\",\n" +
                " \"picture\": \"xfeowjflsad.jpg\",\n" +
                " \"locale\": \"en\"\n" +
                "}");

        assertThat(googleUser.getHd(), is("gmail.com"));
    }
}