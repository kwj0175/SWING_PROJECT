package entity;

public class Recipe {

    private String name;
    private String title;           // 레시피 이름
    private FoodCategory category;       // 카테고리 (밥, 국, 찌개, 반찬 등)
    private String[] details;    // 재료 설명 (여러 줄 문자열)
    private String[] steps;          // 조리 방법 (여러 줄 문자열)
    private String amount;
    private String time;
    private String imagePath;      // 이미지 파일 경로

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

    public FoodCategory getCategory() {
        return category;
    }

    public String getAmount() {
        return amount;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String[] getDetails() {
        return details;
    }

    public String[] getSteps() {
        return steps;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean checkCat(FoodCategory category) {
        return this.getCategory().equals(category);
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
