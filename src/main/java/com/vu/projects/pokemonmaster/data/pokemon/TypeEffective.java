package com.vu.projects.pokemonmaster.data.pokemon;

import lombok.Data;

import java.util.List;

@Data
public class TypeEffective {
    private List<Float> attackScalar;
    private String attackType;
}
