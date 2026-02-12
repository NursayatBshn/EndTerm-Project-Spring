package kz.nursayat.model;

import kz.nursayat.model.interfaces.Validatable;
import java.time.LocalDate;

public class Client extends BaseUser implements Validatable<Client> {
    private LocalDate registeredAt;

    public Client() {
        super(0, "", "", "");
        this.registeredAt = LocalDate.now();
    }

    public Client(int id, String firstName, String lastName, String email, LocalDate registeredAt) {
        super(id, firstName, lastName, email);
        this.registeredAt = registeredAt;
    }

    @Override
    public String getRole() {
        return "Client";
    }

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