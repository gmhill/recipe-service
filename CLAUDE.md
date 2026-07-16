# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

- Build: `./gradlew build`
- Run all tests: `./gradlew test`
- Run a single test class: `./gradlew test --tests "com.maze.recipe.service.RecipeServiceTest"`
- Run a single test method: `./gradlew test --tests "com.maze.recipe.service.RecipeServiceTest.methodName"`
- Run the app locally: `./gradlew bootRun`
- Clean build (matches CI): `./gradlew clean build`

## Architecture

This is a Spring Boot service for storing and retrieving recipes, currently early-stage: there is no REST controller layer yet, and persistence is in-memory only (see below). See `README.md` for the intended system/database diagrams (`assets/SystemDiagram.png`, `assets/DatabaseDesign.png`).

Package layout under `com.maze.recipe`:
- `entity` — domain model (`Recipe`, `RecipeIngredient`, `RecipeInstruction`), validated with Jakarta Bean Validation annotations directly on the entities.
- `dto.request` / `dto.response` — separate DTOs for inbound creation payloads (`CreateRecipeDto`, `CreateIngredientDto`, `CreateInstructionDto`) and outbound responses (`RecipeResponseDto`, `IngredientResponseDto`, `InstructionResponseDto`). Entities are never returned directly from the service layer.
- `mapper.RecipeMapper` — MapStruct interface that converts between entities and DTOs. Notably handles ingredient quantities: request DTOs carry quantity as a `String` which `RecipeMapper` parses into a numerator/denominator pair via `commons-math3`'s `Fraction` (`parseQuantity`), and formats it back to a string on the way out (`formatQuantity`). Any change to how quantities are represented needs to update both directions here.
- `repository` — `RecipeRepository` interface with a single `InMemoryRecipeRepository` implementation (`HashMap` + `AtomicLong` id counter). `create()` cascades the generated recipe `id` down into each `RecipeIngredient`/`RecipeInstruction` before storing. There is no JPA/database-backed implementation yet despite `spring-boot-starter-data-jpa` being a dependency.
- `service.RecipeService` — the only entry point into business logic (create/read/delete recipes). It re-validates the mapped `Recipe` entity itself via injected `jakarta.validation.Validator` rather than relying solely on annotation-driven validation at a controller boundary (there is no controller yet). All ID lookups go through `validateId`, which requires `id > 0` and throws `InvalidIdException` otherwise.
- `exception` — `InvalidIdException`, `InvalidQuantityException`, `RecipeNotFoundException`. No global exception handler (`@ControllerAdvice`) exists yet.

### Equality semantics

Entities use Lombok `@EqualsAndHashCode` but exclude identity/positional fields (`Recipe.id`; `RecipeIngredient`/`RecipeInstruction`'s `recipeId` and `orderIndex`) from equality — two ingredients/instructions/recipes are considered equal based on content, not on where they live. Keep this in mind when writing tests or comparing collections.