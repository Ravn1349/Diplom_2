package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
public class OrderListTests extends BaseTest{

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
}

