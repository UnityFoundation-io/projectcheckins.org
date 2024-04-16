package org.projectcheckins.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.constraints.Unique;

import java.util.List;

public interface TeamInvitationRepository {

    List<? extends TeamInvitation> findAll();

    void save(@Unique @NotBlank @Email String email);

    void deleteByEmail(@NotBlank @Email String email);

    boolean existsByEmail(@NotBlank @Email String email);
}
