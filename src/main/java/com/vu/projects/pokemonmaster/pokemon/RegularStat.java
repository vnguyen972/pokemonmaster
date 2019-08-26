package com.vu.projects.pokemonmaster.pokemon;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
public class RegularStat {
    @Id
    private String movementId;
    private Double power;
    private Double energyDelta;
    private Long durationMs;
    private String pokemonType;

    private BigDecimal dps;
    private BigDecimal eps;
    private BigDecimal damageEnergy;
}
