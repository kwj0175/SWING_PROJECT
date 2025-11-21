package src.screen.login;

import src.entity.User;
import src.manager.UserManager;

public class LoginPresenter {
    private final LoginScreen view;
    private final UserManager userManager;

    public LoginPresenter(LoginScreen view, UserManager userManager) {
        this.view = view;
        this.userManager = userManager;
    }

    public void onLoginSubmitted(String id, String password) {
        if (id == null || id.isBlank() || password == null || password.isBlank()) {
            view.showLoginError("ID와 비밀번호를 모두 입력하세요.");
            return;
        }

        User loggedInUser = userManager.login(id, password);

        if (loggedInUser == null) {
            view.showLoginError("ID 또는 비밀번호가 틀립니다.");
        } else {
            view.onLoginSuccess(loggedInUser);
        }
    }

}
