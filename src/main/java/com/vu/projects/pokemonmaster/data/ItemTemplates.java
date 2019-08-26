package com.vu.projects.pokemonmaster.data;

import com.vu.projects.pokemonmaster.data.form.FormSettings;
import com.vu.projects.pokemonmaster.data.item.ItemSettings;
import com.vu.projects.pokemonmaster.data.move.CombatMove;
import com.vu.projects.pokemonmaster.data.move.MoveSettings;
import com.vu.projects.pokemonmaster.data.move.SmeargleMovesSettings;
import com.vu.projects.pokemonmaster.data.pokemon.*;
import com.vu.projects.pokemonmaster.data.weather.WeatherAffinities;
import lombok.Data;

@Data
public class ItemTemplates {
    private String templateId;
    private PokemonSettings pokemonSettings;
    private FormSettings formSettings;
    private CombatMove combatMove;
    private MoveSettings moveSettings;
    private ItemSettings itemSettings;
    private LuckyPokemonSettings luckyPokemonSettings;
    private TypeEffective typeEffective;
    private PokemonUpgrades pokemonUpgrades;
    private SmeargleMovesSettings smeargleMovesSettings;
    private GenderSettings genderSettings;
    private WeatherAffinities weatherAffinities;
}
