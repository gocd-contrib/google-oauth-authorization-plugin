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

package cd.go.authorization.google.annotation;


import java.lang.reflect.Field;
import java.util.*;

public class MetadataHelper {

    public static List<ProfileMetadata> getMetadata(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<ProfileMetadata> metadata = new ArrayList<>();
        for (Field field : fields) {
            ProfileField profileField = field.getAnnotation(ProfileField.class);
            if (profileField != null) {
                final FieldMetadata fieldMetadata = new FieldMetadata(profileField.required(), profileField.secure(), profileField.type());
                final ProfileMetadata<FieldMetadata> profileMetadata = new ProfileMetadata<>(profileField.key(), fieldMetadata);
                metadata.add(profileMetadata);
            }
        }
        return metadata;
    }
}
