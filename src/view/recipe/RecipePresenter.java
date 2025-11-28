package src.view.recipe;

import src.entity.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipePresenter {
    private final RecipeView view;

    public RecipePresenter(RecipeView view) {
        this.view = view;
    }

    private String[] buildNumberedSteps(String[] rawSteps) {
        if (rawSteps == null) return new String[0];

        List<String> result = new ArrayList<>();
        int index = 1;

        for (String str : rawSteps) {
            if (str == null) continue;

            String numbered = index + ". " + str;
            result.add(numbered);
            index++;
        }

        return result.toArray(new String[0]);
    }

    public void toggleFavorite(Recipe recipe) {
        recipe.setFavorite(!recipe.getFavorite());
    }

    public void showRecipe(Recipe recipe) {
        if (recipe == null) {
            view.updateRecipeView(
                    "",
                    null,
                    "",
                    "",
                    List.of(),
                    new String[0]
            );
            return;
        }

        String[] numberedSteps = buildNumberedSteps(recipe.getSteps());

        view.updateRecipeView(
                recipe.getName(),
                recipe.getImagePath(),
                recipe.getAmount(),
                recipe.getTime(),
                recipe.getDetails(),
                numberedSteps
        );
    }
}
