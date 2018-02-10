package ru.spbau.lecturenotes.storage.requests;

import org.jetbrains.annotations.NotNull;

import ru.spbau.lecturenotes.storage.DocumentId;

public class NewDiscussionRequest {
    protected DocumentId documentId;
    protected DiscussionSketch discussion;

    public NewDiscussionRequest(DocumentId documentId, DiscussionSketch discussion) {
        this.documentId = documentId;
        this.discussion = discussion;
    }

    @NotNull
    public DiscussionSketch getDiscussion() {
        return discussion;
    }

    @NotNull
    public DocumentId getDocumentId() {
        return documentId;
    }
}
