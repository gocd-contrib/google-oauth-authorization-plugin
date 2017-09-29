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
import cd.go.authorization.google.models.GoogleConfiguration;
import cd.go.authorization.google.requests.VerifyConnectionRequest;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cd.go.authorization.google.GooglePlugin.LOG;

public class VerifyConnectionRequestExecutor implements RequestExecutor {
    private static final Gson GSON = new Gson();
    private final VerifyConnectionRequest request;

    public VerifyConnectionRequestExecutor(VerifyConnectionRequest request) {
        this.request = request;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        List<Map<String, String>> errors = validate();
        if (errors.size() != 0) {
            return validationFailureResponse(errors);
        }

        ValidationResult validationResult = verifyConnection(request.googleConfiguration());
        if (!validationResult.isSuccessful()) {
            return verifyConnectionFailureResponse(validationResult);
        }

        return successResponse();
    }

    private List<Map<String, String>> validate() {
        return new MetadataValidator().validate(request.googleConfiguration());
    }

    private ValidationResult verifyConnection(GoogleConfiguration configuration) {
        final ValidationResult result = new ValidationResult();

        try {
            configuration.googleApiClient().verifyConnection();
        } catch (Exception e) {
            result.addError(new ValidationError(e.getMessage()));
            LOG.error("[Verify Connection] Verify connection failed with errors.", e);
        }
        return result;
    }

    private GoPluginApiResponse successResponse() {
        return responseWith("success", "Connection ok", null);
    }

    private GoPluginApiResponse verifyConnectionFailureResponse(ValidationResult validationResult) {
        return responseWith("failure", validationResult.getErrors().get(0).getMessage(), null);
    }

    private GoPluginApiResponse validationFailureResponse(List<Map<String, String>> errors) {
        return responseWith("validation-failed", "Validation failed for the given Auth Config", errors);
    }

    private GoPluginApiResponse responseWith(String status, String message, List<Map<String, String>> errors) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);

        if (errors != null && errors.size() > 0) {
            response.put("errors", errors);
        }

        return DefaultGoPluginApiResponse.success(GSON.toJson(response));
    }
}
