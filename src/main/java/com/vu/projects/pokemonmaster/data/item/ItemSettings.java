package com.vu.projects.pokemonmaster.data.item;

import lombok.Data;

@Data
public class ItemSettings {
    private String itemId;
    private String itemType;
    private String category;
    private Integer dropTrainerLevel;
    private Food food;
}
