package com.vu.projects.pokemonmaster.pokemon;

import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Pokemon {
    @Id
    private String pokemonId;
    private String parentPokemonId;
    private String dexId;
    private String pokemonName;
    private Integer generation;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Type> types;
    @Embedded
    private EncounterRate encounterRate;
    @Embedded
    private Stats stats;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<QuickMove> quickMoves;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<CinematicMove> cinematicMoves;
    private Double kmBuddyDistance;
    private Integer stardustToUnlockThirdMove;
    private Integer candyToUnlockThirdMove;
    @ElementCollection
    private List<String> evolutionIds;
    private String rarity;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<EvolutionBranch> evolutionBranch;
    private String weatherConditionAffinities;
    private String form;
    @Transient
    private List<Link> links;
}
