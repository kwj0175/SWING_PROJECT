package screen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class PlannerDisplay extends JFrame {
    private JTable table;
    private JLabel weekLabel;
    private int currentWeek;
    private int currentMonth;
    private int currentYear;

    public PlannerDisplay() {
        setTitle("식단 플래너");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //일단 창닫으면 종료
        setSize(360, 640); // 화면 크기
        setLocationRelativeTo(null);

        // 날짜 관련 설정
        LocalDate today = LocalDate.now();
        currentYear = today.getYear(); // 시작 연도 설정
        currentMonth = today.getMonthValue(); // 시작 월 설정
        currentWeek = today.get(WeekFields.of(Locale.getDefault()).weekOfMonth()); // 시작 주차 설정

        // 상단 패널 (이전주 / 현재주 / 다음주)
        JPanel topPanel = new JPanel();
        JButton homeBtn = new JButton("홈");// 좌상단 홈버튼
        JButton prevWeekBtn = new JButton("←");
        JButton nextWeekBtn = new JButton("→");
        weekLabel = new JLabel(getWeekText(), SwingConstants.CENTER);
        weekLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        
        //topPanel에 맞춰 버튼추가
        topPanel.add(homeBtn);
        topPanel.add(prevWeekBtn);
        topPanel.add(weekLabel);
        topPanel.add(nextWeekBtn);

        // 표 구성
        String[] columns = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};//임시값, updateTableHeaders() 메서드에서 업데이트됨
        String[] rows = {"아침", "점심", "저녁", "총칼로리"};
        

        DefaultTableModel model = new DefaultTableModel(rows.length, columns.length);
        model.setColumnIdentifiers(columns);
        table = new JTable(model);
        table.setRowHeight(40);
        for (int i = 0; i < rows.length; i++) {
            model.setValueAt(rows[i], i, 0);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // 가로 스크롤 활성화

        // 이전주 버튼 클릭 이벤트
        prevWeekBtn.addActionListener(event -> {
        	currentWeek--;
            if (currentWeek < 1) {
                currentMonth--; // 월 감소
                if (currentMonth < 1) {
                    currentYear--; // 연도 감소
                    currentMonth = 12; // 월을 12로 설정
                }
                currentWeek = 4; // 주차를 4로 설정
            }
            updateWeekLabel(); // 주차 라벨 업데이트
            updateTableHeaders(); // 테이블 헤더 업데이트
        });
        	
        // 다음주 버튼 클릭 이벤트
        nextWeekBtn.addActionListener(event -> {
        	currentWeek++;
            if (currentWeek > 4) {
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
        
        // 홈 버튼 클릭 이벤트 추가
        homeBtn.addActionListener(event -> {
            System.out.println("홈 버튼 클릭됨");
            // 홈 버튼 클릭 시 동작 추가 가능
        });

        // 배치
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        updateTableHeaders(); //초기 실행시 테이블 헤더 정상적으로 나타내도록 표시
        
        setVisible(true);
    }

    private String getWeekText() {
    	return currentYear + "년 " + currentMonth + "월 " + currentWeek + "주차";
    }

    private void updateWeekLabel() {
        weekLabel.setText(getWeekText());
    }
    
    private void updateTableHeaders() {
    	// 현재 주의 시작 날짜 계산 (월요일 기준) 이었는데 일요일 기준으로 변경어디서 된지 모르겠음ㅋㅋ;;
        LocalDate startOfWeek = LocalDate.of(currentYear, currentMonth, 1)
            .with(WeekFields.of(Locale.getDefault()).weekOfMonth(), currentWeek)
            .with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);

        // 요일별 날짜 계산 및 헤더 업데이트
        String[] columns = new String[7];
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            columns[i] = String.format("%02d/%02d (%s)", 
                date.getMonthValue(), 
                date.getDayOfMonth(), 
                getDayOfWeekKorean(date.getDayOfWeek().getValue()));
        }

        // 테이블 모델에 새로운 헤더 설정
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setColumnIdentifiers(columns);
	}
    
 // 요일을 한글로 변환하는 메서드
    private String getDayOfWeekKorean(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1: return "월";
            case 2: return "화";
            case 3: return "수";
            case 4: return "목";
            case 5: return "금";
            case 6: return "토";
            case 7: return "일";
            default: return "";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PlannerDisplay::new);
    }
}
