package com.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.Selectors;
import com.teamcity.ui.elements.PageElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.codeborne.selenide.Selenide.element;

public abstract class Page {

    private final SelenideElement submitButton = element(Selectors.byType("submit"));

    private final SelenideElement savingWaitingMarker = element(Selectors.byId("saving"));
    private final SelenideElement pageWaitingMarker = element(Selectors.byDataTest("ring-loader"));

    public void submit() {
        submitButton.click();
        waitUntilDataIsSaved();
    }

    public void waitUntilPageIsLoaded() {
        pageWaitingMarker.shouldNotBe(Condition.not(Condition.visible), Duration.ofSeconds(60));
    }

    public void waitUntilDataIsSaved() {
        savingWaitingMarker.shouldBe(Condition.not(Condition.visible), Duration.ofSeconds(30));
    }

    public <T extends PageElement> List<T> generatePageElements(ElementsCollection collection,
                                                                 Function<SelenideElement, T> creator) {
        var elements = new ArrayList<T>();
        collection.forEach(webElement -> elements.add(creator.apply(webElement)));
        return elements;
    }

}
