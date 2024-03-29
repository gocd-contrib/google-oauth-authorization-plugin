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

package cd.go.authorization.google.executors;

import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class GetCapabilitiesRequestExecutorTest {

    @Test
    public void shouldSupportWebAuthenticationCapabilities() throws Exception {
        GoPluginApiResponse response = new GetCapabilitiesRequestExecutor().execute();

        String expectedJSON = "{\n" +
                "    \"supported_auth_type\":\"web\",\n" +
                "    \"can_authorize\":false,\n" +
                "    \"can_search\":true,\n" +
                "    \"can_get_user_roles\":false\n" +
                "}";

        JSONAssert.assertEquals(expectedJSON, response.responseBody(), true);
    }
}
