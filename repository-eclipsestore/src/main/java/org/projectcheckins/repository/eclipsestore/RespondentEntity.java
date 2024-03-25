package org.projectcheckins.repository.eclipsestore;

import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.core.api.Respondent;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class RespondentEntity implements Respondent {

    @NotBlank
    private String profileId;

    @Override
    public String profileId() {
        return profileId;
    }

    public void profileId(String profileId) {
        this.profileId = profileId;
    }

    public static RespondentEntity toEntity(Respondent respondent) {
        final RespondentEntity entity = new RespondentEntity();
        entity.profileId(respondent.profileId());
        return entity;
    }

    public static Set<RespondentEntity> toEntity(Set<? extends Respondent> respondents) {
        return respondents.stream().map(RespondentEntity::toEntity).collect(toSet());
    }
}
