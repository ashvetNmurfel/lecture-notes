package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

public class DiscussionId {
    protected DocumentId document;
    protected String key;
    protected DiscussionLocation location;

    public DiscussionId(DocumentId document, String key, DiscussionLocation location) {
        this.document = document;
        this.key = key;
        this.location = location;
    }

    @NotNull
    public GroupId getGroupId() {
        return document.getGroupId();
    }

    @NotNull
    public DocumentId getDocumentId() {
        return document;
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
