package org.projectcheckins.security;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;

@ConfigurationProperties(EmailInvitationConfigProperties.PREFIX)
public class EmailInvitationConfigProperties implements EmailInvitationConfig {
    public static final String PREFIX = "email.invitation";
    public static final String DEFAULT_URL = "";
    private String url = DEFAULT_URL;

    @Override
    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
