package org.projectcheckins.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface TeamInvitationRepository {

    List<? extends TeamInvitation> findAll();

    void save(@NotBlank @Email String email);

    void accept(@NotBlank @Email String email);
}
