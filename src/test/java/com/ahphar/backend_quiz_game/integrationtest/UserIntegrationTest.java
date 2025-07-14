package com.ahphar.backend_quiz_game.integrationtest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

public class UserIntegrationTest {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    private String loginAndGetToken(String username, String password) {
        String payload = """
            {
                "username": "%s",
                "password": "%s"
            }
        """.formatted(username, password);

        return given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
    }

    @Test
    public void shouldUpdateUsernameSuccessfullyAndReset() {
        String token = loginAndGetToken("Six", "password@123");

        String newUsernamePayload = """
            {
                "newUsername": "New Six"
            }
            """;

        String newToken = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(newUsernamePayload)
                .when()
                .put("/users/me/username")
                .then()
                .statusCode(200)
                .body("user.username", equalTo("New Six"))
                .extract()
                .jsonPath()
                .getString("token");

        // Reset
        String resetPayload = """
            {
                "newUsername": "Six"
            }
            """;

        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + newToken)
            .body(resetPayload)
            .when()
            .put("/users/me/username")
            .then()
            .statusCode(200)
            .body("user.username", equalTo("Six"));
    }

    @Test
    public void shouldFailWhenUpdatingToSameUsername() {
        String token = loginAndGetToken("Six", "password@123");

        String sameUsernamePayload = """
            {
                "newUsername": "Six"
            }
            """;

        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + token)
            .body(sameUsernamePayload)
            .when()
            .put("/users/me/username")
            .then()
            .statusCode(400)
            .body("error", equalTo("Username already taken."));
    }

    @Test
    public void shouldFailWhenUsernameAlreadyExists() {
        String token = loginAndGetToken("Six", "password@123");

        String payload = """
            {
                "newUsername": "Five"
            }
            """;

        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + token)
            .body(payload)
            .when()
            .put("/users/me/username")
            .then()
            .statusCode(400)
            .body("error", containsString("Username already taken."));
    }

}
