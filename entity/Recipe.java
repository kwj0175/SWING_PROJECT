package entity;

public class Recipe {

    // ===== 필드(속성) =====
    private String name;        // 메뉴 이름
    private String ingredients; // 재료 (문자열 하나, 나중에 줄바꿈 포함 가능)
    private String method;      // 조리방법 (문자열 하나, 나중에 줄바꿈 포함 가능)
    private int servings;       // 인분 수
    private int cookTime;       // 조리시간(분)
    private String imagePath;   // 이미지 파일 경로

    // ===== 생성자 =====
    public Recipe(String name, String ingredients, String method,
                  int servings, int cookTime, String imagePath) {
        this.name = name;
        this.ingredients = ingredients;
        this.method = method;
        this.servings = servings;
        this.cookTime = cookTime;
        this.imagePath = imagePath;
    }

    // ===== getter / setter =====
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        // 나중에 리스트 등에 띄울 때 이름만 보이게
        return name;
    }
}
