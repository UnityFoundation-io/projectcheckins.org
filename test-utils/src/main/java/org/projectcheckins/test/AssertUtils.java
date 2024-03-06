package org.projectcheckins.test;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.validation.Validator;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.api.ThrowableAssert;
import jakarta.validation.ConstraintViolation;

import java.util.Optional;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

public final class AssertUtils {
    private AssertUtils() {
    }

    public static <T> void assertValid(@NonNull Validator validator, T object) {
        assertThat(validator.validate(object))
                .isEmpty();
    }

    public static <T> void assertNotBlank(@NonNull Validator validator, T object, String propertyName) {
        assertThat(validator.validate(object))
                .anyMatch(x -> x.getPropertyPath().toString().equals(propertyName))
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("must not be blank");
    }

    public static <T> void assertNotNull(@NonNull Validator validator, T object, String propertyName) {
        assertThat(validator.validate(object))
                .anyMatch(x -> x.getPropertyPath().toString().equals(propertyName))
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("must not be null");
    }

    public static <T> void assertPastOrPresent(@NonNull Validator validator, T object, String propertyName) {
        assertThat(validator.validate(object))
                .anyMatch(x -> x.getPropertyPath().toString().equals(propertyName))
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("must be a date in the past or in the present");
    }

    public static AbstractObjectAssert<?, String> assertThrowsWithHtml(ThrowableAssert.ThrowingCallable shouldRaiseThrowable, HttpStatus httpStatus) {
        return assertThatThrownBy(shouldRaiseThrowable)
                .isInstanceOf(HttpClientResponseException.class)
                .hasFieldOrPropertyWithValue("status", httpStatus)
                .extracting(e -> ((HttpClientResponseException) e).getResponse())
                .extracting(r -> r.getBody(String.class))
                .extracting(Optional::get);
    }

    public static Condition<String> containsOnlyOnce(String needle) {
        return containsManyTimes(1, needle);
    }

    public static Condition<String> containsManyTimes(int expectedTimes, String needle) {
        return new Condition<>(haystack -> haystack.split(needle, -1).length - 1 == expectedTimes,
                "Contains [%s] exactly %s times", needle, expectedTimes);
    }

    public static Predicate<HttpResponse<?>> status(HttpStatus httpStatus) {
        return response -> httpStatus.equals(response.getStatus());
    }

    public static Predicate<HttpResponse<?>> htmlBody(Predicate<String> expected) {
        return response -> response.getBody(String.class)
            .filter(expected)
            .isPresent();
    }

    public static Predicate<HttpResponse<?>> htmlBody(String expected) {
        return htmlBody(html -> html.contains("<!DOCTYPE html>"));
    }

    public static Predicate<HttpResponse<?>> htmlBody() {
        return htmlBody("<!DOCTYPE html>");
    }

    public static Predicate<HttpResponse<?>> htmlPage(HttpStatus httpStatus) {
        return status(httpStatus).and(htmlBody());
    }

    public static Predicate<HttpResponse<?>> htmlPage() {
        return htmlPage(HttpStatus.OK);
    }

    public static Predicate<HttpResponse<?>> location(Predicate<String> expected) {
        return response -> response.getHeaders().getFirst(HttpHeaders.LOCATION).filter(expected).isPresent();
    }

    public static Predicate<HttpResponse<?>> location(String location) {
        return location(location::equals);
    }

    public static Predicate<HttpResponse<?>> redirection(Predicate<String> expected) {
        return status(HttpStatus.SEE_OTHER).and(location(expected));
    }

    public static Predicate<HttpResponse<?>> redirection(String expected) {
        return status(HttpStatus.SEE_OTHER).and(location(expected));
    }

    public static Predicate<HttpResponse<?>> unauthorized() {
        return redirection("/unauthorized");
    }
}

