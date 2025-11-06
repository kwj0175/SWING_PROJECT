package manager;

import entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class UserManager {
    private ArrayList<User> userList = new ArrayList<>();
    private static final String FILENAME = "users.txt";

    public UserManager() {
        loadUsersFromFile();
    }

    /**
     * [로그인 로직]
     * ID와 PW를 받아 리스트와 비교.
     * @return 로그인 성공 시 User 객체, 실패 시 null
     */
    public User login(String id, String password) {
        for (User user : userList) {
            if (user.getId().equals(id) && user.getPw().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * [회원가입 로직]
     * ID, PW, 이름을 받아 중복 검사 후 파일에 저장.
     * @return 0: 성공, 1: ID 중복, 2: 파일 쓰기 오류
     */
    public int signUp(String id, String password, String name) {
        if (findById(id) != null) {
            return 1; // ID 중복
        }

        User newUser = new User(id, password, name);

        try (PrintWriter out = new PrintWriter(new FileWriter(FILENAME, true))) {
            out.println(newUser.toString());
        } catch (IOException e) {
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


    private void loadUsersFromFile() {
        File file = new File(FILENAME);
        if (!file.exists()) {
            System.out.println("users.txt 파일이 없어 새로 생성합니다.");
            return;
        }

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                User u = new User();
                u.read(sc);
                if (u.getId() != null) {
                    userList.add(u);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("파일 로드 중 오류 발생: " + e.getMessage());
        }
    }
}
