package com.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.Selectors;
import lombok.Getter;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

@Getter
public class StartUpAgentPage extends Page{

    private final SelenideElement authorizeAgentButton = element(Selectors.byAttribute("data-test-authorize-agent", "true"));
    private final SelenideElement authorizeAgentPopup = element(Selectors.byDataTest("ring-popup"));
    private final SelenideElement authorizePopupButton = element(By.xpath(".//button[@type='submit' and .//span/span[text()='Authorize']]"));

    public StartUpAgentPage open() {
        Selenide.open("/agent/1");
        return this;
    }

    public StartUpAgentPage setupTeamCityAgent() {
        waitUntilPageIsLoaded();
        authorizeAgentButton.click();
        authorizeAgentPopup.shouldBe(Condition.visible, Duration.ofSeconds(10));
        authorizePopupButton.click();
        return this;
    }

}
