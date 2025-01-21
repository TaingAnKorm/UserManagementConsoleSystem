package UserManagementConsoleSystem.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private final int id;
    private final String uuid;
    private String name;
    private String email;
    private boolean isDeleted;

    public User(int id, String uuid, String name, String email, boolean isDeleted) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | UUID: " + uuid + " | Name: " + name + " | Email: " + email + " | isDeleted: " + isDeleted + " |";
    }
}
