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

    private String extractTitle(String line) {//<제목>[재료][]
        if (line == null) return "";

        line = line.trim();
        if (line.isEmpty()) return "";

        int last = line.lastIndexOf('['); // 마지막 '['
        if (last == -1) {
            // [ ] 자체가 없으면 전체가 제목
            return line;
        }

        // 마지막 '[' 앞에 있는 '[' → 뒤에서 두 번째 '['
        int secondLast = line.lastIndexOf('[', last - 1);

        if (secondLast == -1) {
            // '['이 하나만 있는 경우 → 전체를 제목으로 간주
            return line;
        }
        // 정상적인 제목
        return line.substring(0, secondLast).trim();
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
