package com.vu.projects.pokemonmaster.pokemon;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Stats {
    private Integer baseStamina;
    private Integer baseAttack;
    private Integer baseDefense;
}
