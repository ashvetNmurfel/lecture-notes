package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

public class DocumentId {
    protected String key;

    public DocumentId(@NotNull final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
