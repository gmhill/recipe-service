package com.maze.recipe.controller;

import com.maze.recipe.dto.response.RecipeResponseDto;
import com.maze.recipe.exception.RecipeNotFoundException;
import com.maze.recipe.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService recipeService;

    @Test
    void getAllRecipesReturnsOkWithAllRecipes() throws Exception {
        RecipeResponseDto recipe = new RecipeResponseDto(1L, "Sifted Flour Recipe", List.of(), List.of());
        when(recipeService.readAllRecipes()).thenReturn(List.of(recipe));

        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].recipeName").value("Sifted Flour Recipe"));
    }

    @Test
    void getRecipeByIdReturnsOkWhenRecipeExists() throws Exception {
        RecipeResponseDto recipe = new RecipeResponseDto(1L, "Sifted Flour Recipe", List.of(), List.of());
        when(recipeService.readRecipe(eq(1L))).thenReturn(recipe);

        mockMvc.perform(get("/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.recipeName").value("Sifted Flour Recipe"));
    }

    @Test
    void getRecipeByIdReturnsNotFoundWhenRecipeIsMissing() throws Exception {
        when(recipeService.readRecipe(eq(99L)))
                .thenThrow(new RecipeNotFoundException("Unable to find recipe with id 99"));

        mockMvc.perform(get("/recipes/99"))
                .andExpect(status().isNotFound());
    }
}
