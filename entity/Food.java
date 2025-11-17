package entity;

public class Food {
    private String name;
    private final String title;
    private final String img;
    private final FoodCategory category;

    public Food(String title, FoodCategory category) {
//        this.name = "";
        this.title = title;
        this.img = title + ".jpg";
        this.category = category;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public String getTitle() {
        return this.title;
    }

    public String getImg() {
        return this.img;
    }

    public String toString() {
        return this.title;
    }
}
