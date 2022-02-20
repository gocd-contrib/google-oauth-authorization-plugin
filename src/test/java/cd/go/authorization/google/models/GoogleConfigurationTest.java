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

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;

public class GoogleConfigurationTest {

    @Test
    public void shouldDeserializeGoogleConfiguration() throws Exception {
        final GoogleConfiguration googleConfiguration = GoogleConfiguration.fromJSON("{\n" +
                "  \"AllowedDomains\": \"example.co.in\",\n" +
                "  \"ClientId\": \"client-id\",\n" +
                "  \"ClientSecret\": \"client-secret\"\n" +
                "}");

        assertThat(googleConfiguration.allowedDomains(), contains("example.co.in"));
        assertThat(googleConfiguration.clientId(), is("client-id"));
        assertThat(googleConfiguration.clientSecret(), is("client-secret"));
    }

    @Test
    public void shouldSerializeToJSON() throws Exception {
        GoogleConfiguration googleConfiguration = new GoogleConfiguration(
                "example.co.in", "client-id", "client-secret");

        String expectedJSON = "{\n" +
                "  \"AllowedDomains\": \"example.co.in\",\n" +
                "  \"ClientId\": \"client-id\",\n" +
                "  \"ClientSecret\": \"client-secret\"\n" +
                "}";

        JSONAssert.assertEquals(expectedJSON, googleConfiguration.toJSON(), true);

    }

    @Test
    public void shouldConvertConfigurationToProperties() throws Exception {
        GoogleConfiguration googleConfiguration = new GoogleConfiguration(
                "example.co.in", "client-id", "client-secret");

        final Map<String, String> properties = googleConfiguration.toProperties();

        assertThat(properties, hasEntry("AllowedDomains", "example.co.in"));
        assertThat(properties, hasEntry("ClientId", "client-id"));
        assertThat(properties, hasEntry("ClientSecret", "client-secret"));
    }
}