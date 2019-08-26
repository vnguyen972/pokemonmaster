package com.vu.projects.pokemonmaster.pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class CinematicMove extends ResourceSupport {
    @Id
    private @JsonProperty("id") String cinematicMoveid;
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private RegularStat regularStat;

    @OneToOne(cascade = CascadeType.ALL)
    private PvpStat pvpStat;

}
