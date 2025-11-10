package screen.planner;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.awt.event.*;

public class PlannerDisplay extends JPanel {
    private JTable table;
    private JLabel weekLabel;
    private JLabel recipeTitleLabel;
    private JTextArea recipeContentArea;
    private int currentWeek;
    private int currentMonth;
    private int currentYear;

    public PlannerDisplay() {
        setDate();
        JPanel topPanel = new JPanel();

        JScrollPane scrollPane = buildTable(topPanel);
        JPanel recipePanel = buildRecipe();

        setLayout(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(recipePanel, BorderLayout.SOUTH);

        updateTableHeaders(); //초기 실행시 테이블 헤더 정상적으로 나타내도록 표시
    }

    private JPanel buildRecipe() {
        // 하단 레시피 표시 패널
        JPanel recipePanel = new JPanel();
        recipePanel.setLayout(new BorderLayout());
        recipePanel.setPreferredSize(new Dimension(360, 180));

        recipePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(0x273142)),
                        "레시피 상세정보",
                        0, 0, new Font("맑은 고딕", Font.BOLD, 12)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // 레시피 제목 라벨
        recipeTitleLabel = new JLabel("레시피를 선택하세요");
        recipeTitleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        recipeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 레시피 내용 영역
        recipeContentArea = new JTextArea();
        recipeContentArea.setEditable(false);
        recipeContentArea.setLineWrap(true);
        recipeContentArea.setWrapStyleWord(true);
        recipeContentArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        recipeContentArea.setText("표의 셀을 클릭하면 레시피 정보가 여기에 표시됩니다.");
        JScrollPane recipeScrollPane = new JScrollPane(recipeContentArea);

        recipePanel.add(recipeTitleLabel, BorderLayout.NORTH);
        recipePanel.add(recipeScrollPane, BorderLayout.CENTER);
        return recipePanel;
    }

    private JScrollPane buildTable(JPanel topPanel) {
        weekLabel = new JLabel(getWeekText(), SwingConstants.CENTER);
        weekLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        buildButtons(topPanel);

        // 표 구성
        String[] columns = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};//임시값, updateTableHeaders() 메서드에서 업데이트됨
        String[] rows = {"아침", "점심", "저녁"};

        DefaultTableModel model = new DefaultTableModel(rows.length, columns.length);
        model.setColumnIdentifiers(columns);
        table = new JTable(model);
        setupTableInteraction();
        table.setRowHeight(40);
        for (int i = 0; i < rows.length; i++) {
            model.setValueAt(rows[i], i, 0);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // 가로 스크롤 활성화
        return scrollPane;
    }

    private void buttonListener(JButton homeBtn, JButton prevWeekBtn, JButton nextWeekBtn) {
        prevWeekBtnAction(prevWeekBtn);
        nextWeekBtnAction(nextWeekBtn);

        homeBtn.addActionListener(event -> {
            System.out.println("홈 버튼 클릭됨");
            // 홈 버튼 클릭 시 동작 추가 가능
        });
    }

    private void buildButtons(JPanel topPanel) {
    	topPanel.setLayout(new BorderLayout());
    	
        JButton homeBtn = new JButton("❮❮");// 좌상단 홈버튼
        JButton prevWeekBtn = new JButton("❮");
        JButton nextWeekBtn = new JButton("❯");
        
    	// 왼쪽(서쪽)에 홈버튼
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(setCustomButton(homeBtn));
        
        // 가운데(중앙)에 주차 라벨
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(setCustomButton(prevWeekBtn));
        centerPanel.add(weekLabel);
        centerPanel.add(setCustomButton(nextWeekBtn));
        
        // 전체 topPanel에 부착
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(centerPanel, BorderLayout.CENTER);

        buttonListener(homeBtn, prevWeekBtn, nextWeekBtn);
    }

    private void setDate() {
        // 날짜 관련 설정
        LocalDate today = LocalDate.now();
        currentYear = today.getYear(); // 시작 연도 설정
        currentMonth = today.getMonthValue(); // 시작 월 설정
        currentWeek = today.get(WeekFields.of(Locale.getDefault()).weekOfMonth()); // 시작 주차 설정
    }

    private void prevWeekBtnAction(JButton button) {
        // 이전주 버튼 클릭 이벤트
        button.addActionListener(event -> {
            currentWeek--;
            if (currentWeek < 1) {
                currentMonth--; // 월 감소
                if (currentMonth < 1) {
                    currentYear--; // 연도 감소
                    currentMonth = 12; // 월을 12로 설정
                }
                currentWeek = 5; // 한달 최대 5주차까지
            }
            updateWeekLabel(); // 주차 라벨 업데이트
            updateTableHeaders(); // 테이블 헤더 업데이트
        });
    }

    private void nextWeekBtnAction(JButton button) {
        // 다음주 버튼 클릭 이벤트
        button.addActionListener(event -> {
            currentWeek++;
            if (currentWeek > 5) {
                currentMonth++; // 월 증가
                if (currentMonth > 12) {
                    currentYear++; // 연도 증가
                    currentMonth = 1; // 월을 1로 설정
                }
                currentWeek = 1; // 주차를 1로 설정
            }
            updateWeekLabel(); // 주차 라벨 업데이트
            updateTableHeaders(); // 테이블 헤더 업데이트
        });
    }

    private JButton setCustomButton(JButton button) {
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setBackground(new Color(0, 0, 0, 0));
        button.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        return button;
    }

    private void setupTableInteraction() {
		// 테이블 셀 클릭 관련 이벤트(조회 및 삭제 기능), 초기화 함수
    	// 테이블 직접 수정 불가
        table.setDefaultEditor(Object.class, null);

        // 기존 리스너 제거 (중복 방지)
        for (MouseListener listener : table.getMouseListeners()) {
            table.removeMouseListener(listener);
        }

        // 새 마우스 리스너 추가
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (col <= 0) return; // 잘못된 영역 클릭 방지

                // 좌클릭 → 레시피 조회
                if (SwingUtilities.isLeftMouseButton(e)) {
                    String recipe = (String) table.getValueAt(row, col); // 셀에서 레시피 정보 가져오기, 나중에 셀에 넣을때 레시피 정보 같이 넣어야함
                    if (recipe != null && !recipe.isEmpty()) {
                        // 레시피 제목 업데이트
                        String mealType = (String) table.getValueAt(row, 0);
                        String dayHeader = table.getColumnName(col);
                        recipeTitleLabel.setText(dayHeader + " - " + mealType);

                        // 레시피 내용 업데이트
                        recipeContentArea.setText(recipe);
                    } else {
                        recipeTitleLabel.setText("등록된 레시피 없음");
                        recipeContentArea.setText("해당 시간대에 등록된 레시피가 없습니다.");
                    }
                }

                // 우클릭 → 삭제 메뉴
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem deleteItem = new JMenuItem("삭제하기");

                    deleteItem.addActionListener(ae -> {
                        table.setValueAt("", row, col);
                        recipeTitleLabel.setText("레시피를 선택하세요");
                        recipeContentArea.setText("표의 셀을 클릭하면 레시피 정보가 여기에 표시됩니다.");
                    });

                    popup.add(deleteItem);
                    popup.show(table, e.getX(), e.getY());
                }
            }
        });
	}

	private String getWeekText() {
    	return currentYear + "년 " + currentMonth + "월 " + currentWeek + "주차";
    }

    private void updateWeekLabel() {
        weekLabel.setText(getWeekText());
    }

    private void updateTableHeaders() {
    	// 현재 주의 시작 날짜 계산 (일요일 기준)
        LocalDate startOfWeek = LocalDate.of(currentYear, currentMonth, 1)
            .with(WeekFields.of(Locale.getDefault()).weekOfMonth(), currentWeek)
            .with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);

        // 요일별 날짜 계산 및 헤더 업데이트
        String[] columns = new String[7];
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            columns[i] = String.format("%02d (%s)",
                date.getDayOfMonth(),
                getDayOfWeekKorean(date.getDayOfWeek().getValue()));
        }

        // 테이블 모델에 새로운 헤더 설정
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setColumnIdentifiers(columns);
	}

    private String getDayOfWeekKorean(int dayOfWeek) {
        DayOfWeek day = DayOfWeek.of(dayOfWeek);
        return day.getDisplayName(TextStyle.SHORT, Locale.KOREAN);
    }

}