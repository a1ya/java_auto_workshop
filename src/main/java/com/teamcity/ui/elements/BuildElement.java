package com.teamcity.ui.elements;

import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.Selectors;
import lombok.Getter;

@Getter
public class BuildElement extends PageElement{

    private final SelenideElement header;
    private final SelenideElement runButton;

    public BuildElement(SelenideElement element) {
        super(element);
        this.header = findElement(Selectors.byDataTest("ring-link"));
        this.runButton = findElement(Selectors.byDataTest("run-build"));
    }

}
