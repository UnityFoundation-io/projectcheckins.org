// Copyright 2024 Object Computing, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.projectcheckins.core.api;

import io.micronaut.core.annotation.NonNull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.FullNameUtils;
import org.projectcheckins.core.forms.TimeFormat;
import org.projectcheckins.security.api.PublicProfile;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.TimeZone;

public interface Profile extends PublicProfile {

    @NotNull TimeZone timeZone();

    @NotNull DayOfWeek firstDayOfWeek();

    @NotNull LocalTime beginningOfDay();

    @NotNull LocalTime endOfDay();

    @NotNull TimeFormat timeFormat();

    @NotNull Format format();

    @Nullable
    String firstName();

    @Nullable
    String lastName();

    @NonNull
    default String fullName() {
        return FullNameUtils.getFullName(firstName(), lastName());
    }
}
