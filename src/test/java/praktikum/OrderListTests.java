package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.Order.OrderClient;
import praktikum.User.UserClient;
import praktikum.models.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static praktikum.User.UserGenerator.randomUser;
import static praktikum.UserCreationTests.createUser;
import static praktikum.UserCreationTests.deleteUser;
import static praktikum.UserLoginTests.loginUserAndGetUserAccessToken;

public class OrderListTests {

    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static UserClient userClient = new UserClient();
    private static OrderClient orderClient = new OrderClient();

    private static User randomUser = randomUser();
    private static String userAccessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("get User Order List With Authorization Returns Order List")
    @Description("запрос на получение заказов пользователя c авторизацией возвращает список заказов")
    public void getUserOrderListWithAuthorizationReturnsOrderList() {
        createUser(randomUser);
        userAccessToken = loginUserAndGetUserAccessToken(randomUser);
        Response getUserOrderListResponse = orderClient.getUserOrderList(userAccessToken);
        assertEquals("Неверный статус код", 200, getUserOrderListResponse.statusCode());
        assertNotNull(getUserOrderListResponse.path("orders"));
    }

    @Test
    @DisplayName("get User Order List Without Authorization Returns Error")
    @Description("запрос на получение заказов пользователя без авторизации возвращает ошибку")
    public void getUserOrderListWithoutAuthorizationReturnsError() {
        createUser(randomUser);
        userAccessToken = loginUserAndGetUserAccessToken(randomUser);
        Response getUserOrderListResponse = orderClient.getUserOrderListWithoutAuthorization();
        assertEquals("Неверный статус код", 401, getUserOrderListResponse.statusCode());
        assertEquals(false, getUserOrderListResponse.path("success"));
        assertEquals("You should be authorised", getUserOrderListResponse.path("message"));
    }

    @After
    public void tearDown() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken); // удаление курьера
        }
    }
}

