package com.teamcity.ui.pages.favourites;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.Selectors;
import com.teamcity.ui.elements.ProjectElement;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.element;
import static com.codeborne.selenide.Selenide.elements;

public class ProjectsPage extends FavouritesPage {

    private static final String FAVOURITE_PROJECTS_URL = "/favorite/projects?mode=builds";

    private final SelenideElement header = element(Selectors.byClass("ProjectPageHeader__title--ih"));
    private final ElementsCollection subprojects = elements(Selectors.byClass("Subproject__container--Px"));

    public void waitUntilProjectsPageIsLoaded() {
        waitUntilPageIsLoaded();
        header.shouldBe(Condition.visible, Duration.ofSeconds(10));
    }

    public ProjectsPage open() {
        Selenide.open(FAVOURITE_PROJECTS_URL);
        waitUntilProjectsPageIsLoaded();
        return this;
    }

    public List<ProjectElement> getSubprojects(){
        return generatePageElements(subprojects, ProjectElement::new);
    }

}
