package com.teamcity.ui.pages.favourites;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.Selectors;
import com.teamcity.ui.elements.BuildElement;
import com.teamcity.ui.pages.Page;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.element;
import static com.codeborne.selenide.Selenide.elements;

public class ProjectPage extends Page {

    private final SelenideElement header = element(Selectors.byClass("ProjectPageHeader__title--ih"));
    private final ElementsCollection builds = elements(Selectors.byClass("BuildTypeLine__buildTypeInfo--Zh"));

    public void waitUntilProjectPageIsLoaded() {
        waitUntilPageIsLoaded();
        header.shouldBe(Condition.visible, Duration.ofSeconds(10));
    }

    public ProjectPage open(String projectId) {
        Selenide.open("/project/" + projectId);
        waitUntilProjectPageIsLoaded();
        return this;
    }

    public List<BuildElement> getBuilds(){
        return generatePageElements(builds, BuildElement::new);
    }
}
