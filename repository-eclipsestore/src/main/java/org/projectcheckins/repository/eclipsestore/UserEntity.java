package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Introspected
public class UserEntity {
    private @NotBlank String id;

    @NotBlank
    private String email;

    boolean enabled;
    @NotBlank
    private String encodedPassword;
    @NonNull List<String> authorities = new ArrayList<>();

    public UserEntity(String id, String email, String encodedPassword, List<String> authorities) {
        this.id = id;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.authorities = authorities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}
