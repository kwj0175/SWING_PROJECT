package screen;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MenuCategoryDisplay extends JFrame {

    private JTextField inputField;
    private JPanel cards; // CardLayout 영역
    private CardLayout cardLayout;

    // 테스트용 리스트
    private ArrayList<String> mainItems;
    private ArrayList<String> riceItems;
    private ArrayList<String> sideItems;
    private ArrayList<String> soupItems;

    public MenuCategoryDisplay() {
        super("Menu Category Display");
        setSize(360, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initData();
        initComponents();
    }

    private void initData() {
        mainItems = new ArrayList<>();
        riceItems = new ArrayList<>();
        sideItems = new ArrayList<>();
        soupItems = new ArrayList<>();

        for(int i=1; i<=20; i++) mainItems.add("메인" + i);
        for(int i=1; i<=20; i++) riceItems.add("밥류" + i);
        for(int i=1; i<=20; i++) sideItems.add("반찬" + i);
        for(int i=1; i<=20; i++) soupItems.add("국/찌개" + i);
    }

    private void initComponents() {
        // 상단 입력바
        JPanel topPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        JButton searchButton = new JButton("검색");
        topPanel.add(inputField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 카테고리 버튼
        JPanel categoryPanel = new JPanel(new GridLayout(1,4,5,5));
        String[] categories = {"메인", "밥류", "반찬", "국/찌개"};
        for(String cat : categories) {
            JButton btn = new JButton(cat);
            categoryPanel.add(btn);

            btn.addActionListener(e -> cardLayout.show(cards, cat));
        }
        add(categoryPanel, BorderLayout.AFTER_LAST_LINE);

        // CardLayout 영역
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(createScrollPanel(mainItems), "메인");
        cards.add(createScrollPanel(riceItems), "밥류");
        cards.add(createScrollPanel(sideItems), "반찬");
        cards.add(createScrollPanel(soupItems), "국/찌개");

        add(cards, BorderLayout.CENTER);

        // 검색 버튼 이벤트
        searchButton.addActionListener(e -> searchCurrentCard(inputField.getText().trim()));
    }

    // 스크롤 패널 생성
    private JScrollPane createScrollPanel(ArrayList<String> items) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,1,5,5));

        for(String title : items) {
            JPanel menuPanel = new JPanel(new BorderLayout());
            menuPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            menuPanel.setBackground(new Color(245,245,245));

            JLabel label = new JLabel(title);
            label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            menuPanel.add(label, BorderLayout.CENTER);

            menuPanel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    menuPanel.setBackground(new Color(220,220,250));
                }
                public void mouseExited(MouseEvent e) {
                    menuPanel.setBackground(new Color(245,245,245));
                }
            });

            panel.add(menuPanel);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    // 현재 보이는 카드에서 검색
    private void searchCurrentCard(String text) {
        Component current = getCurrentCard();
        if(current instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) current;
            JPanel panel = (JPanel) sp.getViewport().getView();
            for(Component comp : panel.getComponents()) {
                if(comp instanceof JPanel) {
                    JLabel label = (JLabel) ((JPanel) comp).getComponent(0);
                    String title = label.getText();
                    comp.setVisible(title.contains(text));
                }
            }
            panel.revalidate();
            panel.repaint();
        }
    }

    // 현재 보이는 카드 가져오기
    private Component getCurrentCard() {
        for(Component comp : cards.getComponents()) {
            if(comp.isVisible()) return comp;
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            test frame = new test();
            frame.setVisible(true);
        });
    }
}