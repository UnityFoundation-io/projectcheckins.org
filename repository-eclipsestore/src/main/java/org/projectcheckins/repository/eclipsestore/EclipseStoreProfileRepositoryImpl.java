package org.projectcheckins.repository.eclipsestore;

import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.ProfileRepository;

@Singleton
class EclipseStoreProfileRepositoryImpl implements ProfileRepository  {
    @Override
    public Format findPreferedFormat(Authentication authentication) {
        return Format.MARKDOWN;
    }
}
