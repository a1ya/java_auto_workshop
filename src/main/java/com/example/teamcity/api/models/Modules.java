package com.example.teamcity.api.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Modules {
    private List<Module> module;
}

