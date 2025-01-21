package UserManagementConsoleSystem.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserModel {
    private final Map<String, User> usersMap = new HashMap<>();
    private int nextId = 1;

    public User createUser(String name, String email) {
        String uuid = UUID.randomUUID().toString();
        int id = nextId++;
        User user = new User(id, uuid, name, email, false);
        usersMap.put(uuid, user);
        return user;
    }

    public User findUserByUUID(String uuid) {
        return usersMap.get(uuid);
    }

    public boolean updateUser(String uuid, String newName, String newEmail, boolean isDeleted) {
        User user = usersMap.get(uuid);
        if (user != null) {
            if (newName != null && !newName.isEmpty()) user.setName(newName);
            if (newEmail != null && !newEmail.isEmpty()) user.setEmail(newEmail);
            user.setDeleted(isDeleted);
            return true;
        }
        return false;
    }

    public boolean deleteUser(String uuid) {
        User user = usersMap.get(uuid);
        if (user != null) {
            user.setDeleted(true);
            return true;
        }
        return false;
    }

    public Collection<User> getAllUsers() {
        return usersMap.values();
    }
}
