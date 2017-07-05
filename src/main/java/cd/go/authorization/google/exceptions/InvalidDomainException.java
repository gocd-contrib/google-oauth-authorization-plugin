package cd.go.authorization.google.exceptions;

import static java.text.MessageFormat.format;

public class InvalidDomainException extends RuntimeException {
    public InvalidDomainException(String message) {
        super(message);
    }

    public static InvalidDomainException invalidDomain(String domainName, String supportedDomains) {
        return new InvalidDomainException(format("Domain `{0}` is invalid. Supported domains are `{1}`", domainName, supportedDomains));
    }
}
