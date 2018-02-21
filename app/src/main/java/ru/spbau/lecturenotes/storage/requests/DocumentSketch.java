package ru.spbau.lecturenotes.storage.requests;

import java.io.File;

public class DocumentSketch {
    protected File pdf;
    protected String filename;
    protected String description;

    public DocumentSketch(File pdf, String filename, String description) {
        this.pdf = pdf;
        this.filename = filename;
        this.description = description;
    }

    public File getPdf() {
        return pdf;
    }

    public String getFilename() {
        return filename;
    }

    public String getDescription() {
        return description;
    }
}
