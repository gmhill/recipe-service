package com.maze.recipe.controller;

import tools.jackson.databind.ObjectMapper;
import com.maze.recipe.dto.request.CreateIngredientDto;
import com.maze.recipe.dto.request.CreateInstructionDto;
import com.maze.recipe.dto.request.CreateRecipeDto;
import com.maze.recipe.dto.response.IngredientResponseDto;
import com.maze.recipe.dto.response.InstructionResponseDto;
import com.maze.recipe.dto.response.RecipeResponseDto;
import com.maze.recipe.exception.InvalidIdException;
import com.maze.recipe.exception.InvalidQuantityException;
import com.maze.recipe.exception.RecipeNotFoundException;
import com.maze.recipe.service.RecipeService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    void getAllRecipesReturnsAllRecipes() throws Exception {
        // Given
        RecipeResponseDto recipe = getRecipeResponseDto();
        when(recipeService.readAllRecipes()).thenReturn(List.of(recipe));

        // When/Then
        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(recipe.id()))
                .andExpect(jsonPath("$[0].recipeName").value(recipe.recipeName()));
    }

    @Test
    void getRecipeByIdReturnsRecipeWhenFound() throws Exception {
        // Given
        RecipeResponseDto recipe = getRecipeResponseDto();
        when(recipeService.readRecipe(1L)).thenReturn(recipe);

        // When/Then
        mockMvc.perform(get("/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipe.id()))
                .andExpect(jsonPath("$.recipeName").value(recipe.recipeName()));
    }

    @Test
    void getRecipeByIdReturnsNotFoundWhenMissing() throws Exception {
        // Given
        when(recipeService.readRecipe(99L)).thenThrow(new RecipeNotFoundException("Unable to find recipe with id 99"));

        // When/Then
        mockMvc.perform(get("/recipes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Unable to find recipe with id 99"));
    }

    @Test
    void getRecipeByIdReturnsBadRequestForInvalidId() throws Exception {
        // Given
        when(recipeService.readRecipe(-1L)).thenThrow(new InvalidIdException("Provided ID must be greater than 0"));

        // When/Then
        mockMvc.perform(get("/recipes/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Provided ID must be greater than 0"));
    }

    @Test
    void createRecipeReturnsCreatedWithLocationAndBody() throws Exception {
        // Given
        CreateRecipeDto createRecipeDto = getCreateRecipeDto();
        RecipeResponseDto recipe = getRecipeResponseDto();
        when(recipeService.createRecipe(any(CreateRecipeDto.class))).thenReturn(1L);
        when(recipeService.readRecipe(1L)).thenReturn(recipe);

        // When/Then
        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRecipeDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/recipes/" + recipe.id()))
                .andExpect(jsonPath("$.id").value(recipe.id()))
                .andExpect(jsonPath("$.recipeName").value(recipe.recipeName()));
    }

    @Test
    void createRecipeReturnsBadRequestForInvalidPayload() throws Exception {
        // Given
        CreateRecipeDto createRecipeDto = getCreateRecipeDto();
        when(recipeService.createRecipe(any(CreateRecipeDto.class)))
                .thenThrow(new ConstraintViolationException(Set.of()));

        // When/Then
        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRecipeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createRecipeReturnsBadRequestForInvalidQuantity() throws Exception {
        // Given
        CreateRecipeDto createRecipeDto = getCreateRecipeDto();
        when(recipeService.createRecipe(any(CreateRecipeDto.class)))
                .thenThrow(new InvalidQuantityException("Invalid quantity format: abc"));

        // When/Then
        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRecipeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid quantity format: abc"));
    }

    @Test
    void updateRecipeReturnsOkWithUpdatedBody() throws Exception {
        // Given
        CreateRecipeDto updateRecipeDto = getCreateRecipeDto();
        RecipeResponseDto recipe = getRecipeResponseDto();
        when(recipeService.updateRecipe(eq(1L), any(CreateRecipeDto.class))).thenReturn(recipe);

        // When/Then
        mockMvc.perform(put("/recipes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRecipeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipe.id()))
                .andExpect(jsonPath("$.recipeName").value(recipe.recipeName()));
    }

    @Test
    void updateRecipeReturnsNotFoundWhenMissing() throws Exception {
        // Given
        CreateRecipeDto updateRecipeDto = getCreateRecipeDto();
        when(recipeService.updateRecipe(eq(99L), any(CreateRecipeDto.class)))
                .thenThrow(new RecipeNotFoundException("Unable to find recipe with id 99"));

        // When/Then
        mockMvc.perform(put("/recipes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRecipeDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Unable to find recipe with id 99"));
    }

    @Test
    void updateRecipeReturnsBadRequestForInvalidId() throws Exception {
        // Given
        CreateRecipeDto updateRecipeDto = getCreateRecipeDto();
        when(recipeService.updateRecipe(eq(-1L), any(CreateRecipeDto.class)))
                .thenThrow(new InvalidIdException("Provided ID must be greater than 0"));

        // When/Then
        mockMvc.perform(put("/recipes/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRecipeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Provided ID must be greater than 0"));
    }

    @Test
    void updateRecipeReturnsBadRequestForInvalidPayload() throws Exception {
        // Given
        CreateRecipeDto updateRecipeDto = getCreateRecipeDto();
        when(recipeService.updateRecipe(eq(1L), any(CreateRecipeDto.class)))
                .thenThrow(new ConstraintViolationException(Set.of()));

        // When/Then
        mockMvc.perform(put("/recipes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRecipeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void deleteRecipeReturnsNoContentWhenDeleted() throws Exception {
        // Given
        RecipeResponseDto recipe = getRecipeResponseDto();
        when(recipeService.deleteRecipe(1L)).thenReturn(recipe);

        // When/Then
        mockMvc.perform(delete("/recipes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteRecipeReturnsNotFoundWhenMissing() throws Exception {
        // Given
        when(recipeService.deleteRecipe(99L)).thenThrow(new RecipeNotFoundException("Unable to find recipe with id 99"));

        // When/Then
        mockMvc.perform(delete("/recipes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Unable to find recipe with id 99"));
    }

    @Test
    void deleteRecipeReturnsBadRequestForInvalidId() throws Exception {
        // Given
        when(recipeService.deleteRecipe(-1L)).thenThrow(new InvalidIdException("Provided ID must be greater than 0"));

        // When/Then
        mockMvc.perform(delete("/recipes/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Provided ID must be greater than 0"));
    }

    private RecipeResponseDto getRecipeResponseDto() {
        IngredientResponseDto ingredient = new IngredientResponseDto("1", "cup", "flour");
        InstructionResponseDto instruction = new InstructionResponseDto("Sift it.");
        return new RecipeResponseDto(1L, "Sifted Flour Recipe", List.of(ingredient), List.of(instruction));
    }

    private CreateRecipeDto getCreateRecipeDto() {
        CreateIngredientDto ingredient = new CreateIngredientDto("1", "cup", "flour");
        CreateInstructionDto instruction = new CreateInstructionDto("Sift it.");
        return new CreateRecipeDto("Sifted Flour Recipe", List.of(ingredient), List.of(instruction));
    }
}
