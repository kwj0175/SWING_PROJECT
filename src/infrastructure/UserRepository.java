package src.infrastructure;

import src.entity.User;

import java.io.IOException;
import java.util.List;

public interface UserRepository {
    List<User> loadAll();
    void save(User user) throws IOException;
}
