package com.vu.projects.pokemonmaster.data.move;

import lombok.Data;

@Data
public class CombatMove {
    private String uniqueId;
    private String type;
    private Double power;
    private String vfxName;
    private Integer durationTurns;
    private Double energyDelta;
}
