package ru.spbau.lecturenotes.storage.identifiers;

import org.jetbrains.annotations.NotNull;

public class GroupId {
    protected String key;
    protected String name;

    public GroupId(final @NotNull String key, final @NotNull String name) {
        this.key = key;
        this.name = name;
    }

    public GroupId() {
    }

    @NotNull
    public String getKey() {
        return key;
    }

    @NotNull
    public String getName() {
        return name;
    }
}
