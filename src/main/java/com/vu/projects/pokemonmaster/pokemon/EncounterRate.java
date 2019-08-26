package com.vu.projects.pokemonmaster.pokemon;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class EncounterRate {
    private Double baseCaptureRatePercent;
    private Double baseFleeRatePercent;
}
