package screen;

import screen.planner.PlannerDisplay;

import javax.swing.*;

public class MainDisplay extends JFrame {

    public MainDisplay() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //일단 창닫으면 종료
        setSize(360, 640);
        setLocationRelativeTo(null);

        PlannerDisplay plannerDisplay = new PlannerDisplay();
        add(plannerDisplay);
        setVisible(true);
    }

    public void run() {

    }
}
