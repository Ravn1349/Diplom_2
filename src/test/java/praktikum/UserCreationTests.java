package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.User.UserClient;
import praktikum.models.User;

import static org.junit.Assert.assertEquals;
import static praktikum.User.UserGenerator.*;
import static praktikum.UserLoginTests.loginUserAndGetUserAccessToken;

public class UserCreationTests {

    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static UserClient userClient = new UserClient();
    private static User randomUser = randomUser();
    private static String userAccessToken;


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
    private static Response createUserWithoutEmail() {
        User userWithoutLogin = randomUserWithoutEmail();
        return userClient.createUser(userWithoutLogin);
    }

    @Step("create random user Without Password")
    private static Response createUserWithoutPassword() {
        User userWithoutPassword = randomUserWithoutPassword();
        return userClient.createUser(userWithoutPassword);
    }

    @Step("create random user Without Name")
    private static Response createUserWithoutName() {
        User userWithoutName = randomUserWithoutName();
        return userClient.createUser(userWithoutName);
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("create user With All Fields Present Returns success: true")
    @Description("запрос на создание уникального пользователя со всеми обязательными полями возвращает success: true")
    public void createUserWithAllFieldsPresentReturnsSuccessTrue() {
        createUser(randomUser);

        userAccessToken = loginUserAndGetUserAccessToken(randomUser);
    }

    @Test
    @DisplayName("create user's Duplicate Returns Error")
    @Description("если создать пользователя, который уже зарегистрирован, возвращается ошибка. Создать двух одинаковых пользователей нельзя")
    public void createUserDuplicateReturnsError() {
        createUser(randomUser);

        userAccessToken = loginUserAndGetUserAccessToken(randomUser);

        Response repetitiveLoginResponse = userClient.createUser(randomUser);
        assertEquals("Неверный статус код", 403, repetitiveLoginResponse.statusCode());
        assertEquals("Неверное тело ответа", false, repetitiveLoginResponse.path("success"));
        assertEquals("Неверное тело ответа", "User already exists", repetitiveLoginResponse.path("message"));
    }

    @Test
    @DisplayName("create user With Any Field Absent Returns Error")
    @Description("запрос на создание пользователя возвращает ошибку если одного из полей нет")
    public void createUserWithAnyFieldAbsentReturnsError() {
        Response noLoginCreationResponse = createUserWithoutEmail();
        assertEquals("Неверный статус код", 403, noLoginCreationResponse.statusCode());
        assertEquals("Неверное тело ответа", false, noLoginCreationResponse.path("success"));
        assertEquals("Неверное тело ответа", "Email, password and name are required fields", noLoginCreationResponse.path("message"));

        Response noPasswordCreationResponse = createUserWithoutPassword();
        assertEquals("Неверный статус код", 403, noPasswordCreationResponse.statusCode());
        assertEquals("Неверное тело ответа", false, noLoginCreationResponse.path("success"));
        assertEquals("Неверное тело ответа", "Email, password and name are required fields", noLoginCreationResponse.path("message"));

        Response noNameCreationResponse = createUserWithoutName();
        assertEquals("Неверный статус код", 403, noNameCreationResponse.statusCode());
        assertEquals("Неверное тело ответа", false, noLoginCreationResponse.path("success"));
        assertEquals("Неверное тело ответа", "Email, password and name are required fields", noLoginCreationResponse.path("message"));
    }


    @After
    public void tearDown() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken); // удаление курьера
        }
    }
}
