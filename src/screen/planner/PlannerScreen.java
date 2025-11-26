package src.screen.planner;

import src.entity.Recipe;
import src.manager.RecipeManager;
import src.screen.MainScreen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.awt.event.*;

public class PlannerScreen extends JPanel {
    private JTable table;
    private JLabel weekLabel;
    private JLabel recipeTitleLabel;
    private JTextArea recipeContentArea;
    private JButton prevWeekBtn;
    private JButton nextWeekBtn;
    private JPanel recipePanel;

    private final String[] COLUMNS = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
    private final String[] ROWS = {"아침", "점심", "저녁"};

    private final PlannerPresenter plannerPresenter;
    private final MainScreen mainScreen;
    private final RecipeManager recipeManager;

    // 오버레이 관련
    private PlannerOverlay overlayPanel;
    private boolean isAddMode = false;

    public PlannerScreen(MainScreen mainScreen, RecipeManager recipeManager) {
        this.plannerPresenter = new PlannerPresenter();
        this.mainScreen = mainScreen;
        this.recipeManager = recipeManager;

        setLayout(new GridBagLayout());

        JPanel form = buildForm();

        JPanel root = new JPanel();
        GroupLayout layout = new GroupLayout(root);
        root.setLayout(layout);
        root.setOpaque(false);

        layout.setAutoCreateGaps(false);
        layout.setAutoCreateContainerGaps(false);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(form, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
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

        // 초기 주차 데이터 로드
        loadWeekData();
    }

    /**
     * 주차 변경 시 데이터 로드
     */
    private void loadWeekData() {
        // 테이블 초기화
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                Recipe recipe = plannerPresenter.getRecipeAt(row, col);
                if (recipe != null) {
                    model.setValueAt(recipe.getName(), row, col);
                } else {
                    model.setValueAt("", row, col);
                }
            }
        }

        // 레시피 상세정보 초기화
        recipeTitleLabel.setText("레시피를 선택하세요");
        recipeContentArea.setText("표의 셀을 클릭하면 레시피 정보가 여기에 표시됩니다.");

        table.revalidate();
        table.repaint();
    }

    /**
     * 레시피 추가 모드로 진입 (RecipeScreen에서 호출)
     */
    public void enterRecipeAddMode(Recipe recipe) {
        this.isAddMode = true;
        recipeDetailPanelVisibility();
        showRecipeOverlay(recipe);
    }

    /**
     * 레시피 정보를 보여주는 오버레이 생성
     */
    private void showRecipeOverlay(Recipe recipe) {
        // 기존 오버레이가 있으면 제거
        if (overlayPanel == null) {
            // PlannerOverlay 생성
            overlayPanel = new PlannerOverlay(recipe, e -> exitRecipeAddMode());

            // 오버레이를 최상단에 추가
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.fill = GridBagConstraints.BOTH;
            add(overlayPanel, c);
        } else {
            overlayPanel.setRecipe(recipe);
            overlayPanel.setVisible(true);
        }

        // 화면 최상단으로 이동
        setComponentZOrder(overlayPanel, 0);
        revalidate();
        repaint();
    }

    /**
     * 레시피 추가 모드 종료
     */
    private void exitRecipeAddMode() {
        isAddMode = false;

        if (overlayPanel != null) {
            overlayPanel.setVisible(false);
        }
        // 하단 레시피 상세정보 패널 다시 보이기
        recipeDetailPanelVisibility();
        revalidate();
        repaint();
    }

    /**
     * 레시피 상세정보 패널 숨기기
     */
    private void recipeDetailPanelVisibility() {
        if (recipePanel == null)  return;

        boolean visible = !isAddMode;
        recipePanel.setVisible(visible);
        recipePanel.revalidate();
        recipePanel.repaint();
    }

    private JPanel buildForm() {
        JPanel topPanel = new JPanel();
        JScrollPane scrollPane = buildTable(topPanel);
        JPanel detailPanel = buildRecipe();

        JPanel form = new JPanel();
        GroupLayout formLayout = new GroupLayout(form);
        form.setLayout(formLayout);

        formLayout.setAutoCreateGaps(false);
        formLayout.setAutoCreateContainerGaps(false);

        int GAP = 5;
        formLayout.setHorizontalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(topPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(detailPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        formLayout.setVerticalGroup(
                formLayout.createSequentialGroup()
                        .addComponent(topPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(GAP)
                        .addComponent(scrollPane, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(GAP)
                        .addComponent(detailPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );

        updateTableHeaders();
        return form;
    }

    private JPanel buildRecipe() {
        recipePanel = new JPanel();
        recipePanel.setLayout(new BorderLayout());
        recipePanel.setPreferredSize(new Dimension(360, 280));

        recipePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(0x273142)),
                        "레시피 상세정보",
                        0, 0, new Font("맑은 고딕", Font.BOLD, 12)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        recipeTitleLabel = new JLabel("레시피를 선택하세요");
        recipeTitleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        recipeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

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

        DefaultTableModel model = new DefaultTableModel(ROWS.length, COLUMNS.length);
        model.setColumnIdentifiers(COLUMNS);
        table = new JTable(model);
        setupTableInteraction();
        table.setRowHeight(40);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    private void buildButtons(JPanel topPanel) {
        topPanel.setLayout(new BorderLayout());

        prevWeekBtn = new JButton("◀");
        nextWeekBtn = new JButton("▶");

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
            loadWeekData(); // 주차 데이터 로드
        });
    }

    private void nextWeekBtnAction(JButton button) {
        button.addActionListener(event -> {
            plannerPresenter.moveNextWeek();
            updateWeekLabel();
            updateTableHeaders();
            loadWeekData(); // 주차 데이터 로드
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
        table.setDefaultEditor(Object.class, null);

        for (MouseListener listener : table.getMouseListeners()) {
            table.removeMouseListener(listener);
        }

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (col < 0 || row < 0) return;

                // 레시피 추가 모드일 때
                if (isAddMode && overlayPanel != null) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        Recipe recipeToAdd = overlayPanel.getRecipe();
                        addRecipeToCell(row, col, recipeToAdd);
                        exitRecipeAddMode();
                        return;
                    }
                }

                Recipe existingRecipe = plannerPresenter.getRecipeAt(row, col);

                // 일반 모드 - 좌클릭
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (existingRecipe != null) {
                        // 더블 클릭 - 상세 화면으로 이동
                        if (e.getClickCount() == 2) {
                            mainScreen.displayRecipeDetail(existingRecipe);
                        }
                        // 싱글 클릭 - 하단 패널에 정보 표시
                        else if (e.getClickCount() == 1) {
                            displayRecipeInfo(row, col, existingRecipe);
                        }
                    } else {
                        // 빈 셀 클릭 시 레시피 선택 다이얼로그
                        showRecipeSelectionDialog(row, col);
                    }
                }

                // 우클릭 → 삭제 메뉴
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (isAddMode) return;
                    if (existingRecipe != null) {
                        JPopupMenu popup = new JPopupMenu();
                        JMenuItem deleteItem = new JMenuItem("삭제하기");
                        JMenuItem viewDetailItem = new JMenuItem("상세보기");

                        deleteItem.addActionListener(ae -> {
                            table.setValueAt("", row, col);
                            plannerPresenter.removeRecipeAt(row, col);
                            recipeTitleLabel.setText("레시피를 선택하세요");
                            recipeContentArea.setText("표의 셀을 클릭하면 레시피 정보가 여기에 표시됩니다.");
                        });

                        viewDetailItem.addActionListener(ae -> {
                            mainScreen.displayRecipeDetail(existingRecipe);
                        });

                        popup.add(viewDetailItem);
                        popup.add(deleteItem);
                        popup.show(table, e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private void showRecipeSelectionDialog(int row, int col) {
        List<Recipe> allRecipes = recipeManager.getAllRecipe();

        if (allRecipes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "등록된 레시피가 없습니다.",
                    "알림",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "레시피 선택", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        JTextField searchField = new JTextField();
        JButton searchBtn = new JButton("검색");
        searchPanel.add(new JLabel("레시피 검색:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);

        DefaultListModel<Recipe> listModel = new DefaultListModel<>();
        allRecipes.forEach(listModel::addElement);

        JList<Recipe> recipeList = new JList<>(listModel);
        recipeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recipeList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Recipe) {
                    Recipe recipe = (Recipe) value;
                    setText(recipe.getName() + " [" + recipe.getCategory().getDisplayName() + "]");
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(recipeList);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton selectBtn = new JButton("선택");
        JButton cancelBtn = new JButton("취소");

        selectBtn.addActionListener(e -> {
            Recipe selectedRecipe = recipeList.getSelectedValue();
            if (selectedRecipe != null) {
                addRecipeToCell(row, col, selectedRecipe);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "레시피를 선택해주세요.",
                        "알림",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        searchBtn.addActionListener(e -> filterRecipes(searchField.getText(), allRecipes, listModel));
        searchField.addActionListener(e -> filterRecipes(searchField.getText(), allRecipes, listModel));

        buttonPanel.add(selectBtn);
        buttonPanel.add(cancelBtn);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void filterRecipes(String keyword, List<Recipe> allRecipes, DefaultListModel<Recipe> listModel) {
        listModel.clear();
        String lowerKeyword = keyword.toLowerCase().trim();

        for (Recipe recipe : allRecipes) {
            if (lowerKeyword.isEmpty() ||
                    recipe.getName().toLowerCase().contains(lowerKeyword) ||
                    recipe.getTitle().toLowerCase().contains(lowerKeyword)) {
                listModel.addElement(recipe);
            }
        }
    }

    private void displayRecipeInfo(int row, int col, Recipe recipe) {
        String[] mealType = {"아침", "점심", "저녁"};
        String dayHeader = table.getColumnName(col);
        recipeTitleLabel.setText(dayHeader + " - " + mealType[row]);

        StringBuilder content = new StringBuilder();
        content.append("레시피: ").append(recipe.getName()).append("\n\n");
        content.append("재료:\n");
        for (String detail : recipe.getDetails()) {
            content.append("- ").append(detail).append("\n");
        }
        content.append("\n조리시간: ").append(recipe.getTime()).append("\n");
        content.append("분량: ").append(recipe.getAmount());

        recipeContentArea.setText(content.toString());

        // 스크롤을 맨 위로 이동
        SwingUtilities.invokeLater(() -> {
            recipeContentArea.setCaretPosition(0);
        });
    }

    private void addRecipeToCell(int row, int col, Recipe recipe) {
        plannerPresenter.setRecipeAt(row, col, recipe);
        table.setValueAt(recipe.getName(), row, col);
        // 추가 후 바로 하단 패널에 정보 표시
        displayRecipeInfo(row, col, recipe);
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