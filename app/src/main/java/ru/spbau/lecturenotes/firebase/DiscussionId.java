package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

public class DiscussionId {
    protected DocumentId document;
    protected String key;

    @NotNull
    public DocumentId getDocument() {
        return document;
    }

    @NotNull
    public String getKey() {
        return key;
    }
}
