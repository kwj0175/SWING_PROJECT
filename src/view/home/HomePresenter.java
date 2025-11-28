package src.view.home;

import src.entity.Recipe;
import src.service.RecipeService;

import java.util.List;

public class HomePresenter {
    private final HomeView view;
    private final RecipeService recipeService;

    public HomePresenter(HomeView view, RecipeService recipeService) {
        this.view = view;
        this.recipeService = recipeService;
    }

    /** 앱 시작 시 기본 추천 불러오기 */
    public void loadInitialRecommendations() {
        List<Recipe> recipes = recipeService.getRecommendationsPerCategory();
        view.showRecommendations(recipes);
    }

    /** 냉장고 재료 입력 후 호출 */
    public void onIngredientsSubmitted(String input) {
        List<Recipe> recs = recipeService.getRecommendationsByIngredients(input);
        view.showRecommendations(recs);
        view.showInfo("추천 메뉴가 갱신되었습니다!");
    }
}
