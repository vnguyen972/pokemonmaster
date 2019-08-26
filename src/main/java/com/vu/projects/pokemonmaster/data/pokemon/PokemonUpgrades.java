package com.vu.projects.pokemonmaster.data.pokemon;

import lombok.Data;

import java.util.List;

@Data
public class PokemonUpgrades {
    private Integer upgradePerLevel;
    private Integer allowedLevelsAbovePlayer;
    private List<Integer> candyCost;
    private List<Integer> stardustCost;
    private Double shadowStardustMultiplier;
    private Double shadowCandyMultiplier;
    private Double purifiedStardustMultiplier;
    private Double purifiedCandyMultiplier;
}
