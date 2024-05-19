package com.teamcity.api.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Module {
    private String name;
    private Properties properties;
}

