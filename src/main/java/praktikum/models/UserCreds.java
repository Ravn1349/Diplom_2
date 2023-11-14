package praktikum.models;

import lombok.Setter;

@Setter
public class UserCreds {
    private String email;
    private String password;

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
}
