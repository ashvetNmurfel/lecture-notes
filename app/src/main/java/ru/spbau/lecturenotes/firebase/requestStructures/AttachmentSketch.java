package ru.spbau.lecturenotes.firebase.requestStructures;

import org.jetbrains.annotations.NotNull;

public class AttachmentSketch {
    protected String path;

    public AttachmentSketch(String attachmentFile) {
        this.path = attachmentFile;
    }

    @NotNull
    public String getPath() {
        return path;
    }
}
