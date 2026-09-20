package com.maze.recipe.controller;

import com.maze.recipe.dto.response.RecipeResponseDto;
import com.maze.recipe.exception.RecipeNotFoundException;
import com.maze.recipe.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<RecipeResponseDto> getRecipe(@PathVariable long id) {
        try {
            return ResponseEntity.ok(recipeService.readRecipe(id));
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
