package ru.spbau.lecturenotes.storage;

public class User {
    protected String username;

    public User() {}
    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}