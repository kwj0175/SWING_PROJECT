package manager;

import java.util.Scanner;

public interface Manageable {

    void read(Scanner scanner);
    String toString();
    boolean matches(String kwd);
    String getId();
}