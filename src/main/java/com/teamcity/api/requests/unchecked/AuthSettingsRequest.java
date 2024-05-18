package com.teamcity.api.requests.unchecked;

import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class AuthSettingsRequest extends Request implements CrudInterface {

    private final static String AUTH_SETTINGS_ENDPOINT = "/app/rest/server/authSettings";

    public AuthSettingsRequest(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Object create(Object obj) {
            return null;
    }

    @Override
    public Object get(String id) {
        return null;
    }

    @Override
    public Response update(Object obj) {
        return given()
                .spec(spec)
                .body(obj)
                .put(AUTH_SETTINGS_ENDPOINT);
    }

    @Override
    public Object delete(String id) {
        return null;
    }
}