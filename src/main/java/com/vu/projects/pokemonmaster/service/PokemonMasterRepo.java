package com.vu.projects.pokemonmaster.service;

import com.vu.projects.pokemonmaster.pokemon.CinematicMove;
import com.vu.projects.pokemonmaster.pokemon.Pokemon;
import com.vu.projects.pokemonmaster.pokemon.QuickMove;
import com.vu.projects.pokemonmaster.pokemon.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PokemonMasterRepo extends JpaRepository<Pokemon, String> {
    @Query("SELECT p FROM Pokemon p WHERE p.dexId = :dexid")
    List<Pokemon> findByDexId(@Param("dexid") String dexid);

    @Query("SELECT p FROM Pokemon p WHERE :type MEMBER OF p.types")
    List<Pokemon> findByType(@Param("type") Type type);

    @Query("SELECT p FROM Pokemon p WHERE :quickMove MEMBER OF p.quickMoves")
    List<Pokemon> findByQuickMove(@Param("quickMove") QuickMove quickMove);

    @Query("SELECT p FROM Pokemon p WHERE :cinematicMove MEMBER OF p.cinematicMoves")
    List<Pokemon> findByCinematicMove(@Param("cinematicMove") CinematicMove cinematicMove);

    @Query("SELECT p FROM Pokemon p WHERE p.form like :form")
    List<Pokemon> findByForm(@Param("form") String form);
}
