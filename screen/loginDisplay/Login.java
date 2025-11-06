package screen.loginDisplay;

import entity.User;
import manager.UserManager;
import screen.HomeDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private UserManager userManager = new UserManager();

    private JLabel imageLabel;

    private JLabel subTitleLabel;
    private JLabel titleLabel2;

    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel pwLabel;
    private JTextField idField;
    private JPasswordField pwField;
    private JButton loginButton;
    private JButton signUpButton;

    public Login() {
        setTitle("로그인");
        // --- 창 크기 : 800x500 고정 ---
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // --- 배경 이미지 로드 및 크기 조정 ---
        ImageIcon foodIcon = new ImageIcon(getClass().getResource("/HomeImage.jpg"));
        imageLabel = new JLabel();
        int frameWidth = 800;
        int frameHeight = 500;

        if (foodIcon.getIconWidth() > 0) {
            Image scaledImage = foodIcon.getImage().getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            imageLabel.setText("이미지 로드 실패! /HomeImage.jpg 확인");
            imageLabel.setOpaque(true);
            imageLabel.setBackground(Color.WHITE);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        imageLabel.setBounds(0, 0, frameWidth, frameHeight);
        layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER);


        // --- 컨트롤 (상위 레이어) ---
        int controlX = 450;

        subTitleLabel = new JLabel("오늘 뭐 먹지?");
        subTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        subTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        subTitleLabel.setBounds(controlX, 80, 300, 30);
        layeredPane.add(subTitleLabel, JLayeredPane.PALETTE_LAYER);

        titleLabel2 = new JLabel("MySmartRecipe");
        titleLabel2.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel2.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel2.setBounds(controlX, 110, 300, 40);
        layeredPane.add(titleLabel2, JLayeredPane.PALETTE_LAYER);

        idLabel = new JLabel("ID:");
        idLabel.setBounds(controlX, 180, 80, 30);
        layeredPane.add(idLabel, JLayeredPane.PALETTE_LAYER);

        idField = new JTextField(20);
        idField.setBounds(controlX + 90, 180, 210, 30);
        layeredPane.add(idField, JLayeredPane.PALETTE_LAYER);

        pwLabel = new JLabel("PW:");
        pwLabel.setBounds(controlX, 220, 80, 30);
        layeredPane.add(pwLabel, JLayeredPane.PALETTE_LAYER);

        pwField = new JPasswordField(20);
        pwField.setBounds(controlX + 90, 220, 210, 30);
        layeredPane.add(pwField, JLayeredPane.PALETTE_LAYER);

        loginButton = new JButton("로그인");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setBounds(controlX, 300, 300, 40);
        layeredPane.add(loginButton, JLayeredPane.PALETTE_LAYER);

        signUpButton = new JButton("회원가입");
        signUpButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        signUpButton.setBounds(controlX, 350, 300, 40);
        layeredPane.add(signUpButton, JLayeredPane.PALETTE_LAYER);
        // [로그인] 버튼 리스너
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String pw = new String(pwField.getPassword());

                User loggedInUser = userManager.login(id, pw);

                if (loggedInUser != null) {
                    JOptionPane.showMessageDialog(null, loggedInUser.getName() + "님 환영합니다.");
                    new HomeDisplay(loggedInUser).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "ID 또는 비밀번호가 틀립니다.");
                }
            }
        });

        // [회원가입] 버튼 리스너
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignUpDialog dialog = new SignUpDialog(Login.this, userManager);
                dialog.setVisible(true);
            }
        });
    }

    // (테스트용 main)
    public static void main(String[] args) {
        new Login().setVisible(true);
    }
}


