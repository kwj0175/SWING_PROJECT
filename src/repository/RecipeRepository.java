package repository;

import entity.Recipe;

import java.util.List;

public interface RecipeRepository {
    List<Recipe> loadAll();
}
