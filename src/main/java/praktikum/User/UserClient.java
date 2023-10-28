package praktikum.User;

import io.restassured.response.Response;
import praktikum.models.User;
import praktikum.models.UserCreds;
import praktikum.models.UserData;


import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String CREATE_URL = "/api/auth/register";
    private static final String LOGIN_URL = "/api/auth/login";
    private static final String USER_URL = "/api/auth/user";



    public Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_URL);
    }

    public Response loginUser(UserCreds creds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(creds)
                .when()
                .post(LOGIN_URL);
    }

    public Response deleteUser(String userAccessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(userAccessToken)
                .and()
                .when()
                .delete(USER_URL);
    }

    public Response updateUserData(String userAccessToken, UserData userData) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(userAccessToken)
                .and()
                .body(userData)
                .when()
                .patch(USER_URL);
    }

    public Response updateUserDataWithoutAuthorization(UserData userData) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userData)
                .when()
                .patch(USER_URL);
    }



}
