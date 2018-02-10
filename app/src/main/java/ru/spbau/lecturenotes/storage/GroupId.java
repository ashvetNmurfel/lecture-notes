package ru.spbau.lecturenotes.storage;

public class GroupId {
    protected String key;
    protected String name;

    public GroupId(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public GroupId() {
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
