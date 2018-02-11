package ru.spbau.lecturenotes.storage.identifiers;

import org.jetbrains.annotations.NotNull;

import ru.spbau.lecturenotes.storage.DiscussionLocation;

public class DiscussionId {
    protected DocumentId documentId;
    protected String key;
    protected DiscussionLocation location;

    public DiscussionId(final @NotNull DocumentId documentId,
                        final @NotNull String key,
                        final @NotNull DiscussionLocation location) {
        this.documentId = documentId;
        this.key = key;
        this.location = location;
    }

    @NotNull
    public GroupId getGroupId() {
        return documentId.getGroupId();
    }

    @NotNull
    public DocumentId getDocumentId() {
        return documentId;
    }

    @NotNull
    public String getKey() {
        return key;
    }

    @NotNull
    public DiscussionLocation getLocation() {
        return location;
    }
}
