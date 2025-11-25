package src.screen.planner;

import src.entity.Recipe;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PlannerPresenter {
    private int currentWeek;
    private int currentMonth;
    private int currentYear;

    private final WeekFields weekFields;
    private final Map<String, Map<String, Recipe>> weeklyRecipeData = new HashMap<>();

    public PlannerPresenter() {
        this(WeekFields.of(Locale.getDefault()));
    }

    public PlannerPresenter(WeekFields weekFields) {
        this.weekFields = weekFields;
        initToday();
    }

    private void initToday() {
        LocalDate today = LocalDate.now();
        currentYear = today.getYear();
        currentMonth = today.getMonthValue();
        currentWeek = today.get(weekFields.weekOfMonth());
    }

    /** 화면에 표시할 주차 텍스트 */
    public String getWeekText() {
        return currentYear + "년 " + currentMonth + "월 ";// + currentWeek + "주차";
    }

    /** 현재 주의 요일 헤더 (예: "01 (월)" ~ "07 (일)" 이런 식) */
    public String[] getWeekHeaders() {
        LocalDate startOfWeek = LocalDate.of(currentYear, currentMonth, 1)
                .with(weekFields.weekOfMonth(), currentWeek)
                .with(weekFields.dayOfWeek(), 1);

        String[] columns = new String[7];
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            DayOfWeek day = date.getDayOfWeek();
            String dayKor = day.getDisplayName(TextStyle.SHORT, Locale.KOREAN);

            columns[i] = String.format("%02d (%s)",
                    date.getDayOfMonth(),
                    dayKor);
        }
        return columns;
    }

    public void movePrevWeek() {
        currentWeek--;
        if (currentWeek < 1) {
            currentMonth--;
            if (currentMonth < 1) {
                currentYear--;
                currentMonth = 12;
            }
            // 대충 5주까지 있다고 가정한 기존 로직 유지
            currentWeek = 5;
        }
    }

    public void moveNextWeek() {
        currentWeek++;
        if (currentWeek > 5) {
            currentMonth++;
            if (currentMonth > 12) {
                currentYear++;
                currentMonth = 1;
            }
            currentWeek = 1;
        }
    }

    private String getCurrentWeekKey() {
        return currentYear + "-" + currentMonth + "-" + currentWeek;
    }

    private Map<String, Recipe> getCurrentWeekRecipes() {
        String weekKey = getCurrentWeekKey();
        return weeklyRecipeData.computeIfAbsent(weekKey, k -> new HashMap<>());
    }

    private String toCellKey(int row, int col) {
        return row + "," + col;
    }

    public Recipe getRecipeAt(int row, int col) {
        return getCurrentWeekRecipes().get(toCellKey(row, col));
    }

    public void setRecipeAt(int row, int col, Recipe recipe) {
        Map<String, Recipe> currentWeek = getCurrentWeekRecipes();
        if (recipe == null) {
            currentWeek.remove(toCellKey(row, col));
        } else {
            currentWeek.put(toCellKey(row, col), recipe);
        }
    }

    public void removeRecipeAt(int row, int col) {
        getCurrentWeekRecipes().remove(toCellKey(row, col));
    }
}
