package com.teamcity.api.generators;

import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.NewProjectDescription;
import com.teamcity.api.models.User;
import com.teamcity.api.requests.unchecked.UncheckedProject;
import com.teamcity.api.requests.unchecked.UncheckedUser;
import com.teamcity.api.spec.Specifications;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TestData {
    private User user;
    private NewProjectDescription project;
    private BuildType buildType;

    public void delete() {
        var spec = Specifications.getSpec().superUserSpec();
        new UncheckedProject(spec).delete(project.getId());
        new UncheckedUser(spec).delete(user.getUsername());
    }
}