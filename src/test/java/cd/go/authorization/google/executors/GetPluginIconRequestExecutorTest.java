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

import cd.go.authorization.google.utils.Util;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static java.util.Base64.getDecoder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetPluginIconRequestExecutorTest {

    @Test
    public void rendersIconInBase64() throws Exception {
        GoPluginApiResponse response = new GetPluginIconRequestExecutor().execute();
        HashMap<String, String> hashMap = new Gson().fromJson(response.responseBody(), HashMap.class);
        assertThat(hashMap.size(), is(2));
        assertThat(hashMap.get("content_type"), is("image/png"));
        assertThat(Util.readResourceBytes("/logo.png"), is(getDecoder().decode(hashMap.get("data"))));
    }
}