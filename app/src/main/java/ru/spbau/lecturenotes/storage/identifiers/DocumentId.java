package ru.spbau.lecturenotes.storage.identifiers;

import org.jetbrains.annotations.NotNull;

public class DocumentId {
    protected GroupId groupId;
    protected String key;
    protected String filename;

    public DocumentId(@NotNull final GroupId groupId,
                      @NotNull final String key,
                      @NotNull final String filename) {
        this.groupId = groupId;
        this.key = key;
        this.filename = filename;
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
}
