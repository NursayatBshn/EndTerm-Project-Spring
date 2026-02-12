package kz.nursayat.patterns;

import kz.nursayat.model.BaseUser;
import kz.nursayat.model.Client;
import kz.nursayat.model.Freelancer;

public class UserFactory {

    public static BaseUser createUser(String type) {
        if (type == null || type.isBlank()) {
            return null;
        }

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