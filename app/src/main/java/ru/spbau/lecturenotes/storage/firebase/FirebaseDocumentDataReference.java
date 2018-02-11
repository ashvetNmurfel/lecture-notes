package ru.spbau.lecturenotes.storage.firebase;

import org.jetbrains.annotations.NotNull;

public class FirebaseDocumentDataReference {
    protected int numberOfPages;
    protected String type;
    protected String storageReference;

    public FirebaseDocumentDataReference() {
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    @NotNull
    public String getType() {
        return type;
    }

    @NotNull
    public String getStorageReference() {
        return storageReference;
    }
}
