package com.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.teamcity.api.generators.TestData;
import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.Project;
import com.teamcity.api.requests.checked.CheckedProject;
import com.teamcity.api.spec.Specifications;
import com.teamcity.ui.pages.admin.CreateNewBuildConfigurationPage;
import com.teamcity.ui.pages.favourites.ProjectPage;
import org.testng.annotations.Test;

public class CreateNewBuildTest extends BaseUiTest{

    @Test
    public void projectAdminShouldBeAbleToCreateNewBuildConfigurationForProject() {
        TestData testData = testDataStorage.addTestData();
        String expectedBuildTypeName = testData.getBuildType().getName();
        String repositoryUrl = "https://github.com/AlexPshe/spring-core-for-qa";

        loginAsUser(testData.getUser());

        Project project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        new CreateNewBuildConfigurationPage()
                .open(project.getId())
                .createBuildConfigurationByUrl(repositoryUrl)
                .setupBuildConfiguration(expectedBuildTypeName);

        softy.assertThat(checkedWithSuperUser.getProjectRequest().get(project.getId()).getBuildTypes().getBuildType())
                .as("Expected build type name %s not found for the project with name %s", expectedBuildTypeName, project.getName())
                .extracting(BuildType::getName)
                .containsExactly(expectedBuildTypeName);

        new ProjectPage()
                .open(project.getId())
                .getBuilds()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(expectedBuildTypeName))
                .click();

    }

}
