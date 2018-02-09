package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

public class DocumentId {
    protected GroupId groupId;
    protected String key;

    public DocumentId(@NotNull final GroupId groupId,
                      @NotNull final String key) {
        this.groupId = groupId;
        this.key = key;
    }

    public GroupId getGroupId() {
        return groupId;
    }

    public String getKey() {
        return key;
    }
}
