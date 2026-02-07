package kz.nursayat.utils;

import kz.nursayat.exception.InvalidInputException;

public class ValidationUtils {

    // Приватный конструктор, так как это утилитный класс (Singleton Pattern не нужен, это статика)
    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Проверяет, что строка не пустая и не null.
     */
    public static void checkString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be empty");
        }
    }

    /**
     * Проверяет, что число находится в заданном диапазоне.
     */
    public static void checkRange(double value, double min, double max, String fieldName) {
        if (value < min || value > max) {
            throw new InvalidInputException(fieldName + " must be between " + min + " and " + max);
        }
    }

    /**
     * Проверяет корректность email (простейшая проверка на собачку).
     */
    public static void checkEmail(String email) {
        checkString(email, "Email");
        if (!email.contains("@") || !email.contains(".")) {
            throw new InvalidInputException("Invalid email format: " + email);
        }
    }
}