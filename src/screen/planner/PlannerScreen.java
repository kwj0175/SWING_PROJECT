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
    private final String[] ROWS = {"아침", "점심", "저녁"};
    private final PlannerPresenter plannerPresenter;
    private final MainScreen mainScreen;
    private final RecipeManager recipeManager;

    private JTable table;
    private JLabel weekLabel;
    private JLabel recipeTitleLabel;
    private JTextArea recipeContentArea;
    private JButton prevWeekBtn;
    private JButton nextWeekBtn;
    private JPanel recipePanel;

    private PlannerOverlay overlayPanel;
    private boolean isAddMode = false;

    public PlannerScreen(MainScreen mainScreen, RecipeManager recipeManager) {
        this.plannerPresenter = new PlannerPresenter();
        this.mainScreen = mainScreen;
        this.recipeManager = recipeManager;

        setLayout(new GridBagLayout());
        setOpaque(true);
        JPanel form = buildForm();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        add(form, c);

        prevWeekBtnAction(prevWeekBtn);
        nextWeekBtnAction(nextWeekBtn);

        loadWeekData();
    }

    private void loadWeekData() {
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
        resetRecipeAlarm();

        table.revalidate();
        table.repaint();
    }

    private void resetRecipeAlarm() {
        recipeTitleLabel.setText("레시피를 선택하세요");
        recipeContentArea.setText("표의 셀을 클릭하면 레시피 정보가 여기에 표시됩니다.");
    }

    public void startOverlayAddMode(Recipe recipe) {
        this.isAddMode = true;
        recipeDetailPanelVisibility();
        showRecipeOverlay(recipe);
    }

    private void showRecipeOverlay(Recipe recipe) {
        if (overlayPanel == null) {
            overlayPanel = new PlannerOverlay(recipe, e -> endOverlayAddMode());

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.fill = GridBagConstraints.BOTH;
            add(overlayPanel, c);
        } else {
            overlayPanel.setCurrentRecipe(recipe);
            overlayPanel.setVisible(true);
        }

        setComponentZOrder(overlayPanel, 0);
        revalidate();
        repaint();
    }

    private void endOverlayAddMode() {
        isAddMode = false;

        if (overlayPanel != null) {
            overlayPanel.setVisible(false);
        }

        recipeDetailPanelVisibility();
        revalidate();
        repaint();
    }

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
        JPanel detailPanel = buildDetailPanel();

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

    private JPanel buildDetailPanel() {
        recipePanel = new JPanel();
        recipePanel.setLayout(new BorderLayout());
        recipePanel.setPreferredSize(new Dimension(360, 300));

        Color color = UIManager.getColor("Panel.borderColor");
        recipePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(color),
                        "레시피 상세정보",
                        0, 0, new Font("맑은 고딕", Font.BOLD, 12)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        recipeTitleLabel = new JLabel();
        recipeContentArea = new JTextArea();

        resetRecipeAlarm();
        recipeTitleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        recipeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        recipeTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));

        recipeContentArea.setEditable(false);
        recipeContentArea.setLineWrap(true);
        recipeContentArea.setWrapStyleWord(true);
        recipeContentArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12));

        JScrollPane recipeScrollPane = new JScrollPane(recipeContentArea);

        recipePanel.add(recipeTitleLabel, BorderLayout.NORTH);
        recipePanel.add(recipeScrollPane, BorderLayout.CENTER);
        return recipePanel;
    }

    private JScrollPane buildTable(JPanel topPanel) {
        weekLabel = new JLabel(plannerPresenter.getWeekText(), SwingConstants.CENTER);
        weekLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        buildButtons(topPanel);

        String[] columns = plannerPresenter.getWeekHeaders();
        DefaultTableModel model = new DefaultTableModel(ROWS.length, columns.length);
        model.setColumnIdentifiers(columns);
        table = new JTable(model);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);

        setupTableInteraction();
        table.setRowHeight(55);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    private void buildButtons(JPanel topPanel) {
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

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
            loadWeekData();
        });
    }

    private void nextWeekBtnAction(JButton button) {
        button.addActionListener(event -> {
            plannerPresenter.moveNextWeek();
            updateWeekLabel();
            updateTableHeaders();
            loadWeekData();
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

        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (overlayPanel == null || !isAddMode) {
                    return;
                }

                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (row < 0 || col < 0) {
                    overlayPanel.setHighlightRect(null);
                    return;
                }

                Rectangle cellRect = table.getCellRect(row, col, true);
                Rectangle overlayRect = SwingUtilities.convertRectangle(
                        table,
                        cellRect,
                        overlayPanel
                );
                overlayPanel.setHighlightRect(overlayRect);
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (col < 0 || row < 0) return;

                if (isAddMode && overlayPanel != null) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        Recipe recipeToAdd = overlayPanel.getCurrentRecipe();
                        addRecipeToCell(row, col, recipeToAdd);
                        endOverlayAddMode();
                        return;
                    }
                }

                Recipe existingRecipe = plannerPresenter.getRecipeAt(row, col);

                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (existingRecipe != null) {
                        if (e.getClickCount() == 2) {
                            mainScreen.displayRecipeDetail(existingRecipe);
                        }
                        else if (e.getClickCount() == 1) {
                            displayRecipeInfo(row, col, existingRecipe);
                        }
                    } else {
                        showRecipeSelectionDialog(row, col);
                    }
                }

                if (SwingUtilities.isRightMouseButton(e)) {
                    if (isAddMode) return;
                    if (existingRecipe != null) {
                        JPopupMenu popup = new JPopupMenu();
                        JMenuItem deleteItem = new JMenuItem("삭제하기");
                        JMenuItem viewDetailItem = new JMenuItem("상세보기");

                        deleteItem.addActionListener(ae -> {
                            table.setValueAt("", row, col);
                            plannerPresenter.removeRecipeAt(row, col);
                            resetRecipeAlarm();
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

        ActionListener searchAction = e -> {
            String keyword = searchField.getText();
            List<Recipe> filtered = plannerPresenter.filterRecipes(keyword, allRecipes);

            listModel.clear();
            for (Recipe r : filtered) {
                listModel.addElement(r);
            }
        };

        searchBtn.addActionListener(searchAction);
        searchField.addActionListener(searchAction);

        buttonPanel.add(selectBtn);
        buttonPanel.add(cancelBtn);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void displayRecipeInfo(int row, int col, Recipe recipe) {
        String dayHeader = table.getColumnName(col);
        recipeTitleLabel.setText(dayHeader + " - " + ROWS[row]);

        StringBuilder content = new StringBuilder();
        content.append("레시피: ").append(recipe.getName()).append("\n\n");
        content.append("재료:\n");
        for (String detail : recipe.getDetails()) {
            content.append("- ").append(detail).append("\n");
        }
        content.append("\n조리시간: ").append(recipe.getTime()).append("\n");
        content.append("분량: ").append(recipe.getAmount());

        recipeContentArea.setText(content.toString());

        SwingUtilities.invokeLater(() -> {
            recipeContentArea.setCaretPosition(0);
        });
    }

    private void addRecipeToCell(int row, int col, Recipe recipe) {
        plannerPresenter.setRecipeAt(row, col, recipe);
        table.setValueAt(recipe.getName(), row, col);
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