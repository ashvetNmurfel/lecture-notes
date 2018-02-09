package ru.spbau.lecturenotes.firebase;

public class NewDiscussionRequest {
    protected DocumentId documentId;
    protected DiscussionLocation location;
    protected Comment comment;

    public Comment getComment() {
        return comment;
    }

    public DocumentId getDocumentId() {
        return documentId;
    }
}
