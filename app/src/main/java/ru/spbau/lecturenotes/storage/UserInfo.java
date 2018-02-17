package ru.spbau.lecturenotes.storage;

public class UserInfo {
    protected String id;
    protected String name;
    protected String email;

    public UserInfo(String uid, String displayName, String email) {
        this.id = uid;
        this.name = displayName;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
