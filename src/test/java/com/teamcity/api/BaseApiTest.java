package com.teamcity.api;

import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.api.models.AuthSettings;
import com.teamcity.api.requests.CheckedRequests;
import com.teamcity.api.requests.UncheckedRequests;
import com.teamcity.api.requests.unchecked.AuthSettingsRequest;
import com.teamcity.api.spec.Specifications;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseApiTest extends BaseTest {
    public TestDataStorage testDataStorage;

    public CheckedRequests checkedWithSuperUser
            = new CheckedRequests(Specifications.getSpec().superUserSpec());

    public UncheckedRequests uncheckedWithSuperUser
            = new UncheckedRequests(Specifications.getSpec().superUserSpec());

    @BeforeMethod
    public void setupTest() {
        testDataStorage = TestDataStorage.getStorage();
        new AuthSettingsRequest(Specifications.getSpec().superUserSpec()).update(new AuthSettings());
    }

    @AfterMethod
    public void cleanTest() {
        testDataStorage.delete();
    }
}