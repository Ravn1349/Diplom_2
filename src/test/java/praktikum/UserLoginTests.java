package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.User.UserClient;
import praktikum.models.User;
import praktikum.models.UserCreds;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static praktikum.User.UserGenerator.randomUser;
import static praktikum.UserCreationTests.createUser;
import static praktikum.UserCreationTests.deleteUser;

public class UserLoginTests {

    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static UserClient userClient = new UserClient();
    private static User randomUser = randomUser();
    private static String userAccessToken;

    @Step("login User And Get User Access Token")
    static String loginUserAndGetUserAccessToken(User user) {
        UserCreds userCreds = new UserCreds();
        Response loginResponse = userClient.loginUser(userCreds.credsFrom(user));
        assertEquals("Пользователь не залогинен", 200, loginResponse.statusCode());
        assertNotNull("Неверное тело ответа", loginResponse.path("accessToken"));
        userAccessToken = loginResponse.path("accessToken").toString().substring(7);
        return userAccessToken;
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

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
        assertEquals(false, loginResponseWithWrongEmail.path("success"));
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
        assertEquals(false, loginResponseWithWrongPassword.path("success"));
        assertEquals("email or password are incorrect", loginResponseWithWrongPassword.path("message"));

        userAccessToken = loginUserAndGetUserAccessToken(randomUser);
    }


    @After
    public void tearDown() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken); // удаление курьера
        }
    }
}
