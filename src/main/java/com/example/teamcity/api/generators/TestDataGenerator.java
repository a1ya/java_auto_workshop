package com.example.teamcity.api.generators;

import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.models.Roles;
import com.example.teamcity.api.models.Role;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.NewProjectDescription;
//import com.example.teamcity.api.enums.Role;

import java.util.Arrays;

public class TestDataGenerator {
    public static TestData generate() {
        var user = generateUser();
        var newProjectDescription = generateProject();
        var buildType = generateBuild(newProjectDescription);

        return TestData.builder()
                .user(user)
                .project(newProjectDescription)
                .buildType(buildType)
                .build();
    }

    public static User generateUser(){
        return User.builder()
                .username(RandomData.getString())
                .password(RandomData.getString())
                .email(RandomData.getString() + "@gmail.com")
                .build();
    }

    public static NewProjectDescription generateProject(){
        return NewProjectDescription
                .builder()
                .parentProject(Project.builder()
                        .locator("_Root")
                        .build())
                .name(RandomData.getString())
                .id(RandomData.getString())
                .copyAllAssociatedSettings(true)
                .build();
    }

    public static BuildType generateBuild(NewProjectDescription newProjectDescription){
        return BuildType.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .project(newProjectDescription)
                .build();
    }

    public static Roles generateRoles(com.example.teamcity.api.enums.Role role, String scope) {
        return Roles.builder().role
                (Arrays.asList(Role.builder().roleId(role.getText())
                        .scope(scope).build())).build();
    }
}