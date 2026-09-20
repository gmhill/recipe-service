package com.maze.recipe.controller;

import com.maze.recipe.dto.request.CreateRecipeDto;
import com.maze.recipe.dto.response.RecipeResponseDto;
import com.maze.recipe.exception.RecipeNotFoundException;
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
    public ResponseEntity<List<RecipeResponseDto>> getAllRecipes() {
        return ResponseEntity.ok(recipeService.readAllRecipes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDto> getRecipe(@PathVariable long id) {
        try {
            return ResponseEntity.ok(recipeService.readRecipe(id));
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<RecipeResponseDto> createRecipe(@RequestBody CreateRecipeDto createRecipeDto) {
        try {
            long id = recipeService.createRecipe(createRecipeDto);
            RecipeResponseDto created = recipeService.readRecipe(id);
            return ResponseEntity.created(URI.create("/recipes/" + id)).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable long id) {
        try {
            recipeService.deleteRecipe(id);
            return ResponseEntity.noContent().build();
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
