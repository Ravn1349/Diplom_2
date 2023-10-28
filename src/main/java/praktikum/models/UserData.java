package praktikum.models;

public class UserData {
    private String email;
    private String name;

    public UserData() {
    }

    public UserData dataFrom(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof UserData)) {
            return false;
        }

        UserData that = (UserData) other;

        return this.email.equals(that.email)
                && this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int hashCode = 1;

        hashCode = hashCode * 37 + this.email.hashCode();
        hashCode = hashCode * 37 + this.name.hashCode();

        return hashCode;
    }
}
