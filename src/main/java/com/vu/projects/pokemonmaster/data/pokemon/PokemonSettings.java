package com.vu.projects.pokemonmaster.data.pokemon;

import lombok.Data;

import java.util.List;

@Data
public class PokemonSettings {
    private String pokemonId;
    private Double modelScale;
    private String type;
    private String type2;
    private Encounter encounter;
    private Stats stats;
    private List<String> quickMoves;
    private List<String> cinematicMoves;
    private List<String> evolutionIds;
    private Integer evolutionPips;
    private String familyId;
    private Integer candyToEvolve;
    private Double kmBuddyDistance;
    private List<Evolution> evolutionBranch;
    private ThirdMove thirdMove;
    private Boolean isTransferable;
    private Boolean isDeployable;
    private String form;
    private String rarity;
    private String parentPokemonId;
}
