package com.example.teamcity.api;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.generators.TestData;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.generators.TestDataStorage;
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
    public void projectWithSpecialSymbolsInNameCanBeCreated() {
        String testString = "This_is_a_string_with_special_symbols :!@#$%^&*()_+-=[]{}|;:',./<>?~`\"";

        TestData testData = TestDataStorage.getStorage().addTestData(TestData.builder()
                .user(TestDataGenerator.generateUser())
                .project(TestDataGenerator.generateProject(testString, RandomData.getString()))
                .build());

        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        Project project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getName()).isEqualTo(testString);
    }

    @Test
    public void projectWithValidIdWithMaxLengthCanBeCreated() {
        String testString = "a" + RandomStringUtils.randomAlphanumeric(MAX_ID_LENGTH - 1);

        TestData testData = TestDataStorage.getStorage().addTestData(TestData.builder()
                .user(TestDataGenerator.generateUser())
                .project(TestDataGenerator.generateProject(RandomData.getString(), testString))
                .build());

        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        Project project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testString);
    }

    @Test
    public void projectWithIdLongerThanMaxLengthShouldNotBeCreated() {
        String testString = RandomStringUtils.randomAlphanumeric(MAX_ID_LENGTH + 1);

        TestData testData = TestDataStorage.getStorage().addTestData(TestData.builder()
                .user(TestDataGenerator.generateUser())
                .project(TestDataGenerator.generateProject(RandomData.getString(), testString))
                .build());

        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new UncheckedRequests(Specifications.getSpec().authSpec(testData.getUser())).getProjectRequest()
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) //note: should be SC_BAD_REQUEST instead
                .body(Matchers.containsString("Project ID \"" + testString
                        + "\" is invalid: it is 226 characters long while the maximum length is 225."));
    }

    @Test
    public void projectWithSpecialCharactersInIDShouldNotBeCreated() {
        String testString = "String_with_$peci@l_ch@r@cters";

        TestData testData = TestDataStorage.getStorage().addTestData(TestData.builder()
                .user(TestDataGenerator.generateUser())
                .project(TestDataGenerator.generateProject(RandomData.getString(), testString))
                .build());

        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new UncheckedRequests(Specifications.getSpec().authSpec(testData.getUser())).getProjectRequest()
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) //note: should be SC_BAD_REQUEST instead
                .body(Matchers.containsString("Project ID \"" + testString
                        + "\" is invalid: contains unsupported character"));
    }

    @Test
    public void projectWithIDThatStartsWithNonLatinCharacterShouldNotBeCreated() {
        String testString = RandomStringUtils.randomNumeric(10);

        TestData testData = TestDataStorage.getStorage().addTestData(TestData.builder()
                .user(TestDataGenerator.generateUser())
                .project(TestDataGenerator.generateProject(RandomData.getString(), testString))
                .build());

        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new UncheckedRequests(Specifications.getSpec().authSpec(testData.getUser())).getProjectRequest()
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) //note: should be SC_BAD_REQUEST instead
                .body(Matchers.containsString("Project ID \"" + testString
                        + "\" is invalid: starts with non-letter character"));
    }

    @Test
    public void projectWithAlreadyExistingIdShouldNotBeCreated() {
        TestData testData = TestDataStorage.getStorage().addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        Project project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());

        new UncheckedRequests(Specifications.getSpec().authSpec(testData.getUser())).getProjectRequest()
                .create(TestDataGenerator.generateProject(RandomData.getString(), testData.getProject().getId()))
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project ID \"" + testData.getProject().getId()
                        + "\" is already used by another project"));
    }

    @Test
    public void projectWithAlreadyExistingNameShouldNotBeCreated() {
        TestData testData = TestDataStorage.getStorage().addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        Project project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getName()).isEqualTo(testData.getProject().getName());

        new UncheckedRequests(Specifications.getSpec().authSpec(testData.getUser())).getProjectRequest()
                .create(TestDataGenerator.generateProject(testData.getProject().getName(), RandomData.getString()))
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project with this name already exists: "
                                + testData.getProject().getName()));
    }

}