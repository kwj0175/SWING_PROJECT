package manager;

import entity.Recipe;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDatasetLoader {

    // ===== 카테고리별 편의 메서드 =====

    public static List<Recipe> loadSoups() {
        return loadFromFile(
                "datasets/texts/soups.txt",
                "국/찌개",
                "datasets/imgs/soups/"
        );
    }

    public static List<Recipe> loadSideDishes() {
        return loadFromFile(
                "datasets/texts/side_dishes.txt",
                "반찬",
                "datasets/imgs/side_dishes/"
        );
    }

    public static List<Recipe> loadRiceDishes() {
        return loadFromFile(
                "datasets/texts/rice_dishes.txt",
                "밥요리",
                "datasets/imgs/rice_dishes/"
        );
    }

    public static List<Recipe> loadMainSideDishes() {
        return loadFromFile(
                "datasets/texts/main_dishes.txt",
                "메인반찬",
                "datasets/imgs/main_side_dishes/"
        );
    }

    // ===== 공통 로더 =====

    private static List<Recipe> loadFromFile(String textPath,
                                             String categoryLabel,
                                             String imageFolder) {
        List<Recipe> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(textPath))) {
            String line;
            int lineId = 1; // 줄번호를 그대로 레시피 id로 사용

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Recipe r = parseLine(lineId, line, categoryLabel, imageFolder);
                if (r != null) {
                    list.add(r);
                    lineId++;
                }
            }
        } catch (Exception e) {
            System.out.println("레시피 파일 읽기 실패: " + textPath);
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 한 줄 예시:
     * [공모전] 뜨끈한 국물요리 소고기미역국 [재료들...] [조리과정들...] 4인분 30분 이내
     * 우삼겹콩나물국 레시피 [재료들...] [조리과정들...] 3인분 20분 이내
     */
    private static Recipe parseLine(int id,
            String line,
            String category,
            String imageFolder) {
try {
if (line == null) return null;
line = line.trim();
if (line.isEmpty()) return null;

// 형식: 이름 | 제목 | 재료 | 과정 | 인분 | 시간
String[] parts = line.split("\\|");


if (parts.length < 1) {
 System.out.println("포맷 이상(완전 이상한 줄): " + line);
 return null;
}

//부족한 칸은 그냥 빈 문자열로 채움
String nameField = parts[0].trim();                                 // 이름
String title     = (parts.length > 1 ? parts[1].trim() : nameField); // 제목 없으면 이름으로
String ingredients = (parts.length > 2 ? parts[2].trim() : "");
String stepsRaw    = (parts.length > 3 ? parts[3].trim() : "");
String servings    = (parts.length > 4 ? parts[4].trim() : "");
String time        = (parts.length > 5 ? parts[5].trim() : "");

// ===== 조리 과정 보기 좋게 =====
StringBuilder steps = new StringBuilder();
if (!stepsRaw.isEmpty()) {
String[] stepParts = stepsRaw.split("\\s*/\\s*"); // / 기준으로 나누기
for (int i = 0; i < stepParts.length; i++) {
steps.append(i + 1).append(". ").append(stepParts[i].trim());
if (i != stepParts.length - 1) steps.append("\n");
}
}

// 인분/시간 정보 뒤에 붙이기
if (!servings.isEmpty() || !time.isEmpty()) {
steps.append("\n\n");
if (!servings.isEmpty()) steps.append("[인분] ").append(servings).append("\n");
if (!time.isEmpty())     steps.append("[시간] ").append(time);
}

//===== 이미지 파일 경로 =====
//제목(두 번째 칸) 기준
String baseFileName = title.trim();   // 혹시 모를 앞뒤 공백 제거

ClassLoader cl = RecipeDatasetLoader.class.getClassLoader();

//후보 경로들: (1) 그대로, (2) 앞에 공백 하나
String[] candidates = {
     imageFolder + baseFileName + ".jpg",
     imageFolder + baseFileName + ".png",
     imageFolder + " " + baseFileName + ".jpg",
     imageFolder + " " + baseFileName + ".png"
};

String imagePath = null;
for (String path : candidates) {
 if (cl.getResource(path) != null) {
     imagePath = path;
     break;
 }
}

if (imagePath == null) {
 // 그래도 못 찾으면 첫 번째 후보를 그냥 저장하고 로그만 찍기
 imagePath = candidates[0];
 System.out.println("이미지 리소스 없음: ");
 for (String p : candidates) {
     System.out.println("  - " + p);
 }
}

//마지막에 Recipe 생성할 때 이 imagePath 넘김
return new Recipe(id, title.trim(), category, ingredients, steps.toString(), imagePath);


} catch (Exception e) {
System.out.println("파싱 실패한 줄: " + line);
e.printStackTrace();
return null;
}
}
}