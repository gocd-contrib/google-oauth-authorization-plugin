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

package cd.go.authorization.google.requests;

import cd.go.authorization.google.executors.RequestExecutor;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Map;

import static cd.go.authorization.google.utils.Util.GSON;

public abstract class Request {
    protected GoPluginApiRequest apiRequest;

    public static <T extends Request> T from(GoPluginApiRequest apiRequest, Class<T> clazz) {
        final T request = GSON.fromJson(apiRequest.requestBody(), clazz);
        request.apiRequest = apiRequest;
        return request;
    }

    public abstract RequestExecutor executor();

    public Map<String, String> requestParameters() {
        return apiRequest.requestParameters();
    }

    public Map<String, String> requestHeaders() {
        return apiRequest.requestHeaders();
    }

    public GoPluginApiResponse execute() throws Exception {
        return executor().execute();
    }
}
