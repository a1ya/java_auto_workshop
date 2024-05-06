package com.example.teamcity.api;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.generators.TestData;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.spec.Specifications;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class BuildConfigurationTest extends BaseApiTest{

    private static final int MAX_ID_LENGTH = 225;

    @Test
    public void buildConfigurationTest(){
        var testData = TestDataStorage.getStorage().addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    @Test
    public void buildConfigurationWithSpecialSymbolsInNameCanBeCreated() {
        String testString = "This_is_a_string_with_special_symbols :!@#$%^&*()_+-=[]{}|;:',./<>?~`\"";
        NewProjectDescription newProjectDescription = TestDataGenerator.generateProject(RandomData.getString(), RandomData.getString());

        TestData testData = TestDataStorage.getStorage().addTestData(TestData.builder()
                .user(TestDataGenerator.generateUser())
                .project(newProjectDescription)
                .buildType(TestDataGenerator.generateBuild(newProjectDescription, testString, RandomData.getString()))
                .build());

        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        var buildConfiguration = new CheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());

        softy.assertThat(buildConfiguration.getName()).isEqualTo(testData.getBuildType().getName());
    }

    @Test
    public void buildConfigurationWithoutProjectShouldNotBeCreated() {
        NewProjectDescription newProjectDescription = TestDataGenerator.generateProject(RandomData.getString(), RandomData.getString());

        TestData testData = TestDataStorage.getStorage().addTestData(TestData.builder()
                .user(TestDataGenerator.generateUser())
                .project(newProjectDescription)
                .buildType(TestDataGenerator.generateBuild(newProjectDescription, RandomData.getString(), RandomData.getString()))
                .build());

        testData.setBuildType(TestDataGenerator.generateBuild(newProjectDescription, RandomData.getString(), RandomData.getString()));

        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new UncheckedRequests(Specifications.getSpec().authSpec(testData.getUser())).getBuildConfigRequest()
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by locator" +
                        " 'count:1,id:" + testData.getProject().getId() + "'"));
    }

    @Test
    public void buildConfigurationWithIDLongerThanMaxLengthShouldNotBeCreated() {
        String testString = RandomStringUtils.randomAlphanumeric(MAX_ID_LENGTH + 1);
        NewProjectDescription newProjectDescription = TestDataGenerator.generateProject(RandomData.getString(), RandomData.getString());

        TestData testData = TestDataStorage.getStorage().addTestData(TestData.builder()
                .user(TestDataGenerator.generateUser())
                .project(newProjectDescription)
                .buildType(TestDataGenerator.generateBuild(newProjectDescription, RandomData.getString(), testString))
                .build());

        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        new UncheckedRequests(Specifications.getSpec().authSpec(testData.getUser())).getBuildConfigRequest()
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) //note: should be SC_BAD_REQUEST instead
                .body(Matchers.containsString("Build configuration or template ID \""
                        + testData.getBuildType().getId()
                        + "\" is invalid: it is 226 characters long while the maximum length is 225."));
    }

}
