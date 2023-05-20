package dev.sash.hsel.mad.easydo.utils;

import android.util.Patterns;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class ValidatorUtils {

    public static final Validation<String> STRING_NOT_NULL = GenericValidation.from(Objects::nonNull);
    public static final Validation<String> STRING_NOT_EMPTY = GenericValidation.from(text -> !text.isEmpty());
    public static final Validation<String> STRING_IS_EMAIL = GenericValidation.from(email -> Patterns.EMAIL_ADDRESS.matcher(email).matches());

    public static final Validation<String> STING_LENGTH_EQUAL(int length) {
        return GenericValidation.from(text -> ((String) text).length() == length);
    }

    @FunctionalInterface
    public interface Validation<K> {
        Result test(K param);

        default Validation<K> and(Validation<K> other) {
            return (param) -> {
                Result result = this.test(param);
                return !result.isValid() ? result : other.test(param);
            };
        }

        default Validation<K> or(Validation<K> other) {
            return (param) -> {
                Result result = this.test(param);
                return result.isValid() ? result : other.test(param);
            };
        }
    }

    public static class GenericValidation<K> implements Validation<K> {

        private Predicate<K> predicate;

        private GenericValidation(Predicate<K> predicate) {
            this.predicate = predicate;
        }

        public static <K> GenericValidation<K> from(Predicate<K> predicate) {
            return new GenericValidation<>(predicate);
        }

        @Override public Result test(K param) {
            return predicate.test(param) ? Result.ok() : Result.fail();
        }

    }

    public static class Result {

        private boolean valid;

        private Result(boolean valid) {
            this.valid = valid;
        }

        public static Result ok() {
            return new Result(true);
        }

        public static Result fail() {
            return new Result(false);
        }

        public boolean isValid() {
            return valid;
        }
        public boolean isInvalid() {
            return !valid;
        }

        public Optional<String> getMessageIfInvalid(String message) {
            return this.valid ? Optional.empty() : Optional.of(message);
        }
    }

}
