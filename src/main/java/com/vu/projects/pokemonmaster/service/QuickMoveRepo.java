package com.vu.projects.pokemonmaster.service;

import com.vu.projects.pokemonmaster.pokemon.QuickMove;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuickMoveRepo extends JpaRepository<QuickMove, String> {
}
