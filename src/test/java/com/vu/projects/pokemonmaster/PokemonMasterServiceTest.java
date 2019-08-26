package com.vu.projects.pokemonmaster;

import com.vu.projects.pokemonmaster.data.ItemTemplates;
import com.vu.projects.pokemonmaster.data.POGOMaster;
import com.vu.projects.pokemonmaster.pokemon.CinematicMove;
import com.vu.projects.pokemonmaster.pokemon.Pokemon;
import com.vu.projects.pokemonmaster.pokemon.QuickMove;
import com.vu.projects.pokemonmaster.service.PokemonMasterService;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Log
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class PokemonMasterServiceTest extends AbstractBenchMark {

    private static PokemonMasterService pokemonMasterService;

    @Autowired
    public void setPokemonMasterService(PokemonMasterService service) {
        PokemonMasterServiceTest.pokemonMasterService = service;
    }

    @Test
    public void testSpringContextLoaded() {
        assertThat(pokemonMasterService).isNotNull();
    }

    @Test
    public void testReadJsonFromLink() {
        ResponseEntity<String> resp = pokemonMasterService.masterData.get();
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Benchmark
    public void testRetrievePokes() throws IOException {
        List<POGOMaster> pokes = pokemonMasterService.parsingAllItems();
        assertThat(pokes).isNotNull().isNotEmpty();
        log.info("SIZE "+pokes.get(0).getItemTemplates().size());
//        for (ItemTemplates t : pokes.get(0).getItemTemplates()) {
//            if (t.getSmeargleMovesSettings() != null) {
//                log.info("DATA: " + t.getSmeargleMovesSettings());
//            }
//        }
    }

    @Test
    public void testBuildPokemons() throws IOException {
        List<Pokemon> pokes = pokemonMasterService.buildPokemons(pokemonMasterService.parsingAllItems());
        assertThat(pokes).isNotNull().isNotEmpty();
        log.info("SIZE = " + pokes.size());
        log.info("POKE = " + pokes.get(0));
    }

    @Test
    public void testBuildQuickMoves() throws IOException {
        List<QuickMove> qMoves = pokemonMasterService.buildQuickMoves(pokemonMasterService.parsingAllItems());
        assertThat(qMoves).isNotNull().isNotEmpty();
        log.info("QUICK MOVES: " + qMoves);
    }

    @Test
    public void testBuildCinematicMoves() throws IOException {
        List<CinematicMove> cMoves = pokemonMasterService.buildCinematicMoves(pokemonMasterService.parsingAllItems());
        assertThat(cMoves).isNotNull().isNotEmpty();
        log.info("CINEMATIC MOVES: " + cMoves);
    }

    @Test
    public void testLoadSmeargleMoves() throws IOException {
        List<String> list = pokemonMasterService.movesFromSmeargleMoves.apply(
                pokemonMasterService.parsingAllItems().get(0).getItemTemplates(),"cinematic").get();
        assertThat(list).isNotNull().isNotEmpty();
        //list.stream().forEach(s -> log.info(s));
    }

    @Test
    public void testPokeGen() {
        Optional<PokemonMasterService.PokeGen> g = pokemonMasterService.pokeGens.stream()
                .filter(pg -> pg.range.contains(Integer.valueOf("211")))
                .findFirst();
        assertThat(g).isNotEmpty();
        log.info("GEN = " + g.get());
    }

    @Test
    public void testBuildName() {
        pokemonMasterService.buildName("NAME_VU_NGUYEN_TEST", 1, 1);
    }
}
