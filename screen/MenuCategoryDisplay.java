package screen;
import javax.swing.*;
public class MenuCategoryDisplay {
    void display() {
        JFrame frame = new JFrame("Menu Category Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640);
        frame.setVisible(true);

        frame.setLayout(null);

        JLabel inputLabel = new JLabel("검색어 입력:");
        // (x좌표, y좌표, 폭, 높이)
        inputLabel.setBounds(20, 20, 100, 30);
        frame.add(inputLabel);
    }
    public static void main(String[] args) {
        MenuCategoryDisplay display = new MenuCategoryDisplay();
        display.display();
    }
}
