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

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TokenInfoTest {

    @Test
    public void shouldDeserializeJSON() throws Exception {
        final TokenInfo tokenInfo = TokenInfo.fromJSON("{\n" +
                "  \"access_token\": \"31239032-xycs.xddasdasdasda\",\n" +
                "  \"expires_in\": 7200,\n" +
                "  \"token_type\": \"foo-type\",\n" +
                "  \"refresh_token\": \"refresh-xysaddasdjlascdas\"\n" +
                "}");

        assertThat(tokenInfo.accessToken(), is("31239032-xycs.xddasdasdasda"));
        assertThat(tokenInfo.expiresIn(), is(7200L));
        assertThat(tokenInfo.tokenType(), is("foo-type"));
        assertThat(tokenInfo.refreshToken(), is("refresh-xysaddasdjlascdas"));
    }

    @Test
    public void shouldSerializeToJSON() throws Exception {
        final TokenInfo tokenInfo = new TokenInfo("31239032-xycs.xddasdasdasda", 7200, "foo-type", "refresh-xysaddasdjlascdas");

        final String expectedJSON = "{\n" +
                "  \"access_token\": \"31239032-xycs.xddasdasdasda\",\n" +
                "  \"expires_in\": 7200,\n" +
                "  \"token_type\": \"foo-type\",\n" +
                "  \"refresh_token\": \"refresh-xysaddasdjlascdas\"\n" +
                "}";

        JSONAssert.assertEquals(expectedJSON, tokenInfo.toJSON(), true);
    }
}