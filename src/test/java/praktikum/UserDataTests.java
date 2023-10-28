package praktikum;

import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.User.UserClient;
import praktikum.models.User;
import praktikum.models.UserCreds;
import praktikum.models.UserData;

import java.util.Map;

import static org.junit.Assert.*;
import static praktikum.User.UserGenerator.randomUser;
import static praktikum.User.UserGenerator.randomUserWithoutPassword;
import static praktikum.UserCreationTests.createUser;
import static praktikum.UserCreationTests.deleteUser;
import static praktikum.UserLoginTests.loginUserAndGetUserAccessToken;

public class UserDataTests {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static UserClient userClient = new UserClient();
    private static User randomUser = randomUser();
    private static String userAccessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("update User Data With Authorization Returns Success True")
    @Description("запрос на изменение данных пользователя с авторизацией возвращает Success True")
    public void updateUserDataWithAuthorizationReturnsSuccessTrue() {
        createUser(randomUser);

        userAccessToken = loginUserAndGetUserAccessToken(randomUser);
        User newUser = randomUserWithoutPassword();
        UserData newUserData = new UserData().dataFrom(newUser);
        Response updateUserDataResponse = userClient.updateUserData(userAccessToken, newUserData);
        assertEquals("Неверный статус код обновления данных о пользователе", 200, updateUserDataResponse.statusCode());
        assertEquals(true, updateUserDataResponse.path("success"));
        Map< String , String > responseUserData = updateUserDataResponse.path("user");
        assertEquals(newUserData, new Gson().fromJson(responseUserData.toString(), UserData.class));

        userAccessToken = loginUserAndGetUserAccessToken(newUser.withPassword(randomUser.getPassword()));
    }

    @Test
    @DisplayName("update User Data Without Authorization Returns Error")
    @Description("запрос на изменение данных пользователя без авторизации возвращает ошибку")
    public void updateUserDataWithoutAuthorizationReturnsError() {
        createUser(randomUser);

        userAccessToken = loginUserAndGetUserAccessToken(randomUser);
        User newUser = randomUserWithoutPassword();
        UserData newUserData = new UserData().dataFrom(newUser);
        Response updateUserDataResponse = userClient.updateUserDataWithoutAuthorization(newUserData);
        assertEquals("Неверный статус код обновления данных о пользователе", 401, updateUserDataResponse.statusCode());
        assertEquals(false, updateUserDataResponse.path("success"));
        assertEquals("You should be authorised", updateUserDataResponse.path("message"));

        UserCreds userCreds = new UserCreds();
        Response newUserDataLoginresponse = userClient.loginUser(userCreds.credsFrom(newUser.withPassword(randomUser.getPassword())));
        assertEquals("Неверный статус код обновления данных о пользователе", 401, newUserDataLoginresponse.statusCode());
    }

    @After
    public void tearDown() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken); // удаление пользователя
        }
    }
}