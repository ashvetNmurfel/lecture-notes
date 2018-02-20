package ru.spbau.lecturenotes.storage;

import java.io.File;

public class LocalFile<T> {
    protected T identifier;
    protected File file;

    public LocalFile(T identifier, File file) {
        this.identifier = identifier;
        this.file = file;
    }

    public T getIdentifier() {
        return identifier;
    }

    public File getFile() {
        return file;
    }
}
