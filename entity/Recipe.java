package entity;

import manager.Manageable;
import screen.utils.ScreenHelper; // ⭐️ 이미지 찾기 위해 추가

import java.io.File;
import java.util.Scanner;

public class Recipe implements Manageable {

    private String name;
    private String title;
    private FoodCategory category;
    private String[] details;
    private String[] steps;
    private String amount;
    private String time;
    private String imagePath;

    // 기본 생성자 (필수)
    public Recipe() {}

    public Recipe(String name, String title,
                  FoodCategory category, String[] details,
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
    }

    @Override
    public void read(Scanner sc) {
        String line = sc.nextLine();
        String[] parts = line.split("\\|");

        if (parts.length >= 6) {
            this.title = parts[0].trim();
            this.name = parts[1].trim();
            this.details = parts[2].trim().split(",");
            for(int i=0; i<details.length; i++) details[i] = details[i].trim();
            this.steps = parts[3].trim().split("/");
            for(int i=0; i<steps.length; i++) steps[i] = steps[i].trim();

            this.amount = parts[4].trim();
            this.time = parts[5].trim();

            File imgFile = ScreenHelper.findRecipeImage(this.name);
            if (imgFile != null) {
                this.imagePath = imgFile.getAbsolutePath();
            } else {
                this.imagePath = null;
            }
        }
    }

    // Getters & Setters

    public FoodCategory getCategory() { return category; }
    public void setCategory(FoodCategory category) { this.category = category; }

    public String getAmount() { return amount; }
    public String getTime() { return time; }
    public String getName() { return name; }
    public String getTitle() { return title; }
    public String[] getDetails() { return details; }
    public String[] getSteps() { return steps; }
    public String getImagePath() { return imagePath; }

    public boolean checkCat(FoodCategory category) {
        return this.getCategory().equals(category);
    }

    @Override
    public boolean matches(String kwd) {
        if (title == null) return false;
        return title.contains(kwd);
    }

    @Override
    public String getId() { return title; }


    @Override
    public String toString() {
        return "Recipe{" +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
