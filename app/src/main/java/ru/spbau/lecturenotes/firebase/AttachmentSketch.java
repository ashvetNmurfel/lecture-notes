package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class AttachmentSketch {
    protected File attachmentFile;

    public AttachmentSketch(File attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    public File getAttachmentFile() {
        return attachmentFile;
    }
}
