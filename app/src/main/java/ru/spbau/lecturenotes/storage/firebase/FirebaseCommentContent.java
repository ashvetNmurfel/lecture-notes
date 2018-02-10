package ru.spbau.lecturenotes.storage.firebase;

import org.jetbrains.annotations.NotNull;

public class FirebaseCommentContent {
    protected String text;
    protected FirebaseAttachment[] attachments;

    public FirebaseCommentContent() {
    }

    @NotNull
    public String getText() {
        return text;
    }

    @NotNull
    public FirebaseAttachment[] getAttachments() {
        return attachments;
    }
}
