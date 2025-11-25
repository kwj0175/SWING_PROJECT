package src.repository;

import src.entity.Recipe;

import java.util.List;

public interface RecipeRepository {
    List<Recipe> loadAll();
}
