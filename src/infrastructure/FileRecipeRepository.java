package src.infrastructure;

import src.entity.FoodCategory;
import src.entity.Recipe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FileRecipeRepository implements RecipeRepository {
    private final String baseDir;

    public FileRecipeRepository(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public List<Recipe> loadAll() {
        List<Recipe> result = new ArrayList<>();

        File dir = new File(baseDir);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.out.println("txt 파일이 없습니다.");
            return result;
        }
        Arrays.sort(files, Comparator.comparing(File::getName));

        for (File file : files) {
            FoodCategory foodCategory = FoodCategory.from(file.getName());
            if (foodCategory == null) {
                System.err.println("FoodCategory 매핑 실패: " + file.getName());
                continue;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.trim().isEmpty()) continue;
                    Recipe recipe = parseData(line, foodCategory);
                    if (recipe != null)
                        result.add(recipe);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

    private String resolveImageDir(FoodCategory foodCategory) {
        return switch (foodCategory) {
            case MAIN_SIDE_DISH -> "main_side_dishes";
            case SIDE_DISH -> "side_dishes";
            case SOUP -> "soups";
            case RICE_DISH -> "rice_dishes";
            default -> foodCategory.name().toLowerCase();
        };
    }

    private Recipe parseData(String line, FoodCategory foodCategory) {
        if (line == null) return null;
        String[] items = line.split("\\|");
        if (items.length != 6) {
            System.err.println("잘못된 형식의 라인: " + line);
            return null;
        }

        String name = items[0].trim();
        String title    = items[1].trim();
        List<String> details  = Arrays.stream(items[2].split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        String[] steps = Arrays.stream(items[3].split("/"))
                .map(String::trim)
                .toArray(String[]::new);
        String amount   = items[4].trim();
        String time     = items[5].trim();


        String imgDir = resolveImageDir(foodCategory);
        String imagePath = "datasets/imgs/" + imgDir + "/" + title + ".jpg";

        return new Recipe(
                name,
                title,
                foodCategory,
                details,
                steps,
                amount,
                time,
                imagePath
        );
    }
}
