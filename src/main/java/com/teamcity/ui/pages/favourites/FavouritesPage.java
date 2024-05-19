package com.teamcity.ui.pages.favourites;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.Selectors;
import com.teamcity.ui.pages.Page;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

public class FavouritesPage extends Page {
    private SelenideElement header = element(Selectors.byClass("ProjectPageHeader__title--ih"));

    public void waitUntilFavouritePageIsLoaded() {
        waitUntilPageIsLoaded();
        header.shouldBe(Condition.visible, Duration.ofSeconds(10));
    }
}
