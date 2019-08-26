package com.vu.projects.pokemonmaster.data.weather;

import lombok.Data;

import java.util.List;

@Data
public class WeatherAffinities {
    private String weatherCondition;
    private List<String> pokemonType;
}
