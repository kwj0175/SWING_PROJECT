package entity;

import manager.Manageable;
import java.util.Scanner;

//임시 생성
public class Recipe implements Manageable{
    private String name;
    private String category;
    private String ingredients;
    private String recipeSteps;
    private String info;

    public Recipe() {}

    // --- Getters (표시에 필요) ---
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getIngredients() { return ingredients; }
    public String getRecipeSteps() { return recipeSteps; }
    public String getInfo() { return info; }

    public void setCategory(String category) {
        this.category = category;
    }



    @Override
    public void read(Scanner sc) {
        String line = sc.nextLine();
        if (line.isEmpty()) return;

        try {
            int ingStart = line.indexOf('[');
            if (ingStart == -1) return;
            int ingEnd = line.indexOf(']', ingStart);
            if (ingEnd == -1) return;

            int stepStart = line.indexOf('[', ingEnd);
            if (stepStart == -1) return;
            int stepEnd = line.indexOf(']', stepStart);
            if (stepEnd == -1) return;

            String rawName = line.substring(0, ingStart).trim();
            this.name = rawName.replaceAll("^[0-9]+\\.", "") // "175." 제거
                    .replaceAll("\\(.*\\)$", "") // "(2025.11.6)" 제거
                    .trim();

            this.ingredients = line.substring(ingStart + 1, ingEnd).trim();

            this.recipeSteps = line.substring(stepStart + 1, stepEnd).trim();

            this.info = line.substring(stepEnd + 1).trim();

        } catch (Exception e) {
            System.out.println("레시피 파싱 오류: " + line);
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", category, name);
    }

    @Override
    public boolean matches(String kwd) {
        if (kwd == null || name == null) return false;
        return name.contains(kwd);
    }

    @Override
    public String getId() {
        return name;
    }
}
