package com.teamcity.api.requests.checked;

import com.teamcity.api.models.User;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.unchecked.UncheckedUser;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedUser extends Request implements CrudInterface {

    public CheckedUser(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public User create(Object obj) {
        return new UncheckedUser(spec)
                .create(obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(User.class);
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
    public Object delete(String id) {
        return new UncheckedUser(spec).delete(id).then().assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().asString();
    }
}