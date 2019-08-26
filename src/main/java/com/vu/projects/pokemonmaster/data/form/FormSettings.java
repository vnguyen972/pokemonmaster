package com.vu.projects.pokemonmaster.data.form;

import lombok.Data;

import java.util.List;

@Data
public class FormSettings {
    private String pokemon;
    private List<Form> forms;
}
