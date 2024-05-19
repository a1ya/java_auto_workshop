package com.teamcity.ui.pages.favourites;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.teamcity.ui.Selectors;
import com.teamcity.ui.elements.ProjectElement;

import java.util.List;

import static com.codeborne.selenide.Selenide.elements;

public class ProjectsPage extends FavouritesPage {

    private static final String FAVOURITE_PROJECTS_URL = "/favorite/projects?mode=builds";


    private ElementsCollection subprojects = elements(Selectors.byClass("Subproject__container--Px"));

    public ProjectsPage open() {
        Selenide.open(FAVOURITE_PROJECTS_URL);
        waitUntilFavouritePageIsLoaded();
        return this;
    }

    public List<ProjectElement> getSubprojects(){
        return generatePageElements(subprojects, ProjectElement::new);
    }

}
