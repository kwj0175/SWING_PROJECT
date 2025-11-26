package src.entity;

public enum FoodCategory {
    MAIN_SIDE_DISH("메인", "main_side_dishes"),
    RICE_DISH("밥류", "rice_dishes"),
    SIDE_DISH("반찬", "side_dishes"),
    SOUP("국/찌개", "soups");

    private final String displayName;
    private final String filePrefix;

    FoodCategory(String displayName, String filePrefix) {
        this.filePrefix = filePrefix;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static FoodCategory from(String fileName) {
        for (FoodCategory f : values()) {
            if (fileName.startsWith(f.filePrefix)) return f;
        }
        return null;
    }
}
