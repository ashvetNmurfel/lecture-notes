package ru.spbau.lecturenotes.storage.firebase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FirebaseCommentContent {
    protected String text;
    protected ArrayList<FirebaseAttachment> attachments;

    public FirebaseCommentContent() {
    }

    @NotNull
    public String getText() {
        return text;
    }

    @NotNull
    public ArrayList<FirebaseAttachment> getAttachments() {
        return attachments;
    }
}
