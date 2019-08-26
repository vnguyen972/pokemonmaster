package com.vu.projects.pokemonmaster;

import com.vu.projects.pokemonmaster.data.POGOMaster;
import com.vu.projects.pokemonmaster.pokemon.CinematicMove;
import com.vu.projects.pokemonmaster.pokemon.Pokemon;
import com.vu.projects.pokemonmaster.pokemon.QuickMove;
import com.vu.projects.pokemonmaster.service.PokemonMasterService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log
public class PokemonMasterRunner implements CommandLineRunner {

    @Autowired
    private PokemonMasterService pokemonMasterService;

    @Override
    public void run(String... args) throws Exception {
        List<POGOMaster> masterList = (pokemonMasterService.parsingAllItems());
        List<Pokemon> pokemons = pokemonMasterService.buildPokemons(masterList);
        pokemonMasterService.savePokemons(pokemons);
        List<QuickMove> qMoves = pokemonMasterService.buildQuickMoves(masterList);
        log.info("QUICK MOVES COUNT " + qMoves.size());
        pokemonMasterService.saveQuickMoves(qMoves);
        List<CinematicMove> cMoves = pokemonMasterService.buildCinematicMoves(masterList);
        log.info("CINEMATIC MOVES COUNT = " + cMoves.size());
        pokemonMasterService.saveCinematicMoves(cMoves);

    }
}
