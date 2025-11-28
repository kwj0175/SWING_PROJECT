package src.entity;

import java.util.List;
import java.util.Set;

public class Recipe {

    private final String name;
    private final String title;
    private final FoodCategory category;
    private final List<String> details;
    private final String[] steps;
    private final String amount;
    private final String time;
    private final String imagePath;
    private boolean favorite;

    public Recipe(String name, String title,
                  FoodCategory category, List<String> details,
                  String[] steps, String amount,
                  String time, String imagePath) {
        this.name = name;
        this.title = title;
        this.category = category;
        this.details = details;
        this.steps = steps;
        this.amount = amount;
        this.time = time;
        this.imagePath = imagePath;
        this.favorite = false;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public FoodCategory getCategory() { return category; }
    public String getAmount() { return amount; }
    public String getTime() { return time; }
    public String getName() { return name; }
    public String getTitle() { return title; }
    public List<String> getDetails() { return details; }
    public String[] getSteps() { return steps; }
    public String getImagePath() { return imagePath; }

    public int matchScore(Set<String> userDetails) {
        int score = 0;
        for (String str : userDetails) {
            if (details.stream().anyMatch(s -> s.contains(str)))
                score++;
        }
        return score;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
