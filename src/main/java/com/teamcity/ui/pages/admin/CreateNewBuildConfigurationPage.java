package com.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.Selectors;
import com.teamcity.ui.pages.Page;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

public class CreateNewBuildConfigurationPage extends Page {

    private final SelenideElement urlInput = element(Selectors.byId("url"));
    private final SelenideElement buildTypeNameInput = element(Selectors.byId("buildTypeName"));

    public CreateNewBuildConfigurationPage open(String projectId) {
        Selenide.open("/admin/createObjectMenu.html?projectId="
                + projectId + "&showMode=createBuildTypeMenu");
        waitUntilPageIsLoaded();
        return this;
    }

    public CreateNewBuildConfigurationPage createBuildConfigurationByUrl(String url) {
        urlInput.sendKeys(url);
        submit();
        waitUntilPageIsLoaded();
        return this;
    }

    public void setupBuildConfiguration(String buildTypeName) {
        buildTypeNameInput.shouldBe(Condition.visible, Duration.ofSeconds(15));
        buildTypeNameInput.clear();
        buildTypeNameInput.sendKeys(buildTypeName);
        submit();
    }

}
