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
import praktikum.models.Order;
import praktikum.models.User;

import static org.junit.Assert.assertEquals;
import static praktikum.User.UserGenerator.randomUser;
import static praktikum.UserCreationTests.createUser;
import static praktikum.UserCreationTests.deleteUser;
import static praktikum.UserLoginTests.loginUserAndGetUserAccessToken;

public class OrderCreationTests {

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
    @DisplayName("create Order With Authorization Returns Order Number")
    @Description("запрос на создание заказа c авторизацией возвращает номер заказа")
    public void createOrderWithAuthorizationReturnsOrderNumber() {
        createUser(randomUser);
        userAccessToken = loginUserAndGetUserAccessToken(randomUser);
        Order order = new Order();
        order.addIngredients(new String[]{"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"});
        Response createOrderResponse = orderClient.createOrder(userAccessToken, order);
        assertEquals("Неверный статус код", 200, createOrderResponse.statusCode());
        assertEquals(true, createOrderResponse.path("success"));
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
        assertEquals(true, createOrderResponse.path("success")); // В документации API сказано, что только авторизованные пользователи могут делать заказы. Опираюсь на фактическое поведение, т.к. по условиям задания тесты должны проходить.
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
        assertEquals(false, createOrderResponse.path("success"));
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

    @After
    public void tearDown() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken); // удаление курьера
        }
    }
}
