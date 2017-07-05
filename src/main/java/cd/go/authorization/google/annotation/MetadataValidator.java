package cd.go.authorization.google.annotation;

import cd.go.authorization.google.models.GoogleConfiguration;

import java.util.*;

public class MetadataValidator {

    public List<Map<String, String>> validate(GoogleConfiguration configuration) {

        Map<String, String> properties = configuration.toProperties();

        List<Map<String, String>> result = new ArrayList<>();
        List<String> knownFields = new ArrayList<>();

        for (ProfileMetadata field : MetadataHelper.getMetadata(configuration.getClass())) {
            knownFields.add(field.getKey());

            Map<String, String> validationError = field.validate(properties.get(field.getKey()));

            if (!validationError.isEmpty()) {
                result.add(validationError);
            }
        }


        Set<String> set = new HashSet<>(properties.keySet());
        set.removeAll(knownFields);

        if (!set.isEmpty()) {
            for (String key : set) {
                LinkedHashMap<String, String> validationError = new LinkedHashMap<>();
                validationError.put("key", key);
                validationError.put("message", "Is an unknown property");
                result.add(validationError);
            }
        }
        return result;
    }
}
