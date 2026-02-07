package kz.nursayat.model;

import kz.nursayat.model.interfaces.Validatable;
import java.time.LocalDate;

public class Freelancer extends BaseUser implements Validatable<Freelancer> {
    private double rating;
    private LocalDate joinedAt;
    private String phone;

    // Конструктор по умолчанию (для Spring и Builder)
    public Freelancer() {
        super(0, "", "", ""); // Строго 4 аргумента согласно BaseUser.java
        this.joinedAt = LocalDate.now();
    }

    // Конструктор со всеми полями (7 аргументов: 4 в родитель, 3 здесь)
    public Freelancer(int id, String firstName, String lastName, String email,
                      double rating, LocalDate joinedAt, String phone) {
        super(id, firstName, lastName, email); // Вызов BaseUser(int, String, String, String)
        this.rating = rating;
        this.joinedAt = joinedAt;
        this.phone = phone;
    }

    @Override
    public String getRole() {
        return "Freelancer";
    }

    @Override
    public String getFullDescription() {
        return super.getFullDescription() + " | Rating: " + rating + " | Phone: " + phone;
    }

    @Override
    public void validate() {
        // Используем наш новый пакет utils
        kz.nursayat.utils.ValidationUtils.checkString(getFirstName(), "First name");
        kz.nursayat.utils.ValidationUtils.checkString(getLastName(), "Last name");
        kz.nursayat.utils.ValidationUtils.checkEmail(getEmail());

        // Используем проверку диапазона для рейтинга
        kz.nursayat.utils.ValidationUtils.checkRange(rating, 0.0, 5.0, "Rating");
    }

    // Геттеры и сеттеры для специфичных полей
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public LocalDate getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDate joinedAt) { this.joinedAt = joinedAt; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // --- ПАТТЕРН BUILDER ---
    public static class Builder {
        private final Freelancer freelancer = new Freelancer();

        public Builder id(int id) {
            freelancer.setId(id);
            return this;
        }

        public Builder firstName(String firstName) {
            freelancer.setFirstName(firstName);
            return this;
        }

        public Builder lastName(String lastName) {
            freelancer.setLastName(lastName);
            return this;
        }

        public Builder email(String email) {
            freelancer.setEmail(email);
            return this;
        }

        public Builder rating(double rating) {
            freelancer.setRating(rating);
            return this;
        }

        public Builder joinedAt(LocalDate joinedAt) {
            freelancer.setJoinedAt(joinedAt);
            return this;
        }

        public Builder phone(String phone) {
            freelancer.setPhone(phone);
            return this;
        }

        public Freelancer build() {
            return freelancer;
        }
    }
}