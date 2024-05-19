package com.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.teamcity.api.generators.TestData;
import com.teamcity.ui.pages.favourites.ProjectsPage;
import com.teamcity.ui.pages.admin.CreateNewProjectPage;
import org.testng.annotations.Test;

public class CreateNewProjectTest extends BaseUiTest{

    @Test
    public void authorizedUserShouldBeAbleToCreateNewProject() {
        TestData testData = testDataStorage.addTestData();
        String repositoryUrl = "https://github.com/AlexPshe/spring-core-for-qa";

        loginAsUser(testData.getUser());

        new CreateNewProjectPage()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(repositoryUrl)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        new ProjectsPage()
                .open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));

    }
}
