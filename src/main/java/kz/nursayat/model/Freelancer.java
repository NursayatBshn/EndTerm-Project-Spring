package kz.nursayat.model;

import kz.nursayat.model.interfaces.Validatable;
import java.time.LocalDate;

public class Freelancer extends BaseUser implements Validatable<Freelancer> {
    private double rating;
    private LocalDate joinedAt;
    private String phone;

    public Freelancer() {
        super(0, "", "", "");
        this.joinedAt = LocalDate.now();
    }

    public Freelancer(int id, String firstName, String lastName, String email,
                      double rating, LocalDate joinedAt, String phone) {
        super(id, firstName, lastName, email);
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
        kz.nursayat.utils.ValidationUtils.checkString(getFirstName(), "First name");
        kz.nursayat.utils.ValidationUtils.checkString(getLastName(), "Last name");
        kz.nursayat.utils.ValidationUtils.checkEmail(getEmail());

        kz.nursayat.utils.ValidationUtils.checkRange(rating, 0.0, 5.0, "Rating");
    }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public LocalDate getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDate joinedAt) { this.joinedAt = joinedAt; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

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