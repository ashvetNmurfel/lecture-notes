package ru.spbau.lecturenotes.firebase.requestStructures;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.spbau.lecturenotes.firebase.uiData.User;

public class CommentSketch {
    protected String text;
    protected List<AttachmentSketch> attachments;
    protected User author;

    public CommentSketch(@NotNull String text, @NotNull List<AttachmentSketch> attachments, @NotNull User author) {
        this.text = text;
        this.attachments = attachments;
        this.author = author;
    }

    @NotNull
    public String getText() {
        return text;
    }

    @NotNull
    public List<AttachmentSketch> getAttachments() {
        return attachments;
    }

    @NotNull
    public User getAuthor() {
        return author;
    }
}
