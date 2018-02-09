package ru.spbau.lecturenotes.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.ServerTimestamp;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.EventListener;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FirebaseProxy implements DatabaseInterface {
    static FirebaseProxy INSTANCE = new FirebaseProxy();
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public Document getDocument(final @NotNull DocumentId document) {
        final DocumentReference docRef = db
                .collection("groups")
                .document(document.getGroupId().getKey())
                .collection("docs")
                .document(document.getKey());
        final FirebaseDocument[] fdoc = new FirebaseDocument[1];
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        Log.d(TAG,  "Got document snapshot for Document: " + documentSnapshot.getId());
                        fdoc[0] = documentSnapshot.toObject(FirebaseDocument.class);
                    } else {
                        Log.d(TAG, "Error: could not find snapshot for Document: " + document.getKey());
                    }
                } else {
                    Log.d(TAG, "Attempt to get document snapshot for Document " +
                            document.getKey() +
                            " failed with", task.getException());
                }
            }
        });
        if (fdoc[0] == null) {
            return null;
        }

        return FirebaseObjectsConvertor.toDocument(fdoc[0], getDiscussionsList(document));
    }

    @Override
    public Discussion getDiscussion(final DiscussionId discussion) {
        final DocumentReference docRef = db
                .collection("groups")
                .document(discussion.getDocumentId().getKey())
                .collection("discussions")
                .document(discussion.getKey());
        final FirebaseDiscussion[] fdiscussion = new FirebaseDiscussion[1];
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        Log.d(TAG,  "Got document snapshot for Discussion: " + documentSnapshot.getId());
                        fdiscussion[0] = documentSnapshot.toObject(FirebaseDiscussion.class);
                    } else {
                        Log.d(TAG, "Error: could not find snapshot for Discussion: " + discussion.getKey());
                    }
                } else {
                    Log.d(TAG, "Attempt to get document snapshot for Discussion " +
                            discussion.getKey() +
                            " failed with", task.getException());
                }
            }
        });
        if (fdiscussion[0] == null) {
            return null;
        }
        return FirebaseObjectsConvertor.toDiscussion(fdiscussion[0], getCommentsList(discussion));
    }

    @Override
    public Attachment getAttachment(AttachmentId attachment) {
        return null;
    }

    @Override
    public List<DocumentId> getDocumentsList(GroupId group) {
        return null;
    }

    @Override
    public List<GroupId> getGroupsList() {
        return null;
    }

    @Override
    public List<CommentId> getCommentsList(DiscussionId discussionId) {
        return null;
    }

    @Override
    public List<DiscussionId> getDiscussionsList(DocumentId documentId) {
        return null;
    }

    @Override
    public DiscussionId addDiscussion(NewDiscussionRequest request) {
        return null;
    }

    @Override
    public Discussion addComment(AddCommentRequest request) {
        return null;
    }

    @Override
    public ListenerRegistration setDocumentListListener(EventListener listener, GroupId group) {
        return null;
    }

    @Override
    public ListenerRegistration setGroupsListListener(EventListener listener) {
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
        protected DiscussionId id;
        protected String status;
        @ServerTimestamp
        protected Date timestamp;

        public FirebaseDiscussion() {
        }

        public DiscussionId getId() {
            return id;
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
        public String getStorageReference() {
            return storageReference;
        }

        @NotNull
        public Date getUpdateTimestamp() {
            return updateTimestamp;
        }
    }
}
