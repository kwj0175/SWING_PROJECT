package src.repository;

import src.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUserRepository implements UserRepository {
    private final File file;

    public FileUserRepository(String fileName) {
        this.file = new File(fileName);
    }

    @Override
    public List<User> loadAll() {
        List<User> users = new ArrayList<>();

        if (!file.exists()) {
            System.out.println("users.txt 파일이 없어 새로 생성합니다.");
            return users;
        }

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                User u = new User();
                u.read(sc);
                if (u.getId() != null) {
                    users.add(u);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("파일 로드 중 오류 발생: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void save(User user) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }

        // 맨 끝에 한 줄 추가(append = true)
        try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {
            out.println(user.toString());   // User.read(Scanner)랑 포맷만 일치하면 됨
        }
    }
}
