package ru.spbau.lecturenotes.data;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

import ru.spbau.lecturenotes.storage.identifiers.DocumentId;

public class PdfFileStorage implements Serializable {
    private final boolean isDirectory;
    private final String name;
    private final String info;
    private final DocumentId document;
    private final ArrayList<PdfFileStorage> substorages;

    public static PdfFileStorage createFile(final @NotNull String name,
                                            final @NotNull String info,
                                            final @NotNull DocumentId documentId) {
        return new PdfFileStorage(false, name, info, documentId,  null);
    }

    public static PdfFileStorage createDirectory(final @NotNull String name,
                                                 final @NotNull String info,
                                                 final @NotNull ArrayList<PdfFileStorage> substorages) {
        return new PdfFileStorage(true, name, info, null, substorages);
    }

    public static PdfFileStorage createDirectory(final @NotNull String name,
                                                 final @NotNull String info) {
        return new PdfFileStorage(true, name, info, null, new ArrayList<PdfFileStorage>());
    }

    private PdfFileStorage(boolean isDirectory, String name, String info,
                           DocumentId documentId,
                           ArrayList<PdfFileStorage> substorages) {
        this.isDirectory = isDirectory;
        this.substorages = substorages;
        this.document = documentId;
        this.info = info;
        this.name = name;
    }

    public void addSubstorage(final @NotNull PdfFileStorage newStorage) {
        if (isDirectory()) {
            substorages.add(newStorage);
        } else {
            throw new UnsupportedOperationException("Attempting to add substorage to a file storage");
        }
    }

    public ArrayList<PdfFileStorage> substorages() {
        return substorages;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isFile() {
        return isDirectory;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public DocumentId getDocument() {
        return document;
    }

}
