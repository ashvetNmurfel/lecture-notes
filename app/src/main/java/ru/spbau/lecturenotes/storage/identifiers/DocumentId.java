package ru.spbau.lecturenotes.storage.identifiers;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class DocumentId implements Serializable {
    protected GroupId groupId;
    protected String key;
    protected String filename;
    protected String description;

    public DocumentId() {
    }

    public DocumentId(@NotNull final GroupId groupId,
                      @NotNull final String key,
                      @NotNull final String filename,
                      @NotNull final String description) {
        this.groupId = groupId;
        this.key = key;
        this.filename = filename;
        this.description = description;
    }

    @NotNull
    public GroupId getGroupId() {
        return groupId;
    }

    @NotNull
    public String getKey() {
        return key;
    }

    @NotNull
    public String getFilename() {
        return filename;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DocumentId && Objects.equals(key, ((DocumentId) o).getKey());
    }
}
