package com.teamcity.api.requests.checked;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class AuthRequest {
    private RequestSpecification spec;

    public AuthRequest (RequestSpecification spec) {
        this.spec = spec;
    }

    public String getCsrfToken() {
        return RestAssured
                .given()
                .spec(spec)
                .get("/authenticationTest.html?csrf")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().asString();
    }

}
