package ru.spbau.lecturenotes.storage.requests;

import java.io.InputStream;

public class DocumentSketch {
    protected InputStream pdf;
    protected String filename;
    protected String description;

    public DocumentSketch(InputStream pdf, String filename, String description) {
        this.pdf = pdf;
        this.filename = filename;
        this.description = description;
    }

    public InputStream getPdf() {
        return pdf;
    }

    public String getFilename() {
        return filename;
    }

    public String getDescription() {
        return description;
    }
}
