package praktikum.User;
import net.datafaker.Faker;

import praktikum.models.User;

public class UserGenerator {
    public static User randomUser() {
        Faker faker = new Faker();
        return new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.internet().password())
                .withName(faker.name().firstName());
    }

    public static User randomUserWithoutEmail() {
        Faker faker = new Faker();
        return new User()
                .withPassword(faker.internet().password())
                .withName(faker.name().firstName());
    }

    public static User randomUserWithoutPassword() {
        Faker faker = new Faker();
        return new User()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().firstName());
    }

    public static User randomUserWithoutName() {
        Faker faker = new Faker();
        return new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.internet().password());
    }
}
