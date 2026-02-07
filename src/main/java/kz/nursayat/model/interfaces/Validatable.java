package kz.nursayat.model.interfaces;

import kz.nursayat.exception.InvalidInputException;

public interface Validatable<T> {
    void validate();

    default void logValidationStatus() {
        System.out.println("Status: Validation started for " + this.getClass().getSimpleName());
    }

    static void checkStringNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidInputException(fieldName + " cannot be empty");
        }
    }
}