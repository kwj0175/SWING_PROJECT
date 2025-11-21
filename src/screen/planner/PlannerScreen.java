package src.screen.planner;

import src.screen.MainScreen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.awt.event.*;

public class PlannerScreen extends JPanel {
    private JTable table;
    private JLabel weekLabel;
    private JLabel recipeTitleLabel;
    private JTextArea recipeContentArea;
    private JButton homeBtn;
    private JButton prevWeekBtn;
    private JButton nextWeekBtn;

    private final PlannerPresenter plannerPresenter;

    public PlannerScreen(MainScreen mainScreen) {
        this.plannerPresenter = new PlannerPresenter();

        setLayout(new GridBagLayout());

        JPanel form = buildForm();
        JPanel backBtnPanel = buildBackBtnPanel();

        JPanel root = new JPanel();
        GroupLayout layout = new GroupLayout(root);
        root.setLayout(layout);
        root.setOpaque(false);

        layout.setAutoCreateGaps(false);
        layout.setAutoCreateContainerGaps(false);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(backBtnPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(form, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(backBtnPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(form, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        add(root, c);

        prevWeekBtnAction(prevWeekBtn);
        nextWeekBtnAction(nextWeekBtn);

        homeBtn.addActionListener(event -> {
            System.out.println("홈 버튼 클릭됨");
            mainScreen.displayHomeScreen();
        });
    }

    private JPanel buildBackBtnPanel() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        homeBtn = new JButton("❮❮");
        bar.add(setCustomButton(homeBtn));
        return bar;
    }

    private JPanel buildForm() {
        JPanel topPanel = new JPanel();
        JScrollPane scrollPane = buildTable(topPanel);
        JPanel recipePanel = buildRecipe();

        JPanel form = new JPanel();
        GroupLayout formLayout = new GroupLayout(form);
        form.setLayout(formLayout);

        formLayout.setAutoCreateGaps(false);
        formLayout.setAutoCreateContainerGaps(false);

        int GAP = 5;
        formLayout.setHorizontalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(topPanel,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                        .addComponent(scrollPane,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                        .addComponent(recipePanel,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
        );

        // 세로: 위(top) - 가운데(scroll) - 아래(recipe) 순서로 배치
        formLayout.setVerticalGroup(
                formLayout.createSequentialGroup()
                        .addComponent(topPanel,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addGap(GAP)
                        .addComponent(scrollPane,
                                0,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)   // ★ 가운데가 가변 높이
                        .addGap(GAP)
                        .addComponent(recipePanel,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
        );

        updateTableHeaders(); //초기 실행시 테이블 헤더 정상적으로 나타내도록 표시
        return form;
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
        weekLabel = new JLabel(plannerPresenter.getWeekText(), SwingConstants.CENTER);
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

    private void buildButtons(JPanel topPanel) {
    	topPanel.setLayout(new BorderLayout());

        prevWeekBtn = new JButton("❮");
        nextWeekBtn = new JButton("❯");
        
        // 가운데(중앙)에 주차 라벨
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(setCustomButton(prevWeekBtn));
        centerPanel.add(weekLabel);
        centerPanel.add(setCustomButton(nextWeekBtn));

        topPanel.add(centerPanel, BorderLayout.CENTER);
    }

    private void prevWeekBtnAction(JButton button) {
        button.addActionListener(event -> {
            plannerPresenter.movePrevWeek();
            updateWeekLabel();
            updateTableHeaders();
        });
    }

    private void nextWeekBtnAction(JButton button) {
        // 다음주 버튼 클릭 이벤트
        button.addActionListener(event -> {
            plannerPresenter.moveNextWeek();
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

    private void updateWeekLabel() {
        weekLabel.setText(plannerPresenter.getWeekText());
    }

    private void updateTableHeaders() {
        String[] columns = plannerPresenter.getWeekHeaders();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setColumnIdentifiers(columns);
	}
}