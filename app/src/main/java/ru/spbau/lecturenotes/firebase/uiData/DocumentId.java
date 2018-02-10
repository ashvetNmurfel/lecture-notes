package ru.spbau.lecturenotes.firebase.uiData;

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

    public GroupId getGroupId() {
        return groupId;
    }

    public String getKey() {
        return key;
    }

    public String getFilename() {
        return filename;
    }
}
