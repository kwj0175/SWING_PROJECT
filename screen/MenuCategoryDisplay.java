package screen;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

// 데이터 클래스들
class MainSideDishes {
	private String title;
	private String img;
	void setTitle(String title) {
		this.title = title;
		img = title+".jpg";//<- 수정 필요
	}
	String getTitle() {
		return this.title;
	}
	String getImg() {
		return this.img;
	}
	public String toString() {
		return this.title;
	}
}

class RiceDishes {
	private String title;
	private String img;
	void setTitle(String title) {
		this.title = title;
		img = title + ".jpg";//<- 수정 필요
	}
	String getTitle() {
		return this.title;
	}
	String getImg() {
		return this.img;
	}
	public String toString() {
		return this.title;
	}
}

class SideDishes {
	private String title;
	private String img;
	void setTitle(String title) {
		this.title = title;
		img = title + ".jpg";//<- 수정 필요
	}
	String getTitle() {
		return this.title;
	}
	String getImg() {
		return this.img;
	}
	public String toString() {
		return this.title;
	}
}

class Soups {
	private String title;
	private String img;
	void setTitle(String title) {
		this.title = title;
		img = title + ".jpg";//<- 수정 필요
	}
	String getTitle() {
		return this.title;
	}
	String getImg() {
		return this.img;
	}
	public String toString() {
		return this.title;
	}
}
// 데이터 클래스 끝

class MainCategory {
	ArrayList<MainSideDishes> mainsideDish = new ArrayList<>();
	ArrayList<RiceDishes> riceDish = new ArrayList<>();
	ArrayList<SideDishes> sideDish = new ArrayList<>();
	ArrayList<Soups> soup = new ArrayList<>();
	// MenuCategoryDisplay t;
	
	Scanner openFile(String fileName) {
		Scanner fi = null;
		try {
			fi = new Scanner(new File(fileName), "UTF-8");
			
		}catch (Exception e) {
            System.err.println("경고: 파일을 찾을 수 없습니다: " + fileName + ". (" + e.getMessage() + ")");
            return null;
			// throw new RuntimeException(e);
		}
		return fi;
	}
	
	void readFile(String filename) {
		Scanner fi = openFile(filename);
        if (fi == null) {
            return; // 파일 열기에 실패했으면 종료
        }
		if (filename.equals("datasets/texts/main_side_dishes.txt")){
			while (fi.hasNextLine()) {
                // System.out.println("Reading line from main_side_dishes.txt");
				String line = fi.nextLine().trim();
				if(line.trim().isEmpty()) continue;
				String title = extractTitle(line);
				MainSideDishes m = new MainSideDishes();
				m.setTitle(title);
				mainsideDish.add(m);
			}
		}
		else if(filename.equals("datasets/texts/rice_dishes.txt")) {
			while (fi.hasNextLine()) {
				String line = fi.nextLine().trim();
				if(line.trim().isEmpty()) continue;
				String title = extractTitle(line);
				RiceDishes r = new RiceDishes();
				r.setTitle(title);
				riceDish.add(r);
			}
		}
		else if(filename.equals("datasets/texts/side_dishes.txt")) {
			while (fi.hasNextLine()) {
				String line = fi.nextLine().trim();
				if(line.trim().isEmpty()) continue;
				String title = extractTitle(line);
				SideDishes s = new SideDishes();
				s.setTitle(title);
				sideDish.add(s);
			}
		}
		else if(filename.equals("datasets/texts/soups.txt")) {
			while (fi.hasNextLine()) {
				String line = fi.nextLine().trim();
				if(line.trim().isEmpty()) continue;
				String title = extractTitle(line);
				Soups s = new Soups();
				s.setTitle(title);
				soup.add(s);
			}
		}
		fi.close();
	}
	
	String extractTitle(String line) {//<제목>[재료][]
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
	
	
	void run() {
		// 파일 읽기
	    readFile("datasets/texts/main_side_dishes.txt");
	    readFile("datasets/texts/rice_dishes.txt");
	    readFile("datasets/texts/side_dishes.txt");
	    readFile("datasets/texts/soups.txt");
	    
        System.out.println("MainSideDishes 로드된 항목 수: " + mainsideDish.size());
        System.out.println("RiceDishes 로드된 항목 수: " + riceDish.size());
        System.out.println("SideDishes 로드된 항목 수: " + sideDish.size());
        System.out.println("Soups 로드된 항목 수: " + soup.size());
	    // 읽은 파일 넘겨주기
	    // t = new MenuCategoryDisplay(mainsideDish, riceDish, sideDish, soup);

	    // t.setData(mainsideDish, riceDish, sideDish, soup);
	    // t.setVisible(true);
	    
	    // 읽은 데이터 확인용 출력
	    // System.out.println("===== Main Side Dishes =====");
	    // for (MainSideDishes m : mainsideDish) {
	    //     System.out.println(m.getTitle());
	    // }

	    // System.out.println("\n===== Rice Dishes =====");
	    // for (RiceDishes r : riceDish) {
	    //     System.out.println(r.getTitle());
	    // }

	    // System.out.println("\n===== Side Dishes =====");
	    // for (SideDishes s : sideDish) {
	    //     System.out.println(s.getTitle());
	    // }

	    // System.out.println("\n===== Soups =====");
	    // for (Soups so : soup) {
	    //     System.out.println(so.getTitle());
	    // }
	}
	
	// public static void main(String[] args) {
	// 	Main mymain = new Main();
	// 	mymain.run();
    //     SwingUtilities.invokeLater(() -> {
    //     if (mymain.t != null) {
    //         mymain.t.setVisible(true);
    //     }
    // });
	// }
}


public class MenuCategoryDisplay extends JFrame {

    private JTextField inputField;
    private JPanel cards; // CardLayout 영역
    private CardLayout cardLayout;

    // 데이터 저장
    private ArrayList<MainSideDishes> mainItems;
    private ArrayList<RiceDishes> riceItems;
    private ArrayList<SideDishes> sideItems;
    private ArrayList<Soups> soupItems;

    public MenuCategoryDisplay() {
        // 모든 리스트를 빈 리스트로 초기화
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
    
    public MenuCategoryDisplay(ArrayList<MainSideDishes> mainsideDish, ArrayList<RiceDishes> riceDish, 
    		ArrayList<SideDishes> sideDish, ArrayList<Soups> soup) {
        super("Menu Category Display");
        setSize(360, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

//        initData();
        
        //데이터 넘겨받기
        mainItems = mainsideDish;
    	riceItems = riceDish;
    	sideItems = sideDish;
    	soupItems = soup;
    	
        initComponents();
    }
    
    // void setData(ArrayList<MainSideDishes> mainsideDish, ArrayList<RiceDishes> riceDish, 
    // 		ArrayList<SideDishes> sideDish, ArrayList<Soups> soup) {
    // 	mainItems = mainsideDish;
    // 	riceItems = riceDish;
    // 	sideItems = sideDish;
    // 	soupItems = soup;
    // }
    

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
    private JScrollPane createScrollPanel(ArrayList<?> items) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,1,5,5));

        for(Object title : items) {
        	String titleString = String.valueOf(title);
        	
            JPanel menuPanel = new JPanel(new BorderLayout());
            menuPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            menuPanel.setBackground(new Color(245,245,245));

            JLabel label = new JLabel(titleString);
            label.setFont(new Font("맑은 고딕", Font.BOLD, 10));
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
    	MainCategory mymain = new MainCategory();
        mymain.run();
        SwingUtilities.invokeLater(() -> {
            MenuCategoryDisplay frame = new MenuCategoryDisplay(
                mymain.mainsideDish,   // 데이터 1
                mymain.riceDish,      // 데이터 2
                mymain.sideDish,      // 데이터 3
                mymain.soup           // 데이터 4
            );
            frame.setVisible(true);
    });
    }
}