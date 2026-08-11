package com.maze.recipe.controller;

import com.maze.recipe.dto.request.CreateRecipeDto;
import com.maze.recipe.dto.response.RecipeResponseDto;
import com.maze.recipe.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeResponseDto> getAllRecipes() {
        return recipeService.readAllRecipes();
    }

    @GetMapping("/{id}")
    public RecipeResponseDto getRecipe(@PathVariable long id) {
        return recipeService.readRecipe(id);
    }

    @PostMapping
    public ResponseEntity<RecipeResponseDto> createRecipe(@RequestBody CreateRecipeDto createRecipeDto) {
        long id = recipeService.createRecipe(createRecipeDto);
        RecipeResponseDto created = recipeService.readRecipe(id);
        return ResponseEntity.created(URI.create("/recipes/" + id)).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
