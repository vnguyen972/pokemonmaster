package com.vu.projects.pokemonmaster.pokemon;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
public class EvolutionBranch extends ResourceSupport {
    @Id
    private String evolution;
    private String form;
    private Integer candyCost;
}
