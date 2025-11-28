package src.infrastructure;

import src.entity.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

        try (Scanner sc = new Scanner(file, StandardCharsets.UTF_8)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                User u = parseUser(line);
                if (u != null) {
                    users.add(u);
                }
            }
        } catch (IOException e) {
            System.out.println("파일 로드 중 오류 발생: " + e.getMessage());
        }
        return users;
    }

    private User parseUser(String line) {
        if (line == null || line.trim().isEmpty()) return null;

        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            System.err.println("[WARN] 잘못된 사용자 라인: " + line);
            return null;
        }

        String id = parts[0].trim();
        String pw = parts[1].trim();
        String name = parts[2].trim();

        if (id.isEmpty()) {
            System.err.println("[WARN] ID 비어있는 사용자 라인: " + line);
            return null;
        }

        return new User(id, pw, name);
    }

    private String formatUser(User user) {
        return String.join(" | ", user.getId(), user.getPw(), user.getName());
    }

    @Override
    public void save(User user) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {
            out.println(user.toString());
        }
    }
}
