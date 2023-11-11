package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.junit.Test;
import praktikum.models.UserCreds;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UserLoginTests extends BaseTest {

    @Test
    @DisplayName("login Existent User With All Required Fields Returns User Access Token")
    @Description("успешный запрос на логин под существующим пользователем возвращает Access Token")
    public void loginExistentUserWithAllRequiredFieldsReturnsUserAccessToken() {
        createUser(randomUser);

        userAccessToken = loginUserAndGetUserAccessToken(randomUser);
    }

    @Test
    @DisplayName("login User With Wrong Email Returns Error")
    @Description("система вернёт ошибку, если при авторизации неправильно указать логин; если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void loginUserWithWrongEmailReturnsError() {
        createUser(randomUser);

        UserCreds userCredsWithWrongEmail = new UserCreds();
        userCredsWithWrongEmail.passwordFrom(randomUser);
        userCredsWithWrongEmail.setEmail(new Faker().internet().emailAddress());
        Response loginResponseWithWrongEmail = userClient.loginUser(userCredsWithWrongEmail);
        assertEquals("Неверный статус код", 401, loginResponseWithWrongEmail.statusCode());
        assertFalse(loginResponseWithWrongEmail.path("success"));
        assertEquals("email or password are incorrect", loginResponseWithWrongEmail.path("message"));

        userAccessToken = loginUserAndGetUserAccessToken(randomUser);
    }

    @Test
    @DisplayName("login User With Wrong Password Returns Error")
    @Description("система вернёт ошибку, если при авторизации неправильно указать пароль")
    public void loginUserWithWrongPasswordReturnsError() {
        createUser(randomUser);

        UserCreds userCredsWithWrongPassword = new UserCreds();
        userCredsWithWrongPassword.emailFrom(randomUser);
        userCredsWithWrongPassword.setPassword(new Faker().internet().password());
        Response loginResponseWithWrongPassword = userClient.loginUser(userCredsWithWrongPassword);
        assertEquals("Неверный статус код", 401, loginResponseWithWrongPassword.statusCode());
        assertFalse(loginResponseWithWrongPassword.path("success"));
        assertEquals("email or password are incorrect", loginResponseWithWrongPassword.path("message"));

        userAccessToken = loginUserAndGetUserAccessToken(randomUser);
    }
}
