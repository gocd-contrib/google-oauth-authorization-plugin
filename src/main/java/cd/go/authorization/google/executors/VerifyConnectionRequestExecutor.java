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

import cd.go.authorization.google.annotation.MetadataValidator;
import cd.go.authorization.google.requests.VerifyConnectionRequest;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerifyConnectionRequestExecutor implements RequestExecutor {
    private static final Gson GSON = new Gson();
    private final VerifyConnectionRequest request;

    public VerifyConnectionRequestExecutor(VerifyConnectionRequest request) {
        this.request = request;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        List<Map<String, String>> errors = validate();
        if (!errors.isEmpty()) {
            return validationFailureResponse(errors);
        }

        return successResponse();
    }

    private List<Map<String, String>> validate() {
        return new MetadataValidator().validate(request.googleConfiguration());
    }

    private GoPluginApiResponse successResponse() {
        return responseWith("success", "Connection ok", null);
    }

    private GoPluginApiResponse validationFailureResponse(List<Map<String, String>> errors) {
        return responseWith("validation-failed", "Validation failed for the given Auth Config", errors);
    }

    private GoPluginApiResponse responseWith(String status, String message, List<Map<String, String>> errors) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);

        if (errors != null && !errors.isEmpty()) {
            response.put("errors", errors);
        }

        return DefaultGoPluginApiResponse.success(GSON.toJson(response));
    }
}
