package kz.nursayat.model;

import kz.nursayat.model.interfaces.Validatable;
import java.time.LocalDate;

public class Client extends BaseUser implements Validatable<Client> {
    // Поле registeredAt перенесено сюда, так как его нет в BaseUser
    private LocalDate registeredAt;

    // Конструктор по умолчанию для Builder и Spring
    public Client() {
        super(0, "", "", "");
        this.registeredAt = LocalDate.now();
    }

    // Исправленный конструктор: 4 аргумента в super() и 1 для текущего класса
    public Client(int id, String firstName, String lastName, String email, LocalDate registeredAt) {
        super(id, firstName, lastName, email); // Строго 4 аргумента согласно BaseUser.java
        this.registeredAt = registeredAt;
    }

    // Реализация обязательного метода из BaseUser
    @Override
    public String getRole() {
        return "Client";
    }

    // Геттер и сеттер для registeredAt (теперь они существуют в классе)
    public LocalDate getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDate registeredAt) {
        this.registeredAt = registeredAt;
    }

    @Override
    public void validate() {
        kz.nursayat.utils.ValidationUtils.checkString(getFirstName(), "First name");
        kz.nursayat.utils.ValidationUtils.checkString(getLastName(), "Last name");
        kz.nursayat.utils.ValidationUtils.checkEmail(getEmail());
    }

    // --- ПАТТЕРН BUILDER ---
    public static class Builder {
        private final Client client = new Client();

        public Builder id(int id) {
            client.setId(id);
            return this;
        }

        public Builder firstName(String firstName) {
            client.setFirstName(firstName);
            return this;
        }

        public Builder lastName(String lastName) {
            client.setLastName(lastName);
            return this;
        }

        public Builder email(String email) {
            client.setEmail(email);
            return this;
        }

        public Builder registeredAt(LocalDate registeredAt) {
            client.setRegisteredAt(registeredAt);
            return this;
        }

        public Client build() {
            return client;
        }
    }
}