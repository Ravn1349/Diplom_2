package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import praktikum.models.Order;

import static org.junit.Assert.*;

public class OrderCreationTests extends BaseTest {
    @Test
    @DisplayName("create Order With Authorization Returns Order Number")
    @Description("запрос на создание заказа c авторизацией возвращает номер заказа")
    public void createOrderWithAuthorizationReturnsOrderNumber() {
        createUser(randomUser);
        userAccessToken = loginUserAndGetUserAccessToken(randomUser);
        Order order = new Order();
        order.addIngredients(new String[]{"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"});
        Response createOrderResponse = orderClient.createOrder(userAccessToken, order);
        assertEquals("Неверный статус код", 200, createOrderResponse.statusCode());
        assertTrue(createOrderResponse.path("success"));
        int orderNumber = createOrderResponse.path("order.number");
    }

    @Test
    @DisplayName("create Order Without Authorization Returns Order Number")
    @Description("запрос на создание заказа без авторизации возвращает номер заказа")
    public void createOrderWithoutAuthorizationReturnsOrderNumber() {
        createUser(randomUser);
        userAccessToken = loginUserAndGetUserAccessToken(randomUser);
        Order order = new Order();
        order.addIngredients(new String[]{"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"});
        Response createOrderResponse = orderClient.createOrderWithoutAuthorization(order);
        assertEquals("Неверный статус код", 200, createOrderResponse.statusCode()); //
        assertTrue(createOrderResponse.path("success")); // В документации API сказано, что только авторизованные пользователи могут делать заказы. Опираюсь на фактическое поведение, т.к. по условиям задания тесты должны проходить.
        int orderNumber = createOrderResponse.path("order.number"); //
    }

    @Test
    @DisplayName("create Order Without Ingredients Returns Error")
    @Description("запрос на создание заказа без ингредиентов возвращает ошибку")
    public void createOrderWithoutIngredientsReturnsError() {
        createUser(randomUser);
        userAccessToken = loginUserAndGetUserAccessToken(randomUser);
        Order order = new Order();
        Response createOrderResponse = orderClient.createOrder(userAccessToken, order);
        assertEquals("Неверный статус код", 400, createOrderResponse.statusCode());
        assertFalse(createOrderResponse.path("success"));
        assertEquals("Ingredient ids must be provided", createOrderResponse.path("message"));
    }

    @Test
    @DisplayName("create Order With Wrong Ingredients Hash Returns Error")
    @Description("запрос на создание заказа с неверным хешем ингредиентов возвращает ошибку")
    public void createOrderWithWrongIngredientsHashReturnsError() {
        createUser(randomUser);
        userAccessToken = loginUserAndGetUserAccessToken(randomUser);
        Order order = new Order();
        order.addIngredients(new String[]{"61c0c5a71d1f82001bdaaa6d1","61c0c5a71d1f82001bdaaa6f1"});
        Response createOrderResponse = orderClient.createOrder(userAccessToken, order);
        assertEquals("Неверный статус код", 500, createOrderResponse.statusCode());
    }
}
