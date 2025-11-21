package src.screen.home;

import src.entity.Recipe;
import src.manager.RecipeManager;

import java.util.List;

public class HomePresenter {
    private final HomeScreen view;
    private final RecipeManager recipeManager;

    public HomePresenter(HomeScreen view, RecipeManager recipeManager) {
        this.view = view;
        this.recipeManager = recipeManager;
    }

    /** 앱 시작 시 기본 추천 불러오기 */
    public void loadInitialRecommendations() {
        List<Recipe> recipes = recipeManager.getRecommendationsPerCategory();
        view.showRecommendations(recipes);
    }

    /** 냉장고 재료 입력 후 호출 */
    public void onIngredientsSubmitted(String input) {
        List<Recipe> recs = recipeManager.getRecommendationsByIngredients(input);
        view.showRecommendations(recs);
        view.showInfo("추천 메뉴가 갱신되었습니다!");
    }
}
