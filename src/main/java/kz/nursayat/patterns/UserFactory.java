package kz.nursayat.patterns;

import kz.nursayat.model.BaseUser;
import kz.nursayat.model.Client;
import kz.nursayat.model.Freelancer;

/**
 * Фабрика для создания подклассов BaseUser.
 * Реализует требование раздела 1.B: "The factory must return the base type".
 */
public class UserFactory {

    /**
     * Создает объект пользователя на основе типа.
     * @param type Тип пользователя ("CLIENT" или "FREELANCER")
     * @return Объект, наследуемый от BaseUser
     * @throws IllegalArgumentException если передан неизвестный тип
     */
    public static BaseUser createUser(String type) {
        if (type == null || type.isBlank()) {
            return null;
        }

        // Используем switch для поддержки легкого расширения (easy extension) [cite: 3]
        switch (type.toUpperCase()) {
            case "CLIENT":
                return new Client();
            case "FREELANCER":
                return new Freelancer();
            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }
}