package entity;

public class Recipe {

    private int id;                // 레시피 번호
    private String name;           // 레시피 이름
    private String category;       // 카테고리 (밥, 국, 찌개, 반찬 등)
    private String ingredients;    // 재료 설명 (여러 줄 문자열)
    private String steps;          // 조리 방법 (여러 줄 문자열)
    private String imagePath;      // 이미지 파일 경로

    public Recipe() {
    }

    public Recipe(int id, String name, String category,
                  String ingredients, String steps, String imagePath) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        this.steps = steps;
        this.imagePath = imagePath;
    }

    // ===== Getter / Setter =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
