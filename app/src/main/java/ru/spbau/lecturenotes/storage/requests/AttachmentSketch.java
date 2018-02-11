package ru.spbau.lecturenotes.storage.requests;

import org.jetbrains.annotations.NotNull;

import ru.spbau.lecturenotes.storage.AttachmentType;

public class AttachmentSketch {
    protected String path;
    protected AttachmentType type;

    public AttachmentSketch(final @NotNull String attachmentFile,
                            final @NotNull AttachmentType type) {
        this.path = attachmentFile;
        this.type = type;
    }

    @NotNull
    public String getPath() {
        return path;
    }

    @NotNull
    public AttachmentType getAttachmentType() {
        return type;
    }
}
