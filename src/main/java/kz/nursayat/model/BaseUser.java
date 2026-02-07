package kz.nursayat.model;

public abstract class BaseUser extends BaseEntity {
    private String firstName;
    private String lastName;
    private String email;

    protected BaseUser(int id, String firstName, String lastName, String email) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public abstract String getRole();

    @Override
    public String getEntityName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getFullDescription() {
        return getRole() + ": " + getEntityName() + " (" + email + ")";
    }

    public String getFirstName() {
            return firstName;
        }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}