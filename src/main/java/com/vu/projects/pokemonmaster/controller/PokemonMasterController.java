package com.vu.projects.pokemonmaster.controller;

import com.vu.projects.pokemonmaster.exceptions.FormNotFoundException;
import com.vu.projects.pokemonmaster.pokemon.*;
import com.vu.projects.pokemonmaster.service.PokemonMasterService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Log
@RestController
@RequestMapping(path = "/pokemon")
public class PokemonMasterController {

    @Autowired
    private PokemonMasterService pokemonMasterService;

    @RequestMapping(path = "/all")
    public @ResponseBody List<Pokemon> getAllPokemon() {
        List<Pokemon> pokemons = pokemonMasterService.listAllPokemons();
        pokemons.forEach(p -> buildLinks(p));
        return pokemons;
    }

    @GetMapping(path="/id/{id}")
    public ResponseEntity<?> findPokemonById(@PathVariable String id) {
        Pokemon pokemon = null;
        try {
            pokemon = pokemonMasterService.getPokemon(id);
            buildLinks(pokemon);
            return new ResponseEntity<>(pokemon, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("{\"ERROR\": \"Unable to find any pokemon with ID = " + id + "\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path="/dex/{dexId}")
    public ResponseEntity<?> findByDexId(@PathVariable String dexId) {
        List<Pokemon> pokemons = pokemonMasterService.getPokemonByDexId(dexId);
        if (pokemons.isEmpty()) {
            return new ResponseEntity<>("{\"ERROR\": \"Unable to find any pokemon with DEXID = " + dexId + "\"}",
                    HttpStatus.NOT_FOUND);
        } else {
            pokemons.forEach(p->buildLinks(p));
        }
        return new ResponseEntity<>(pokemons, HttpStatus.OK);
    }

    @GetMapping(path="/type/{type}")
    public ResponseEntity<?> findByType(@PathVariable String type) {
        Type t = new Type();
        String s = ("POKEMON_TYPE_" + type).toUpperCase();
        t.setTypeId(s);
        t.setName(type.substring(0,1).toUpperCase()+type.substring(1,type.length()));
        log.info("TYPE = " + t);
        List<Pokemon> list = pokemonMasterService.getPokemonByType(t);
        if (list.isEmpty()) {
            return new ResponseEntity<>("{\"ERROR\": \"Unable to find any pokemon with TYPE = " + type + "\"}",
                    HttpStatus.NOT_FOUND);
        } else {
            list.forEach(p->buildLinks(p));
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(path = "/quickmoves/{move}")
    public ResponseEntity<?> findByQuickMove(@PathVariable String move) {
        QuickMove quickMove = new QuickMove();
        quickMove.setQuickMoveid(move);
        quickMove.setName(pokemonMasterService.buildName(move, 0, 1));
        List<Pokemon> list = pokemonMasterService.getPokemonByQuickMove(quickMove);
        if (list.isEmpty()) {
            return new ResponseEntity<>("{\"ERROR\": \"Unable to find any pokemon with MOVE = " + move + "\"}",
                    HttpStatus.NOT_FOUND);
        } else {
            list.forEach(p->buildLinks(p));
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(path = "/chargedmoves/{move}")
    public  ResponseEntity<?> findByCinematicMove(@PathVariable String move) {
        CinematicMove cinematicMove = new CinematicMove();
        cinematicMove.setCinematicMoveid(move);
        cinematicMove.setName(pokemonMasterService.buildName(move, 0, 0));
        List<Pokemon> list = pokemonMasterService.getPokemonByCinematicMove(cinematicMove);
        if (list.isEmpty()) {
            return new ResponseEntity<>("{\"ERROR\": \"Unable to find any pokemon with MOVE = " + move + "\"}",
                    HttpStatus.NOT_FOUND);
        } else {
            list.forEach(p->buildLinks(p));
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(path = "/form/{form}")
    public ResponseEntity<?> findByForm(@PathVariable String form) {
        String theForm = "%" + form.toUpperCase();
        List<Pokemon> list = pokemonMasterService.getPokemonByForm(theForm);
        if (list.isEmpty()) {
            return new ResponseEntity<>("{\"ERROR\": \"Unable to find any pokemon with FORM = " + form + "\"}",
                    HttpStatus.NOT_FOUND);
        } else {
            list.forEach(p->buildLinks(p));
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    private void buildLinks(Pokemon pokemon) {
        // Build Evolution Links
        pokemon.getEvolutionBranch().stream()
                .filter(eb -> eb.getLinks().isEmpty())
                .forEach(eb -> eb.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PokemonMasterController.class).
                        findPokemonById(eb.getEvolution())).withSelfRel()));

        // Build Type links
        pokemon.getTypes().stream().filter(t -> t.getLinks().isEmpty())
                .forEach(t->t.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PokemonMasterController.class)
                        .findByType(t.getName().toLowerCase())).withSelfRel()));

        // Build Quick Move Links
        pokemon.getQuickMoves().stream().filter(qm -> qm.getLinks().isEmpty())
                .forEach(qm -> qm.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PokemonMasterController.class).
                        findByQuickMove(qm.getQuickMoveid())).withSelfRel()));

        // Build Cinematic Move Links
        pokemon.getCinematicMoves().stream().filter(cm -> cm.getLinks().isEmpty())
                .forEach(cm -> cm.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PokemonMasterController.class).
                        findByCinematicMove(cm.getCinematicMoveid())).withSelfRel()));

        // Build Self and Parent Link
        List<Link> list = new ArrayList<>();
        if (pokemon.getParentPokemonId() != null) {
            list.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PokemonMasterController.class)
                    .findPokemonById(pokemon.getParentPokemonId())).withRel("parentPokemon"));
        }
        list.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PokemonMasterController.class)
                .findPokemonById(pokemon.getPokemonId())).withSelfRel());
        pokemon.setLinks(list);
    }
}
