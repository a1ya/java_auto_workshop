package com.example.teamcity.api.spec;
import com.example.teamcity.api.configs.Config;
import com.example.teamcity.api.models.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.List;

public class Specifications {

    private static Specifications spec;

    private Specifications() {};

    public static Specifications getSpec(){
        if (spec ==null){
            spec = new Specifications();
        }
        return spec;
    }

    private RequestSpecBuilder reqBuilder(){
        var requestBuilder = new RequestSpecBuilder();
        requestBuilder.setBaseUri("http://" + Config.getProperty("host"));
        requestBuilder.addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
        requestBuilder.setContentType(ContentType.JSON);
        requestBuilder.setAccept(ContentType.JSON);
        return requestBuilder;
    }

    public RequestSpecification unauthSpec(){
        var requestBuilder = reqBuilder();
        requestBuilder.setContentType(ContentType.JSON);
        requestBuilder.setAccept(ContentType.JSON);
        return requestBuilder.build();
    }

    public RequestSpecification authSpec(User user){
        var requestBuilder = reqBuilder();
        requestBuilder.setBaseUri("http://" + user.getUsername() + ":" + user.getPassword() + "@" + Config.getProperty("host"));
        return requestBuilder.build();
    }

    public RequestSpecification superUserSpec(){
        var requestBuilder = reqBuilder();
        requestBuilder.setBaseUri("http://:" + Config.getProperty("superUserToken") + "@" + Config.getProperty("host"));
        return requestBuilder.build();
    }
}
