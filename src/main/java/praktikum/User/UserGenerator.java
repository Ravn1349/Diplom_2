package praktikum.User;
import net.datafaker.Faker;

import praktikum.models.User;

public class UserGenerator {

    public static User randomUser() {
        Faker faker = new Faker();
        return User.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .name(faker.name().firstName())
                .build();
    }

    public static User randomUserWithoutEmail() {
        Faker faker = new Faker();
        return User.builder()
                .password(faker.internet().password())
                .name(faker.name().firstName())
                .build();
    }

    public static User randomUserWithoutPassword() {
        Faker faker = new Faker();
        return User.builder()
                .email(faker.internet().emailAddress())
                .name(faker.name().firstName())
                .build();
    }

    public static User randomUserWithoutName() {
        Faker faker = new Faker();
        return User.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();
    }
}
