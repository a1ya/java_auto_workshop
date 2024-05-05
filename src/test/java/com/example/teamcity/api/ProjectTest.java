package com.example.teamcity.api;

import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.generators.TestData;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.spec.Specifications;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class ProjectTest extends BaseApiTest {

    private static final int MAX_ID_LENGTH = 225;

    @Test
    public void projectWithValidMaxLengthIdCanBeCreated() {
        String testString = "a" + RandomStringUtils.randomAlphanumeric(MAX_ID_LENGTH-1);

        TestData testData = TestDataStorage.getStorage().addTestData(TestData.builder()
                .user(TestDataGenerator.generateUser())
                .project(NewProjectDescription
                        .builder()
                        .name(RandomData.getString())
                        .id(testString)
                        .build())
                .build());
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        Project project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testString);
    }


    @Test
    public void projectWithIdBiggerThenMaxLengthShouldNotBeCreated() {
        String testString = RandomStringUtils.randomAlphanumeric(MAX_ID_LENGTH+1);

        TestData testData = TestDataStorage.getStorage().addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        NewProjectDescription newProjectDescription = NewProjectDescription
                .builder()
                .name(RandomData.getString())
                .id(testString)
                .build();

        new UncheckedRequests(Specifications.getSpec().authSpec(testData.getUser())).getProjectRequest()
                .create(newProjectDescription)
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) //TODO: make it SC_BAD_REQUEST instead
                .body(Matchers.containsString("Project ID \"" + testString
                        + "\" is invalid: it is 226 characters long while the maximum length is 225."));
    }

    @Test
    public void projectWithSpecialCharactersInIDShouldNotBeCreated() {
        String testString = "String_with_$peci@l_ch@r@cters";

        TestData testData = TestDataStorage.getStorage().addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        NewProjectDescription newProjectDescription = NewProjectDescription
                .builder()
                .name(RandomData.getString())
                .id(testString)
                .build();

        new UncheckedRequests(Specifications.getSpec().authSpec(testData.getUser())).getProjectRequest()
                .create(newProjectDescription)
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) //TODO: make it SC_BAD_REQUEST instead
                .body(Matchers.containsString("Project ID \"" + testString
                        + "\" is invalid: contains unsupported character '$'. ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters)."));
    }

    @Test
    public void projectWithIDThatStartsWithNonLatinCharacterShouldNotBeCreated() {
        String testString = RandomStringUtils.randomNumeric(10);

        TestData testData = TestDataStorage.getStorage().addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        NewProjectDescription newProjectDescription = NewProjectDescription
                .builder()
                .name(RandomData.getString())
                .id(testString)
                .build();

        new UncheckedRequests(Specifications.getSpec().authSpec(testData.getUser())).getProjectRequest()
                .create(newProjectDescription)
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) //TODO: make it SC_BAD_REQUEST instead
                .body(Matchers.containsString("Project ID \"" + testString
                        + "\" is invalid: starts with non-letter character"));
    }

    @Test
    public void projectWithAlreadyExistingIdShouldNotBeCreated() {
        TestData testData = TestDataStorage.getStorage().addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        Project project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());

        NewProjectDescription newProjectDescription = NewProjectDescription
                .builder()
                .name(RandomData.getString())
                .id(testData.getProject().getId())
                .build();

        new UncheckedRequests(Specifications.getSpec().authSpec(testData.getUser())).getProjectRequest()
                .create(newProjectDescription)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project ID \"" + testData.getProject().getId()
                        + "\" is already used by another project"));
    }

    @Test
    public void projectWithAlreadyExistingNameShouldNotBeCreated() {
        TestData testData = TestDataStorage.getStorage().addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        Project project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());

        NewProjectDescription newProjectDescription = NewProjectDescription
                .builder()
                .name(testData.getProject().getName())
                .id(RandomData.getString())
                .build();

        new UncheckedRequests(Specifications.getSpec().authSpec(testData.getUser())).getProjectRequest()
                .create(newProjectDescription)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project with this name already exists: "
                                + testData.getProject().getName()));
    }

}