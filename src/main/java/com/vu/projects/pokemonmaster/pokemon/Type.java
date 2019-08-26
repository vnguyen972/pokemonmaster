package com.vu.projects.pokemonmaster.pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Type extends ResourceSupport {
    @Id
    private @JsonProperty("id") String typeId;
    private String name;
}
