package com.teamcity.api.generators;

import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.User;
import com.teamcity.api.models.Roles;
import com.teamcity.api.models.Role;
import com.teamcity.api.models.Project;
import com.teamcity.api.models.NewProjectDescription;
//import com.example.teamcity.api.enums.Role;

import java.util.Arrays;

public class TestDataGenerator {
    public static TestData generate() {
        var user = generateUser();
        var newProjectDescription = generateProject(RandomData.getString(), RandomData.getString());
        var buildType = generateBuild(newProjectDescription, RandomData.getString(), RandomData.getString());

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
                .roles(Roles.builder()
                        .role(Arrays.asList(Role.builder()
                                .roleId("SYSTEM_ADMIN")
                                .scope("g")
                                .build()))
                        .build())
                .build();
    }

    public static NewProjectDescription generateProject(String name, String id){
        return NewProjectDescription
                .builder()
                .parentProject(Project.builder()
                        .locator("_Root")
                        .build())
                .name(name)
                .id(id)
                .copyAllAssociatedSettings(true)
                .build();
    }

    public static BuildType generateBuild(NewProjectDescription newProjectDescription, String name, String id){
        return BuildType.builder()
                .id(id)
                .name(name)
                .project(newProjectDescription)
                .build();
    }

    public static Roles generateRoles(com.teamcity.api.enums.Role role, String scope) {
        return Roles.builder().role
                (Arrays.asList(Role.builder().roleId(role.getText())
                        .scope(scope).build())).build();
    }
}