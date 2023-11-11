package praktikum.Order;

import io.restassured.response.Response;
import praktikum.models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String CREATE_URL = "/api/orders";

    public Response createOrder(String userAccessToken, Order order) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(userAccessToken)
                .and()
                .body(order)
                .when()
                .post(CREATE_URL);
    }

    public Response createOrderWithoutAuthorization(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(CREATE_URL);
    }

    public Response getUserOrderList(String userAccessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(userAccessToken)
                .and()
                .when()
                .get(CREATE_URL);
    }

    public Response getUserOrderListWithoutAuthorization() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(CREATE_URL);
    }
}
