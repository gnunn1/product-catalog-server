package com.redhat.demo;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static io.restassured.RestAssured.given;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(OrderAnnotation.class)
public class AuthEndpointTest {

    static final String USER = "bob.smith@noreply.com";
    static final String PASSWORD = "password";

    @Test
    @Order(1)
    public void testCreateUser() {
        given()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .formParam("email", USER)
            .formParam("password", PASSWORD)
            .formParam("password_confirmation", PASSWORD)
            .request()
            .post("/api/auth/register")
                .then()
                    .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @Order(2)
    public void login() {

        given()
            .auth().basic(USER,PASSWORD)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .formParam("email", USER)
            .formParam("password", PASSWORD)
            .post("/api/auth")
            .then()
                .statusCode(HttpStatus.SC_OK);

        given()
            .auth().basic(USER,"notpassword")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .formParam("email", USER)
            .formParam("password", "notpassword")
            .post("/api/auth")
            .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

}
