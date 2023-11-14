package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UserCreationTests extends BaseTest {

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
        assertFalse("Неверное тело ответа", repetitiveLoginResponse.path("success"));
        assertEquals("Неверное тело ответа", "User already exists", repetitiveLoginResponse.path("message"));
    }

    @Test
    @DisplayName("create user With Any Field Absent Returns Error")
    @Description("запрос на создание пользователя возвращает ошибку если одного из полей нет")
    public void createUserWithAnyFieldAbsentReturnsError() {
        Response noLoginCreationResponse = createUserWithoutEmail();
        assertEquals("Неверный статус код", 403, noLoginCreationResponse.statusCode());
        assertFalse("Неверное тело ответа", noLoginCreationResponse.path("success"));
        assertEquals("Неверное тело ответа", "Email, password and name are required fields", noLoginCreationResponse.path("message"));

        Response noPasswordCreationResponse = createUserWithoutPassword();
        assertEquals("Неверный статус код", 403, noPasswordCreationResponse.statusCode());
        assertFalse("Неверное тело ответа", noPasswordCreationResponse.path("success"));
        assertEquals("Неверное тело ответа", "Email, password and name are required fields", noLoginCreationResponse.path("message"));

        Response noNameCreationResponse = createUserWithoutName();
        assertEquals("Неверный статус код", 403, noNameCreationResponse.statusCode());
        assertFalse("Неверное тело ответа", noNameCreationResponse.path("success"));
        assertEquals("Неверное тело ответа", "Email, password and name are required fields", noLoginCreationResponse.path("message"));
    }
}
