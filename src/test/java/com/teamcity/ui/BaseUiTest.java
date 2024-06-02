package com.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.teamcity.BaseTest;
import com.teamcity.api.configs.Config;
import com.teamcity.api.models.User;
import com.teamcity.api.requests.checked.CheckedUser;
import com.teamcity.api.spec.Specifications;
import com.teamcity.ui.pages.LoginPage;
import org.testng.annotations.BeforeSuite;

public class BaseUiTest extends BaseTest {

    @BeforeSuite
    public void setupUiTest() {
        Configuration.browser = "firefox";
        Configuration.baseUrl = "http://" + Config.getProperty("host");
        Configuration.remote = Config.getProperty("remote");
        Configuration.reportsFolder = "target/surefire-reports";
        Configuration.downloadsFolder ="target/downloads";

        BrowserSettings.setup(Config.getProperty("browser"));
    }

    public void loginAsUser(User user){
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(user);
        new LoginPage().open().login(user);
    }

}
