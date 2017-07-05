package cd.go.authorization.google.models;

import cd.go.authorization.google.Provider;

import java.util.Properties;

public interface PluginConfiguration {

    Properties configuration();

    String clientId();

    String clientSecret();

    String toJSON();

    Provider provider() throws Exception;
}
