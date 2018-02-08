package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

class FirebaseAttachmentReference {
    protected String type;
    protected String reference;

    public FirebaseAttachmentReference() {
    }

    @NotNull
    public String getType() {
        return type;
    }

    @NotNull
    public String getReference() {
        return reference;
    }
}
