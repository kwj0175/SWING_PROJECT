package src.screen.login;

import src.entity.User;
import src.manager.UserManager;
import src.screen.MainScreen;
import src.screen.utils.ScreenHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JPanel {
    private final UserManager userManager;
    private final MainScreen mainScreen;
    private final LoginPresenter loginPresenter;

    private JTextField idField;
    private JPasswordField pwField;
    private JButton loginButton;
    private JButton signUpButton;

    public LoginScreen(MainScreen mainScreen, UserManager userManager) {
        this.mainScreen = mainScreen;
        this.userManager = userManager;
        this.loginPresenter = new LoginPresenter(this, userManager);

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel form = buildForm();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = c.gridy = 0;
        add(form, c);

        loginEvent();
    }

    private void loginEvent() {
        ActionListener loginListener = e -> {
            String id = idField.getText();
            String pw = new String(pwField.getPassword());
            loginPresenter.onLoginSubmitted(id, pw);
        };

        pwField.addActionListener(loginListener);
        loginButton.addActionListener(loginListener);

        // [회원가입] 버튼 리스너
        signUpButton.addActionListener(e -> {
            SignUpScreen signUpScreen = new SignUpScreen(mainScreen, userManager);
            signUpScreen.setVisible(true);
        });
    }

    private JPanel buildForm() {
        JLabel title = ScreenHelper.setText("MySmartRecipe", 26);
        JLabel sub = ScreenHelper.setText("오늘 뭐 먹지?", 20);
        JLabel idLabel = ScreenHelper.setText("ID");
        JLabel pwLabel = ScreenHelper.setText("PW");

        idField = new JTextField(20);
        pwField = new JPasswordField(20);

        loginButton = ScreenHelper.primaryButton("로그인", 14);
        signUpButton = ScreenHelper.primaryButton("회원가입", 14);

        JPanel form = ScreenHelper.darkCardPanel();
        GroupLayout formLayout = ScreenHelper.groupLayout(form);

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
                                .addComponent(idField,
                                        GroupLayout.PREFERRED_SIZE,
                                        TEXT_FIELD_WIDTH,
                                        GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(formLayout.createSequentialGroup()                        // PW 행
                                .addComponent(pwLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pwField,
                                        GroupLayout.PREFERRED_SIZE,
                                        TEXT_FIELD_WIDTH,
                                        GroupLayout.PREFERRED_SIZE)
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
                                .addComponent(idLabel)
                                .addComponent(idField))
                        .addGap(ROW_GAP) // ID 줄 아래 12px
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(pwLabel)
                                .addComponent(pwField))
                        .addGap(CONTAINER_GAP) // PW 줄 아래 16px
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(loginButton)
                                .addComponent(signUpButton))
                        .addGap(CONTAINER_GAP)
        );

        formLayout.linkSize(SwingConstants.HORIZONTAL, idLabel, pwLabel);
        formLayout.linkSize(SwingConstants.HORIZONTAL, loginButton, signUpButton);

        return form;
    }

    public void showLoginError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "로그인 실패",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void onLoginSuccess(User loggedInUser) {
        JOptionPane.showMessageDialog(
                this,
                loggedInUser.getName() + "님 환영합니다."
        );
        mainScreen.displayHomeScreen(loggedInUser);
    }

}

