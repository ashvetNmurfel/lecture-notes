package ru.spbau.lecturenotes.storage.requests;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentSketch {
    protected String text;
    protected ArrayList<AttachmentSketch> attachments;

    public CommentSketch(@NotNull String text, @NotNull List<AttachmentSketch> attachments) {
        this.text = text;
        this.attachments = new ArrayList<>(attachments);
    }

    public CommentSketch(@NotNull String text) {
        this.text = text;
        this.attachments = new ArrayList<>();
    }

    public void addAttachments(final @NotNull AttachmentSketch... attachments) {
        Collections.addAll(this.attachments, attachments);
    }

    @NotNull
    public String getText() {
        return text;
    }

    @NotNull
    public ArrayList<AttachmentSketch> getAttachments() {
        return attachments;
    }
}
