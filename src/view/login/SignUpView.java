package src.view.login;

import src.service.UserService;
import src.view.utils.ScreenHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpView extends JDialog {
    private final UserService userService;

    private JTextField idField;
    private JPasswordField pwField;
    private JTextField nameField;
    private JButton signUpButton;
    private JButton backButton;

    public SignUpView(JFrame parentFrame, UserService manager) {
        super(parentFrame, "회원가입", true);
        this.userService = manager;

        JPanel form = buildForm();
        setContentPane(form);
        pack();
        setLocationRelativeTo(parentFrame);

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

    private void handleSignUp() {
        String id = idField.getText();
        String pw = new String(pwField.getPassword());
        String name = nameField.getText();

        if (id.isEmpty() || pw.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "모든 항목을 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int result = userService.signUp(id, pw, name);

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

    private JPanel buildForm() {
        JLabel titleLabel = ScreenHelper.setText("회원가입", 24);
        JLabel idLabel = ScreenHelper.setText("ID");
        JLabel pwLabel = ScreenHelper.setText("PW");
        JLabel nameLabel = ScreenHelper.setText("이름");

        idField = new JTextField(20);
        pwField = new JPasswordField(20);
        nameField = new JTextField(20);

        backButton = ScreenHelper.primaryButton("뒤로가기", 14);
        signUpButton = ScreenHelper.primaryButton("회원가입", 14);

        int TEXT_FIELD_WIDTH = 180;
        int CONTAINER_GAP = 30;
        int ROW_GAP = 20;

        JPanel form = ScreenHelper.cardPanel();
        GroupLayout formLayout = ScreenHelper.groupLayout(form);

        formLayout.setHorizontalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(titleLabel)
                        .addGroup(formLayout.createSequentialGroup()
                                .addComponent(idLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(idField,
                                        GroupLayout.PREFERRED_SIZE,
                                        TEXT_FIELD_WIDTH,
                                        GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(formLayout.createSequentialGroup()
                                .addComponent(pwLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pwField,
                                        GroupLayout.PREFERRED_SIZE,
                                        TEXT_FIELD_WIDTH,
                                        GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(formLayout.createSequentialGroup()
                                .addComponent(nameLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(nameField,
                                        GroupLayout.PREFERRED_SIZE,
                                        TEXT_FIELD_WIDTH,
                                        GroupLayout.PREFERRED_SIZE))
                        .addGroup(formLayout.createSequentialGroup()
                                .addComponent(backButton)
                                .addGap(CONTAINER_GAP)
                                .addComponent(signUpButton)
                        )
        );

        formLayout.setVerticalGroup(
                formLayout.createSequentialGroup()
                        .addComponent(titleLabel)
                        .addGap(CONTAINER_GAP)
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(idLabel)
                                .addComponent(idField))
                        .addGap(ROW_GAP)
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(pwLabel)
                                .addComponent(pwField))
                        .addGap(ROW_GAP)
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(nameLabel)
                                .addComponent(nameField))
                        .addGap(CONTAINER_GAP)
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(backButton)
                                .addComponent(signUpButton))
                        .addGap(CONTAINER_GAP)
        );

        formLayout.linkSize(SwingConstants.HORIZONTAL, idLabel, pwLabel, nameLabel);
        formLayout.linkSize(SwingConstants.HORIZONTAL, backButton, signUpButton);

        return form;
    }
}