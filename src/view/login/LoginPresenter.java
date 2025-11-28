package src.view.login;

import src.entity.User;
import src.service.UserService;

public class LoginPresenter {
    private final LoginView view;
    private final UserService userService;

    public LoginPresenter(LoginView view, UserService userService) {
        this.view = view;
        this.userService = userService;
    }

    public void onLoginSubmitted(String id, String password) {
        if (id == null || id.isBlank() || password == null || password.isBlank()) {
            view.showLoginError("ID와 비밀번호를 모두 입력하세요.");
            return;
        }

        User loggedInUser = userService.login(id, password);

        if (loggedInUser == null) {
            view.showLoginError("ID 또는 비밀번호가 틀립니다.");
        } else {
            view.onLoginSuccess(loggedInUser);
        }
    }

}
