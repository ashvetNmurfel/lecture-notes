package ru.spbau.lecturenotes.storage.firebase;

import com.google.firebase.firestore.ServerTimestamp;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;

import ru.spbau.lecturenotes.storage.identifiers.DocumentId;

public class FirebaseDocument {
    protected DocumentId id;
    protected FirebaseDocumentDataReference reference;
    @ServerTimestamp
    protected Date updateTimestamp;

    public FirebaseDocument() {
    }

    @NotNull
    public DocumentId getId() {
        return id;
    }

    @NotNull
    public FirebaseDocumentDataReference getReference() {
        return reference;
    }

    @NotNull
    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }
}
