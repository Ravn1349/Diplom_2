package praktikum;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import praktikum.Order.OrderClient;
import praktikum.User.UserClient;
import praktikum.models.User;
import praktikum.models.UserCreds;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static praktikum.User.UserGenerator.*;
import static praktikum.User.UserGenerator.randomUserWithoutName;

public class BaseTest {

    static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    static UserClient userClient = new UserClient();
    static OrderClient orderClient = new OrderClient();

    static User randomUser = randomUser();
    static String userAccessToken;

    @Step("create user")
    static Response createUser(User user) {
        Response CreationResponse = userClient.createUser(user);
        assertEquals("Неверный статус код создания курьера", 200, CreationResponse.statusCode()); // пользователя можно создать передав в ручку все обязательные поля, запрос возвращает правильный код ответа
        assertEquals("Неверное тело ответа", true, CreationResponse.path("success")); // успешный запрос возвращает success: true;
        return CreationResponse;
    }

    @Step("delete user")
    static void deleteUser(String userAccessToken) {
        Response courierDeletionResponse = userClient.deleteUser(userAccessToken);
        assertEquals("Не удалось удалить пользователя", 202, courierDeletionResponse.statusCode()); // удаление пользователя
    }

    @Step("create random user Without Email")
    static Response createUserWithoutEmail() {
        User userWithoutEmail = randomUserWithoutEmail();
        return userClient.createUser(userWithoutEmail);
    }

    @Step("create random user Without Password")
    static Response createUserWithoutPassword() {
        User userWithoutPassword = randomUserWithoutPassword();
        return userClient.createUser(userWithoutPassword);
    }

    @Step("create random user Without Name")
    static Response createUserWithoutName() {
        User userWithoutName = randomUserWithoutName();
        return userClient.createUser(userWithoutName);
    }

    @Step("login User And Get User Access Token")
    static String loginUserAndGetUserAccessToken(User user) {
        UserCreds userCreds = new UserCreds();
        Response loginResponse = userClient.loginUser(userCreds.credsFrom(user));
        assertEquals("Пользователь не залогинен", 200, loginResponse.statusCode());
        assertNotNull("Неверное тело ответа", loginResponse.path("accessToken"));
        return loginResponse.path("accessToken").toString().substring(7);
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @After
    public void tearDown() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken); // удаление пользователя
        }
        userAccessToken = null;
    }
}
