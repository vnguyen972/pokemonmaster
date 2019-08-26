package com.vu.projects.pokemonmaster.data.move;

import lombok.Data;

@Data
public class MoveSettings {
    private String movementId;
    private Integer animationId;
    private String pokemonType;
    private Double power;
    private Double accuracyChance;
    private Double staminaLossScalar;
    private Integer trainerLevelMin;
    private Integer trainerLevelMax;
    private String vfxName;
    private Long durationMs;
    private Integer damageWindowStartMs;
    private Integer damageWindowEndMs;
    private Double energyDelta;
}
