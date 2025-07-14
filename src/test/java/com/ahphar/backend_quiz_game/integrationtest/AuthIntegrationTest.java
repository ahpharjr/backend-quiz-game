package com.ahphar.backend_quiz_game.integrationtest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AuthIntegrationTest {
    
    @BeforeAll
    static void setUp(){
        RestAssured.baseURI = "http://localhost:8080";
    }

    //Integration test for user login with valid credentials

    @Test
    public void shouldReturnOKWithValidToken(){
        String loginPayload = """
                {
                    "username": "Six",
                    "password": "password@123"
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .response();

        System.out.println("Generated Token: " + response.jsonPath().getString("token"));
    }

    //Integration test for user login with invalid credentials
    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin(){
        String loginPayload = """
                {
                    "username": "invalid username",
                    "password": "wrongpassword"
                }
                """;

        given()
            .contentType("application/json")
            .body(loginPayload)
            .when()
            .post("/auth/login")
            .then()
            .statusCode(401)
            .body(equalTo("Invalid username or password."));
    }

    //Integration test for user login with unverified email
    @Test 
    public void shouldReturnForbiddenOnUnverifiedLogin(){
        String loginPayload = """
                {
                    "username": "Five",
                    "password": "password@123"
                }
                """;

        given()
            .contentType("application/json")
            .body(loginPayload)
            .when()
            .post("/auth/login")
            .then()
            .statusCode(403)
            .body(equalTo("Please verify your email."));
    }

    @Test
    public void shouldReturnOKWithValidRegisteration(){
        String registerPayload = """
                {
                    "username": "NewUser",
                    "email": "newuser@example.com",
                    "password": "password@123"
                }
                """;

        given()
            .contentType("application/json")
            .body(registerPayload)
            .when()
            .post("/auth/register")
            .then()
            .statusCode(200)
            .body(equalTo("Register successfully. Please verify your email."));
    }

    @Test
    public void shouldReturn400WithInvalidEmailRegisteration(){
        String registerPayload = """
                {
                    "username": "NewUsernew",
                    "email": "newuser@example.com",
                    "password": "password@123"
                }
                """;

        given()
            .contentType("application/json")
            .body(registerPayload)
            .when()
            .post("/auth/register")
            .then()
            .statusCode(400)
            .body(equalTo("Email already exists"));
    }

    @Test
    public void shouldReturn400WithInvalidUsernameRegisteration(){
        String registerPayload = """
                {
                    "username": "NewUser",
                    "email": "newuser1@example.com",
                    "password": "password@123"
                }
                """;

        given()
            .contentType("application/json")
            .body(registerPayload)
            .when()
            .post("/auth/register")
            .then()
            .statusCode(400)
            .body(equalTo("Player name already taken"));
    }

}

