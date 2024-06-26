package com.teamcity.api.requests.checked;

import com.teamcity.api.models.BuildType;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedBuildConfig extends Request implements CrudInterface {

    public CheckedBuildConfig(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public BuildType create(Object obj) {
        return new UncheckedBuildConfig(spec).create(obj).then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(BuildType.class);
    }

    @Override
    public Object get(String id) {
        return null;
    }

    @Override
    public Object update(Object obj) {
        return null;
    }

    @Override
    public String delete(String id) {
        return new UncheckedBuildConfig(spec).delete(id).then().assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().asString();
    }
}
