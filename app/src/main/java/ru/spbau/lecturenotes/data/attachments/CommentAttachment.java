package ru.spbau.lecturenotes.data.attachments;

import org.jetbrains.annotations.NotNull;

public class CommentAttachment {
    protected final String attachmentId;
    protected final String author;
    protected CommentAttachment(@NotNull final String author) {
        // todo: replace with real data
        this.author = author;
        attachmentId = "";
    }
}
