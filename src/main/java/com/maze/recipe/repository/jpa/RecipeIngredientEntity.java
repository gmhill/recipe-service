package com.maze.recipe.repository.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recipe_ingredients")
@Getter
@Setter
@NoArgsConstructor
public class RecipeIngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private RecipeEntity recipe;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "quantity_numerator", nullable = false)
    private Integer quantityNumerator;

    @Column(name = "quantity_denominator", nullable = false)
    private Integer quantityDenominator;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private String ingredient;
}
