package com.maze.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maze.recipe.dto.request.CreateIngredientDto;
import com.maze.recipe.dto.request.CreateInstructionDto;
import com.maze.recipe.dto.request.CreateRecipeDto;
import com.maze.recipe.dto.response.IngredientResponseDto;
import com.maze.recipe.dto.response.InstructionResponseDto;
import com.maze.recipe.dto.response.RecipeResponseDto;
import com.maze.recipe.exception.InvalidIdException;
import com.maze.recipe.exception.RecipeNotFoundException;
import com.maze.recipe.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecipeService recipeService;

    @Test
    void getAllRecipesReturnsOkWithList() throws Exception {
        when(recipeService.readAllRecipes()).thenReturn(List.of(sampleRecipe()));

        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].recipeName").value("Sifted Flour Recipe"));
    }

    @Test
    void getRecipeByIdReturnsOk() throws Exception {
        when(recipeService.readRecipe(1L)).thenReturn(sampleRecipe());

        mockMvc.perform(get("/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.recipeName").value("Sifted Flour Recipe"));
    }

    @Test
    void getRecipeByIdReturnsNotFoundWhenMissing() throws Exception {
        when(recipeService.readRecipe(99L))
                .thenThrow(new RecipeNotFoundException("Unable to find recipe with id 99"));

        mockMvc.perform(get("/recipes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getRecipeByIdReturnsBadRequestForInvalidId() throws Exception {
        when(recipeService.readRecipe(-1L))
                .thenThrow(new InvalidIdException("Provided ID must be greater than 0"));

        mockMvc.perform(get("/recipes/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRecipeReturnsCreated() throws Exception {
        CreateRecipeDto createDto = new CreateRecipeDto(
                "Sifted Flour Recipe",
                List.of(new CreateIngredientDto("1", "cup", "flour")),
                List.of(new CreateInstructionDto("Sift it."))
        );

        when(recipeService.createRecipe(any())).thenReturn(1L);
        when(recipeService.readRecipe(1L)).thenReturn(sampleRecipe());

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/recipes/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteRecipeReturnsNoContent() throws Exception {
        when(recipeService.deleteRecipe(1L)).thenReturn(sampleRecipe());

        mockMvc.perform(delete("/recipes/1"))
                .andExpect(status().isNoContent());

        verify(recipeService).deleteRecipe(1L);
    }

    @Test
    void deleteRecipeReturnsNotFoundWhenMissing() throws Exception {
        when(recipeService.deleteRecipe(99L))
                .thenThrow(new RecipeNotFoundException("Unable to find recipe with id 99"));

        mockMvc.perform(delete("/recipes/99"))
                .andExpect(status().isNotFound());
    }

    private RecipeResponseDto sampleRecipe() {
        return new RecipeResponseDto(
                1L,
                "Sifted Flour Recipe",
                List.of(new IngredientResponseDto("1", "cup", "flour")),
                List.of(new InstructionResponseDto("Sift it."))
        );
    }
}
