package com.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.Selectors;
import com.teamcity.ui.pages.Page;

import static com.codeborne.selenide.Selenide.element;

public class CreateNewProjectPage extends Page {

    private final SelenideElement urlInput = element(Selectors.byId("url"));
    private final SelenideElement projectNameInput = element(Selectors.byId("projectName"));
    private final SelenideElement buildTypeNameInput = element(Selectors.byId("buildTypeName"));

    public CreateNewProjectPage open(String parentProjectId) {
        Selenide.open("/admin/createObjectMenu.html?projectId="
                + parentProjectId + "&showMode=createProjectMenu");
        waitUntilPageIsLoaded();
        return this;
    }

    public CreateNewProjectPage createProjectByUrl(String url) {
        urlInput.sendKeys(url);
        submit();
        waitUntilPageIsLoaded();
        return this;
    }

    public void setupProject(String projectName, String buildTypeName) {
        projectNameInput.clear();
        buildTypeNameInput.clear();
        projectNameInput.sendKeys(projectName);
        buildTypeNameInput.sendKeys(buildTypeName);
        submit();
    }
}
