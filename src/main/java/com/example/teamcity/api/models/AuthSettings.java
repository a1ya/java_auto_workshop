package com.example.teamcity.api.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthSettings {
    private boolean perProjectPermissions;
    private Modules modules;

    // Constructor

    public AuthSettings() {
        this.perProjectPermissions = true;
        this.modules = new Modules();
        List<Module> defaultModules = new ArrayList<>();
        defaultModules.add(createDefaultModule("Default", 3));
        defaultModules.add(createDefaultModule("Token-Auth", 0));
        defaultModules.add(createDefaultModule("HTTP-Basic", 0));
        this.modules.setModule(defaultModules);
    }

    // Getters and setters

    // Utility method to create default module
    private Module createDefaultModule(String name, int count) {
        Module module = new Module();
        module.setName(name);
        Properties properties = new Properties();
        properties.setCount(count);
        module.setProperties(properties);
        return module;
    }

}
