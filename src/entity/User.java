package src.entity;

import java.util.Scanner;

public class User {
    private String id;
    private String pw;
    private String name;

    public User() {}

    public User(String id, String password, String name) {
        this.id = id;
        this.pw = password;
        this.name = name;
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
