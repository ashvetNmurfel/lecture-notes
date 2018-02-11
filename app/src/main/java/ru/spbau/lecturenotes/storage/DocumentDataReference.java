package ru.spbau.lecturenotes.storage;

public class DocumentDataReference {
    protected int numberOfPages;
    protected DocumentDataType type;
    protected String storageReference;

    public DocumentDataReference(int numberOfPages, DocumentDataType type, String storageReference) {
        this.numberOfPages = numberOfPages;
        this.type = type;
        this.storageReference = storageReference;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public DocumentDataType getType() {
        return type;
    }

    public String getStorageReference() {
        return storageReference;
    }
}
