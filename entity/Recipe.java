package entity;

import manager.Manageable;
import java.util.Scanner;

public class Recipe implements Manageable {
    // 화면 표시용 이름
    private String displayName;
    // 이미지 파일 매핑용
    private String imageName;

    private String category;
    private String ingredients;
    private String steps;        // 조리법
    private String info;         // 인분 + 시간

    private String title;

    public Recipe() {}

    @Override
    public void read(Scanner sc) {
        String line = sc.nextLine();
        String[] parts = line.split("\\|");

        if (parts.length >= 6) {
            this.displayName = parts[0].trim();
            this.imageName = parts[1].trim();
            this.ingredients = parts[2].trim();
            this.steps = parts[3].trim();
            // 인분 + 시간 -> info
            this.info = parts[4].trim() + " " + parts[5].trim();

            this.title = this.displayName;
        }
    }

    //  Getters
    public String getDisplayName() { return displayName != null ? displayName : "이름 없음"; }
    public String getImageName() { return imageName; }
    public String getCategory() { return category; }
    public String getIngredients() { return ingredients; }
    public String getSteps() { return steps; }
    public String getInfo() { return info; }

    public String getName() { return displayName; }
    public String getTitle() { return displayName; }

    public void setCategory(String category) { this.category = category; }

    //  Manageable
    @Override
    public boolean matches(String kwd) {
        if (displayName == null) return false;
        return displayName.contains(kwd);
    }

    @Override
    public String getId() { return displayName; }

    @Override
    public String toString() {
        return String.format("[%s] %s", category, displayName);
    }

}