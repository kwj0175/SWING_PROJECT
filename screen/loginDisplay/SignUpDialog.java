package screen.loginDisplay;

import manager.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpDialog  extends JDialog {
    private UserManager userManager;

    private JTextField idField;
    private JPasswordField pwField;
    private JTextField nameField;
    private JButton signUpButton;
    private JButton backButton;

    public SignUpDialog(JFrame parentFrame, UserManager manager) {
        super(parentFrame, "회원가입", true);
        this.userManager = manager;

        setLayout(null);
        setSize(400, 450);
        setLocationRelativeTo(parentFrame);

        // "회원가입" 타이틀 (상단)
        JLabel titleLabel = new JLabel("회원가입");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(100, 30, 200, 40);
        add(titleLabel);

        // ID (중간 좌측 1)
        JLabel idLabel = new JLabel("ID");
        idLabel.setBounds(50, 120, 80, 30);
        add(idLabel);
        idField = new JTextField(20);
        idField.setBounds(140, 120, 180, 30);
        add(idField);

        // 비밀번호 (중간 좌측 2)
        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setBounds(50, 170, 80, 30);
        add(pwLabel);
        pwField = new JPasswordField(20);
        pwField.setBounds(140, 170, 180, 30);
        add(pwField);

        // 이름 (중간 좌측 3)
        JLabel nameLabel = new JLabel("이름");
        nameLabel.setBounds(50, 220, 80, 30);
        add(nameLabel);
        nameField = new JTextField(20);
        nameField.setBounds(140, 220, 180, 30);
        add(nameField);

        // "뒤로가기" 버튼 (좌측 하단)
        backButton = new JButton("뒤로가기");
        backButton.setBounds(50, 350, 120, 40);
        add(backButton);

        // "회원가입하기" 버튼 (우하단)
        signUpButton = new JButton("회원가입하기");
        signUpButton.setBounds(230, 350, 120, 40);
        add(signUpButton);

        // "뒤로가기" 버튼 리스너
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // [회원가입하기] 버튼 리스너
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });
    }

    /**
     * 회원가입 버튼 클릭 시 실행될 로직
     */
    private void handleSignUp() {
        String id = idField.getText();
        String pw = new String(pwField.getPassword());
        String name = nameField.getText();

        if (id.isEmpty() || pw.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "모든 항목을 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int result = userManager.signUp(id, pw, name);

        switch (result) {
            case 0:
                JOptionPane.showMessageDialog(this, "회원가입 성공! 로그인 해주세요.", "성공", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                break;
            case 1:
                JOptionPane.showMessageDialog(this, "이미 사용 중인 ID입니다.", "ID 중복 오류", JOptionPane.ERROR_MESSAGE);
                break;
            case 2:
            default:
                JOptionPane.showMessageDialog(this, "시스템 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }
}
