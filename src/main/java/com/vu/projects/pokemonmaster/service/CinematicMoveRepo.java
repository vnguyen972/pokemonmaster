package com.vu.projects.pokemonmaster.service;

import com.vu.projects.pokemonmaster.pokemon.CinematicMove;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinematicMoveRepo extends JpaRepository<CinematicMove, String> {
}
