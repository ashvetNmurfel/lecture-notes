package ru.spbau.lecturenotes.storage.firebase;

import com.google.firebase.firestore.ServerTimestamp;

import java.sql.Date;

import ru.spbau.lecturenotes.storage.Attachment;
import ru.spbau.lecturenotes.storage.AttachmentId;

public class FirebaseAttachment {
    protected AttachmentId id;
    protected String storageReference;
    protected String type;
    @ServerTimestamp
    protected Date creationTimestamp;

    public FirebaseAttachment(Attachment attachment) {
        this.id = attachment.getAttachmentId();
        this.storageReference = attachment.getReference();
        this.type = attachment.getType().toString();
        this.creationTimestamp = null;
    }

    public FirebaseAttachment() {
    }

    public AttachmentId getId() {
        return id;
    }

    public String getStorageReference() {
        return storageReference;
    }

    public String getType() {
        return type;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }
}
