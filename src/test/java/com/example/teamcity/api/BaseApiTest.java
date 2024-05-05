package com.example.teamcity.api;

import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseApiTest extends BaseTest {
    public TestDataStorage testDataStorage;

    public CheckedRequests checkedWithSuperUser
            = new CheckedRequests(Specifications.getSpec().superUserSpec());

    public UncheckedRequests uncheckedWithSuperUser
            = new UncheckedRequests(Specifications.getSpec().superUserSpec());

    @BeforeMethod
    public void setupTest() {
        testDataStorage = TestDataStorage.getStorage();

        String requestBody = "{\"perProjectPermissions\":true,\"modules\":{\"module\":[{\"name\":\"Default\",\"properties\":{\"property\":[{\"name\":\"usersCanResetOwnPasswords\",\"value\":\"true\"},{\"name\":\"usersCanChangeOwnPasswords\",\"value\":\"true\"},{\"name\":\"freeRegistrationAllowed\",\"value\":\"false\"}],\"count\":3}},{\"name\":\"Token-Auth\",\"properties\":{\"property\":[],\"count\":0}},{\"name\":\"HTTP-Basic\",\"properties\":{\"property\":[],\"count\":0}}]}}";

        RestAssured
                .given()
                .spec(Specifications.getSpec().superUserSpec())
                .body(requestBody)
                .put("/app/rest/server/authSettings")
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @AfterMethod
    public void cleanTest() {
        testDataStorage.delete();
    }
}