package praktikum.models;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class UserData {
    private String email;
    private String name;

    public UserData dataFrom(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        return this;
    }
}
