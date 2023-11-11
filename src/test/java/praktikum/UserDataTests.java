package praktikum;

import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import praktikum.models.User;
import praktikum.models.UserCreds;
import praktikum.models.UserData;
import java.util.Map;
import static org.junit.Assert.*;
import static praktikum.User.UserGenerator.randomUserWithoutPassword;
public class UserDataTests extends BaseTest{

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
        assertTrue(updateUserDataResponse.path("success"));
        Map< String , String > responseUserData = updateUserDataResponse.path("user");
        assertEquals(newUserData, new Gson().fromJson(responseUserData.toString(), UserData.class));
        newUser.setPassword(randomUser.getPassword());
        userAccessToken = loginUserAndGetUserAccessToken(newUser);
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
        assertFalse(updateUserDataResponse.path("success"));
        assertEquals("You should be authorised", updateUserDataResponse.path("message"));

        UserCreds userCreds = new UserCreds();
        newUser.setPassword(randomUser.getPassword());
        Response newUserDataLoginResponse = userClient.loginUser(userCreds.credsFrom(newUser));
        assertEquals("Неверный статус код обновления данных о пользователе", 401, newUserDataLoginResponse.statusCode());
    }
}
