package com.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.teamcity.api.generators.TestData;
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
        String repositoryUrl = "https://github.com/AlexPshe/spring-core-for-qa";

        loginAsUser(testData.getUser());

        Project project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        new CreateNewBuildConfigurationPage()
                .open(project.getId())
                .createBuildConfigurationByUrl(repositoryUrl)
                .setupBuildConfiguration(testData.getBuildType().getName());

        new ProjectPage()
                .open(project.getId())
                .getBuilds()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(testData.getBuildType().getName()));
    }

}
