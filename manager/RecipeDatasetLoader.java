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
                "datasets/texts/main_side_dishes.txt",
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
// ===== 1) 제목 / 재료 / 과정 위치 찾기 =====
// " ...여기까지가 제목... [재료... ] [과정... ] 4인분..." 형태라고 가정
int spaceBracket = line.indexOf(" [");   // 제목 뒤의 " [" (재료 시작 바로 앞)
if (spaceBracket == -1) {
System.out.println("포맷 이상 (재료 [ 못 찾음): " + line);
return null;
}

// 제목(이미지 파일용) = 재료 [ 앞까지 전체
String titleFull = line.substring(0, spaceBracket).trim();
// 예: "[공모전] 뜨끈한 국물요리 소고기미역국"

// 재료 [ 위치/ ] 위치
int ingredOpen = spaceBracket + 1;                 // 실제 '[' 위치
int ingredClose = line.indexOf(']', ingredOpen+1);
String ingredients = line.substring(ingredOpen+1, ingredClose).trim();

// 조리 과정 [ ] 찾기
int stepsOpen = line.indexOf('[', ingredClose+1);
int stepsClose = line.indexOf(']', stepsOpen+1);
String stepsRaw = line.substring(stepsOpen+1, stepsClose).trim();

// ===== 2) 조리 과정을 줄 나눠서 보기 좋게 =====
String[] stepParts = stepsRaw.split("\\s*,\\s*");
StringBuilder steps = new StringBuilder();
for (int i = 0; i < stepParts.length; i++) {
steps.append(i + 1).append(". ").append(stepParts[i]);
if (i != stepParts.length - 1) steps.append("\n");
}

// ===== 3) 화면에 보여줄 이름 (태그/레시피 꼬리 제거는 옵션) =====
String name = titleFull;


// ===== 4) 이미지 파일 경로 만들기 =====
//  ex) datasets/imgs/soups/[공모전] 뜨끈한 국물요리 소고기미역국.jpg
String baseFileName = titleFull;

// jpg 도 있고 png 도 있어서 둘 다 시도
String jpgPath = imageFolder + baseFileName + ".jpg";
String pngPath = imageFolder + baseFileName + ".png";

String imagePath;
java.io.File jpgFile = new java.io.File(jpgPath);
java.io.File pngFile = new java.io.File(pngPath);

if (jpgFile.exists()) {
imagePath = jpgPath;
} else if (pngFile.exists()) {
imagePath = pngPath;
} else {
// 둘 다 없으면 일단 jpg 경로로 넣고, 화면에서는 "이미지 없음" 처리될 수도 있음
imagePath = jpgPath;
System.out.println("이미지 파일 없음: " + jpgPath + " / " + pngPath);
}

// 최종 Recipe 생성
return new Recipe(id, name, category, ingredients, steps.toString(), imagePath);

} catch (Exception e) {
System.out.println("파싱 실패한 줄: " + line);
e.printStackTrace();
return null;
}
}
}