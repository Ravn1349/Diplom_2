package praktikum.models;

public class User {
    private String email;
    private String password;

    private String name;

    public User() {
    }
    public User withEmail(String email) {
        this.email = email;
        return this;
    }

    public User withPassword(String password) {
        this.password = password;
        return this;
    }

    public User withName(String name) {
        this.name = name;
        return this;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof User)) {
            return false;
        }

        User that = (User) other;

        return this.email.equals(that.email)
                && this.password.equals(that.password)
                && this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int hashCode = 1;

        hashCode = hashCode * 37 + this.email.hashCode();
        hashCode = hashCode * 37 + this.password.hashCode();
        hashCode = hashCode * 37 + this.name.hashCode();

        return hashCode;
    }
}
