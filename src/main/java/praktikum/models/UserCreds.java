package praktikum.models;

public class UserCreds {
    private String email;
    private String password;

    public UserCreds() {
    }

    public UserCreds credsFrom(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        return this;
    }

    public UserCreds emailFrom(User user) {
        this.email = user.getEmail();
        return this;
    }

    public UserCreds passwordFrom(User user) {
        this.password = user.getPassword();
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
