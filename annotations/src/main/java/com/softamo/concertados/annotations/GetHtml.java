package com.softamo.concertados.annotations;

import io.micronaut.http.annotation.UriMapping;
import io.micronaut.scheduling.TaskExecutors;
import java.lang.annotation.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface GetHtml {
    String uri() default UriMapping.DEFAULT_URI;

    String[] rolesAllowed();

    String view() default "";

    boolean hidden() default true;

    String executesOn() default TaskExecutors.BLOCKING;
}
