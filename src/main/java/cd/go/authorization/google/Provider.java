package cd.go.authorization.google;

import cd.go.authorization.google.models.PluginConfiguration;
import cd.go.authorization.google.models.TokenInfo;
import cd.go.authorization.google.models.User;
import org.brickred.socialauth.Permission;
import org.brickred.socialauth.util.AccessGrant;

import java.util.Map;

public interface Provider<T extends PluginConfiguration> {
    String getProviderName();

    Permission permission();

    String authorizationServerUrl(String callbackUrl) throws Exception;

    TokenInfo accessToken(Map<String, String> params) throws Exception;

    User userProfile(AccessGrant accessGrant) throws Exception;

    T pluginConfiguration();

    void verifyConnection() throws Exception;
}
