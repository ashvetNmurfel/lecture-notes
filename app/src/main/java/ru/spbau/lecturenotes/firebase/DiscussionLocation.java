package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class DiscussionLocation {
    protected DocumentId documentId;
    protected int page;
    protected Rectangle rectangle;

    public DiscussionLocation(@NotNull final DocumentId documentId,
                              int page,
                              @Nullable Rectangle rectangle) {
        this.documentId = documentId;
        this.page = page;
        this.rectangle = rectangle;
    }

    @NotNull
    public DocumentId getDocumentId() {
        return documentId;
    }

    public int getPage() {
        return page;
    }

    @Nullable
    public Rectangle getRectangle() {
        return rectangle;
    }
}
