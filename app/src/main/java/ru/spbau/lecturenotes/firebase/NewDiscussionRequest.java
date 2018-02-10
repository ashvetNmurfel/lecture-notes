package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

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
