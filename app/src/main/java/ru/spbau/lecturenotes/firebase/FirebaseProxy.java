package ru.spbau.lecturenotes.firebase;

import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.ServerTimestamp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.util.EventListener;

public class FirebaseProxy implements DatabaseInterface {
    static FirebaseProxy INSTANCE = new FirebaseProxy();

    @Override
    public Document getDocument(DocumentId document) {
        return null;
    }

    @Override
    public Discussion getDiscussion(DiscussionId discussion) {
        return null;
    }

    @Override
    public Attachment getAttachment(AttachmentId attachment) {
        return null;
    }

    @Override
    public DiscussionId createDiscussion(NewDiscussionRequest request) {
        return null;
    }

    @Override
    public Discussion addComment(AddCommentRequest request) {
        return null;
    }

    @Override
    public ListenerRegistration setDocumentListListener(EventListener listener) {
        return null;
    }

    @Override
    public ListenerRegistration setDocumentListener(DocumentId document, EventListener listener) {
        return null;
    }

    @Override
    public ListenerRegistration setDiscussionListener(DiscussionId discussion, EventListener listener) {
        return null;
    }

    protected FirebaseAttachment getFirebaseAttachment(AttachmentId attachment) {
        return null;
    }

    static public class FirebaseAttachment {
        protected AttachmentId id;
        protected String storageReference;
        protected String type;
        @ServerTimestamp
        protected Date creationTimestamp;

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

    static public class FirebaseComment {
        protected CommentId id;
        protected String parent;
        protected FirebaseCommentContent content;
        protected User author;
        @ServerTimestamp
        protected Date creationTimestamp;
        protected boolean isEdited;
        @ServerTimestamp
        protected Date editTimestamp;

        public FirebaseComment() {

        }

        @NotNull
        public CommentId getId() {
            return id;
        }

        @NotNull
        public String getParent() {
            return parent;
        }

        @NotNull
        public FirebaseCommentContent getContent() {
            return content;
        }

        @NotNull
        public User getAuthor() {
            return author;
        }

        @NotNull
        public Date getCreationTimestamp() {
            return creationTimestamp;
        }

        public boolean isEdited() {
            return isEdited;
        }

        @NotNull
        public Date getEditTimestamp() {
            return editTimestamp;
        }
    }

    static public class FirebaseCommentContent {
        protected String text;
        protected FirebaseAttachment[] attachments;

        public FirebaseCommentContent() {
        }

        public FirebaseCommentContent(@NotNull final CommentContent commentContent) {
            text = commentContent.getText();
            attachments = new FirebaseAttachment[commentContent.getAttachmentsIds().length];
            for (int i = 0; i < attachments.length; i++) {
                attachments[i] = INSTANCE.getFirebaseAttachment(commentContent.getAttachmentsIds()[i]);
            }
        }

        @NotNull
        public String getText() {
            return text;
        }

        @NotNull
        public FirebaseAttachment[] getAttachments() {
            return attachments;
        }
    }

    static public class FirebaseDiscussion {
        protected DiscussionLocation location;
        protected String key;
        protected String status;
        @ServerTimestamp
        protected Date timestamp;

        public FirebaseDiscussion() {
        }

        public FirebaseDiscussion(@NotNull final Discussion discussion) {
            location = discussion.getLocation();
            key = discussion.discussionId.getKey();
            status = discussion.getStatus().toString();
        }

        @NotNull
        public DiscussionLocation getLocation() {
            return location;
        }

        @NotNull
        public String getKey() {
            return key;
        }

        @NotNull
        public String getStatus() {
            return status;
        }

        @NotNull
        public Date getTimestamp() {
            return timestamp;
        }
    }

    static public class FirebaseDocument {
        protected DocumentId id;
        protected String filename;
        protected String storageReference;
        @ServerTimestamp
        protected Date updateTimestamp;

        public FirebaseDocument() {
        }

        @NotNull
        public DocumentId getId() {
            return id;
        }

        @NotNull
        public String getFilename() {
            return filename;
        }

        @NotNull
        public String getStorageReference() {
            return storageReference;
        }

        @NotNull
        public Date getUpdateTimestamp() {
            return updateTimestamp;
        }
    }



}
