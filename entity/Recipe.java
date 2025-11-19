package entity;

import manager.Manageable;
import screen.utils.ScreenHelper; // ⭐️ 이미지 찾기 위해 추가

import java.io.File;
import java.util.Scanner;

public class Recipe implements Manageable {

    private String name;            // 검색용 이름 (예: 182.굴떡국...)
    private String title;           // 화면 표시용 이름 (예: 굴떡국)
    private FoodCategory category;  // 카테고리 Enum
    private String[] details;       // 재료 (배열)
    private String[] steps;         // 조리 방법 (배열)
    private String amount;          // 인분
    private String time;            // 시간
    private String imagePath;       // 이미지 절대 경로

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
        // 파이프(|) 기호로 분리. 정규식 특수문자라 \\ 필요
        String[] parts = line.split("\\|");

        if (parts.length >= 6) {
            // 1. [0]번째 문자열 -> 제목 (화면에 표시)
            this.title = parts[0].trim();

            // 2. [1]번째 문자열 -> 이름 (이미지 찾기용)
            this.name = parts[1].trim();

            // 재료 (쉼표 분리)
            this.details = parts[2].trim().split(",");
            for(int i=0; i<details.length; i++) details[i] = details[i].trim();

            // 조리법 (슬래시 분리)
            this.steps = parts[3].trim().split("/");
            for(int i=0; i<steps.length; i++) steps[i] = steps[i].trim();

            this.amount = parts[4].trim();
            this.time = parts[5].trim();

            // 3. 이미지 찾기: 반드시 parts[1]인 'name'을 사용
            File imgFile = ScreenHelper.findRecipeImage(this.name);
            if (imgFile != null) {
                this.imagePath = imgFile.getAbsolutePath();
            } else {
                this.imagePath = null;
            }
        }
    }

    // --- Getters & Setters ---

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
