package screen.category;

import entity.Food;
import entity.FoodCategory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Category {
    ArrayList<Food> foods = new ArrayList<>();

    void readFile() {
        File dir = new File("datasets/texts");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.out.println("txt 파일이 없습니다.");
            return;
        }
        Arrays.sort(files, Comparator.comparing(File::getName));

        for (File file : files) {
            FoodCategory foodCategory = FoodCategory.from(file.getName());
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.trim().isEmpty()) continue;

                    String title = extractTitle(line);
                    foods.add(new Food(title, foodCategory));
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

    private String extractTitle(String line) {
        if (line == null) return "";

        line = line.trim();
        if (line.isEmpty()) return "";

        // 형식: 이름 | 제목 | 재료 | 과정 | 인분 | 시간
        String[] parts = line.split("\\|");
        if (parts.length < 2) {
            // 파이프가 없으면 그냥 전체 줄을 제목으로
            return line;
        }

        // 0: 이름, 1: 제목
        return parts[1].trim();
    }

    public ArrayList<Food> getFoodsByCategory(FoodCategory category) {
        ArrayList<Food> f = new ArrayList<>();
        for (Food food : foods) {
            if (food.getCategory() == category) {
                f.add(food);
            }
        }
        System.out.println(f);
        return f;
    }

}
