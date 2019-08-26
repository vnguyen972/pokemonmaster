package com.vu.projects.pokemonmaster.data.pokemon;

import lombok.Data;

@Data
public class Encounter {
    private Double baseCaptureRate;
    private Double baseFleeRate;
    private Double collisionRadiusM;
    private Double collisionHeightM;
    private String movementType;
    private Double movementTimer;
    private Double jumpTimeS;
    private Double attackTimerS;
    private Double attackProbability;
    private Double dodgeProbability;
    private Double dodgeDistance;
    private Double dodgeDurationS;
    private Double cameraDistance;
    private Double minPokemonActionFrequencyS;
    private Double maxPokemonActionFrequencyS;
}
