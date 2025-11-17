package screen.login;

import entity.User;
import manager.UserManager;
import screen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JPanel {
    private final UserManager userManager = new UserManager();

    private JTextField idField;
    private JPasswordField pwField;
    private JButton loginButton;
    private JButton signUpButton;

    public LoginScreen(MainScreen mainScreen) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel form = buildForm();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = c.gridy = 0;

        add(form, c);

        // [로그인] 버튼 리스너
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String pw = new String(pwField.getPassword());

                User loggedInUser = userManager.login(id, pw);

                if (loggedInUser != null) {
                    JOptionPane.showMessageDialog(null, loggedInUser.getName() + "님 환영합니다.");
                    mainScreen.displayHomeScreen(loggedInUser);
                } else {
                    JOptionPane.showMessageDialog(null, "ID 또는 비밀번호가 틀립니다.");
                }
            }
        });

        // [회원가입] 버튼 리스너
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignUpScreen signUpScreen = new SignUpScreen(mainScreen, userManager);
                signUpScreen.setVisible(true);
            }
        });
    }

    private JPanel buildForm() {
        JLabel title = setLabelWithFont(new JLabel("MySmartRecipe"), "SansSerif", Font.BOLD, 26);
        JLabel sub = setLabelWithFont(new JLabel("오늘 뭐 먹지?"), "SansSerif", Font.BOLD, 20);

        JLabel idLabel = new JLabel("ID");
        idField = new JTextField(20);
        JLabel pwLabel = new JLabel("PW");
        pwField = new JPasswordField(20);

        loginButton = new JButton("로그인");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        signUpButton = new JButton("회원가입");
        signUpButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        JPanel form = new JPanel();
        form.setOpaque(true);
        form.setBackground(Color.DARK_GRAY);
        form.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        GroupLayout formLayout = new GroupLayout(form);
        form.setLayout(formLayout);
        formLayout.setAutoCreateGaps(false);
        formLayout.setAutoCreateContainerGaps(true);

        int TEXT_FIELD_WIDTH = 180;
        int CONTAINER_GAP = 30;
        int ROW_GAP = 20;

        formLayout.setHorizontalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(title)
                        .addComponent(sub)
                        .addGroup(formLayout.createSequentialGroup()                        // ID 행
                                .addComponent(idLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(idField, GroupLayout.PREFERRED_SIZE, TEXT_FIELD_WIDTH, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(formLayout.createSequentialGroup()                        // PW 행
                                .addComponent(pwLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pwField, GroupLayout.PREFERRED_SIZE, TEXT_FIELD_WIDTH, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(formLayout.createSequentialGroup()                        // 버튼 행(가로는 순차)
                                .addComponent(loginButton)
                                .addGap(CONTAINER_GAP)
                                .addComponent(signUpButton)
                        )
        );

        formLayout.setVerticalGroup(
                formLayout.createSequentialGroup()
                        .addComponent(title)
                        .addComponent(sub)
                        .addGap(CONTAINER_GAP)
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(idLabel).addComponent(idField))
                        .addGap(ROW_GAP) // ID 줄 아래 12px
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(pwLabel).addComponent(pwField))
                        .addGap(CONTAINER_GAP) // PW 줄 아래 16px
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(loginButton).addComponent(signUpButton))
                        .addGap(CONTAINER_GAP)
        );

        formLayout.linkSize(SwingConstants.HORIZONTAL, idLabel, pwLabel);
        formLayout.linkSize(SwingConstants.HORIZONTAL, loginButton, signUpButton);

        return form;
    }

    private JLabel setLabelWithFont(JLabel label, String font, int style, int size) {
        label.setFont(new Font(font, style, size));
        return label;
    }

}

