package com.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.teamcity.api.generators.TestData;
import com.teamcity.ui.pages.StartUpAgentPage;
import org.testng.annotations.Test;

import java.time.Duration;

public class SetupAgentTest extends BaseUiTest{

    @Test
    public void startUpAgentTest() {
        TestData testData = testDataStorage.addTestData();
        loginAsUser(testData.getUser());

        new StartUpAgentPage()
                .open()
                .setupTeamCityAgent()
                .getAuthorizeAgentButton().shouldHave(Condition.text("Unauthorize"), Duration.ofSeconds(10))
        ;
    }
}
