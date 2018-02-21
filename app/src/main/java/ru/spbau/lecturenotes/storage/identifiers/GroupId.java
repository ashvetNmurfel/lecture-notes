package ru.spbau.lecturenotes.storage.identifiers;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class GroupId implements Serializable {
    protected String key;
    protected String name;
    protected String description;

    public GroupId(final @NotNull String key,
                   final @NotNull String name,
                   final @NotNull String description) {
        this.key = key;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }
}
