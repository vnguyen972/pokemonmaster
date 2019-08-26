package com.vu.projects.pokemonmaster.data.item;

import lombok.Data;

import java.util.List;

@Data
public class Food {
    private List<String> itemEffect;
    private List<Float> itemEffectPercent;
    private Double berryMultiplier;
}
