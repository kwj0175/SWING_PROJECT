package entity;

import manager.Manageable;

import java.util.Scanner;

public class User implements Manageable {
    private String id;
    private String pw;
    private String name;

    public User() {}

    public User(String id, String password, String name) {
        this.id = id;
        this.pw = password;
        this.name = name;
    }


    public void read(Scanner sc) {
        String line = sc.nextLine();
        String[] parts = line.split(" \\| ");

        if (parts.length >= 3) {
            this.id = parts[0].trim();
            this.pw = parts[1].trim();
            this.name = parts[2].trim();
        }
    }

    @Override
    public String toString() {
        return String.join(" | ", id, pw, name);
    }

    public boolean matches(String kwd) {
        if (kwd == null) return false;
        return this.id.equals(kwd);
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }

    public String getName() {
        return name;
    }
}
