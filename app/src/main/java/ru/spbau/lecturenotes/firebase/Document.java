package ru.spbau.lecturenotes.firebase;

import java.util.List;

class Document {
    DocumentId id;
    List<DiscussionId> discussions;
    // +  Document reference (CloudStorage Sync?)

    public DocumentId getId() {
        return id;
    }

    public List<DiscussionId> getDiscussions() {
        return discussions;
    }
}