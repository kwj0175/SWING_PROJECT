package src.service;

import src.entity.User;
import src.infrastructure.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final List<User> userList;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userList = new ArrayList<>(userRepository.loadAll());
    }

    public User login(String id, String password) {
        for (User user : userList) {
            if (user.getId().equals(id) && user.getPw().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public int signUp(String id, String password, String name) {
        if (findById(id) != null) return 1;

        User newUser = new User(id, password, name);

        try {
            userRepository.save(newUser);
        } catch (IOException e) {
            System.err.println("[ERROR] 사용자 정보 저장 실패: " + e.getMessage());
            e.printStackTrace();
            return 2;
        }
        userList.add(newUser);
        return 0;
    }

    private User findById(String id) {
        for (User user : userList) {
            if (user.matches(id)) {
                return user;
            }
        }
        return null;
    }
}
