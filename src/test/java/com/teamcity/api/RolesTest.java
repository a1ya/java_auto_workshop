package com.teamcity.api;

import com.teamcity.api.enums.Role;
import com.teamcity.api.generators.TestData;
import com.teamcity.api.generators.TestDataGenerator;
import com.teamcity.api.requests.UncheckedRequests;
import com.teamcity.api.requests.checked.CheckedBuildConfig;
import com.teamcity.api.requests.checked.CheckedProject;
import com.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners()
public class RolesTest extends BaseApiTest {
    @Test
    public void unauthorizedUserShouldNotHaveRightToCreateProject() {
        var testData = testDataStorage.addTestData();

        new UncheckedRequests(Specifications.getSpec().unauthSpec()).getProjectRequest()
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);

        uncheckedWithSuperUser.getProjectRequest()
                .get(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by locator" +
                        " 'count:1,id:" + testData.getProject().getId() + "'"));
    }

    @Test
    public void systemAdminShouldHaveRightsToCreateProject() {
        var testData = testDataStorage.addTestData();

        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    @Test
    public void projectAdminShouldHaveRightsToCreateBuildConfigToHisProject() {
        var testData = testDataStorage.addTestData();

        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" + testData.getProject().getId()));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType());

        softy.assertThat(buildConfig.getId()).isEqualTo(testData.getBuildType().getId());
    }

    @Test
    public void projectAdminShouldNotHaveRightsToCreateBuildConfigToAnotherProject() {

        TestData firstTestData = testDataStorage.addTestData();
        TestData secondTestData = testDataStorage.addTestData();

        checkedWithSuperUser.getProjectRequest().create(firstTestData.getProject());
        checkedWithSuperUser.getProjectRequest().create(secondTestData.getProject());

        firstTestData.getUser().setRoles(TestDataGenerator.
                generateRoles(Role.PROJECT_ADMIN, "p:" + firstTestData.getProject().getId()));
        checkedWithSuperUser.getUserRequest().create(firstTestData.getUser());

        secondTestData.getUser().setRoles(TestDataGenerator.
                generateRoles(Role.PROJECT_ADMIN, "p:" + secondTestData.getProject().getId()));
        checkedWithSuperUser.getUserRequest().create(secondTestData.getUser());

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(secondTestData.getUser()))
                .create(firstTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .body(Matchers.containsString("You do not have enough permissions to edit project with id: "
                        + firstTestData.getProject().getId()));
    }

}