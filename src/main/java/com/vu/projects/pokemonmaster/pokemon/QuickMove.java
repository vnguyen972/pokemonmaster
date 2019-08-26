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
@EqualsAndHashCode(callSuper = false)
@Entity
public class QuickMove extends ResourceSupport {
    @Id
    private @JsonProperty("id") String quickMoveid;
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private RegularStat regularStat;

    @OneToOne(cascade = CascadeType.ALL)
    private PvpStat pvpStat;
}
