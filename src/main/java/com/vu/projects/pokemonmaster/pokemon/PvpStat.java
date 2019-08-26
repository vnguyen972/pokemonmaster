package com.vu.projects.pokemonmaster.pokemon;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
public class PvpStat {
    @Id
    private String uniqueId;
    private Double power;
    private Double energyDelta;
    private Integer durationTurn;
    private String type;

    private BigDecimal dpt;
    private BigDecimal ept;
    private BigDecimal damageEnergy;
}
