package ru.spbau.lecturenotes.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class FirebaseProxy implements DatabaseInterface {
    static FirebaseProxy INSTANCE = new FirebaseProxy();
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    public Document getDocument(final @NotNull DocumentId document) {
        Log.i(TAG, "Attempting to get Document " + document.getKey());
        final DocumentReference docRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(document.getGroupId().getKey())
                .collection(FirebaseCollections.DOCS.str())
                .document(document.getKey());
        final FirebaseDocument[] fdoc = new FirebaseDocument[1];
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        Log.i(TAG,  "Got document snapshot for Document: " + documentSnapshot.getId());
                        fdoc[0] = documentSnapshot.toObject(FirebaseDocument.class);
                    } else {
                        Log.w(TAG, "Error: could not find snapshot for Document: " + document.getKey());
                    }
                } else {
                    Log.e(TAG, "Attempt to get document snapshot for Document " +
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
        Log.i(TAG, "Attempting to get Discussion" + discussion.getKey());
        final DocumentReference docRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(discussion.getDocumentId().getKey())
                .collection(FirebaseCollections.DOCS.str())
                .document(discussion.getKey());
        final FirebaseDiscussion[] fdiscussion = new FirebaseDiscussion[1];
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        Log.i(TAG,  "Got document snapshot for Discussion: " + documentSnapshot.getId());
                        fdiscussion[0] = documentSnapshot.toObject(FirebaseDiscussion.class);
                    } else {
                        Log.w(TAG, "Error: could not find snapshot for Discussion: " + discussion.getKey());
                    }
                } else {
                    Log.e(TAG, "Attempt to get document snapshot for Discussion " +
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
    public Attachment getAttachment(final AttachmentId attachment) {
        Log.i(TAG, "Attempting to get Attachment " + attachment.getKey());
        final DocumentReference docRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(attachment.getGroupId().getKey())
                .collection(FirebaseCollections.DOCS.str())
                .document(attachment.getDocumentId().getKey())
                .collection(FirebaseCollections.DISCUSSIONS.str())
                .document(attachment.getDiscussion().getKey())
                .collection(FirebaseCollections.ATTACHMENTS.str())
                .document(attachment.getKey());
        final FirebaseAttachment[] fattachment = new FirebaseAttachment[1];
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        Log.i(TAG,  "Got document snapshot for Attachment: " + documentSnapshot.getId());
                        fattachment[0] = documentSnapshot.toObject(FirebaseAttachment.class);
                    } else {
                        Log.w(TAG, "Error: could not find snapshot for Attachment: " + attachment.getKey());
                    }
                } else {
                    Log.e(TAG, "Attempt to get document snapshot for Attachment" +
                            attachment.getKey() +
                            " failed with", task.getException());
                }
            }
        });
        return FirebaseObjectsConvertor.toAttachment(fattachment[0]);
    }

    @Override
    public List<DocumentId> getDocumentsList(final GroupId group) {
        Log.i(TAG, "Attempting to get Document list for Group" + group.getKey());
        final CollectionReference collRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(group.getKey())
                .collection(FirebaseCollections.DOCS.str());
        final List<DocumentId> documentIds = new ArrayList<>();
        collRef.orderBy("updateTimestamp").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.i(TAG, "Added snapshot for Document: " + document.getId()
                                + " to the list of ids");
                        documentIds.add(document.toObject(FirebaseDocument.class).getId());
                    }
                } else {
                    Log.e(TAG, "Error getting documents in Group: " + group.getKey(), task.getException());
                }
            }
        });
        return documentIds;
    }

    @Override
    public List<GroupId> getGroupsList() {
        Log.i(TAG, "Attempting to get Group list");
        final CollectionReference collRef = db
                .collection(FirebaseCollections.GROUPS.str());
        final List<GroupId> groupIds = new ArrayList<>();
        collRef
                .whereEqualTo("permissions." + FirebaseAuth.getInstance().getUid(), true)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.i(TAG, "Added snapshot for Group: " + document.getId()
                                + " to the list of ids");
                        groupIds.add(document.toObject(FirebaseGroup.class).getGroupId());
                    }
                } else {
                    Log.e(TAG, "Error getting list of groups", task.getException());
                }
            }
        });
        return groupIds;
    }

    @Override
    public List<CommentId> getCommentsList(final DiscussionId discussionId) {
        Log.i(TAG, "Attempting to get Comments list for Discussion " + discussionId.getKey());
        final CollectionReference collRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(discussionId.getGroupId().getKey())
                .collection(FirebaseCollections.DOCS.str())
                .document(discussionId.getDocumentId().getKey())
                .collection(FirebaseCollections.DISCUSSIONS.str())
                .document(discussionId.getKey())
                .collection(FirebaseCollections.COMMENTS.str());
        final List<CommentId> commentIds = new ArrayList<>();
        collRef.orderBy("creationTimestamp").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.i(TAG, "Added snapshot for Comment: " + document.getId()
                                + " to the list of ids");
                        commentIds.add(document.toObject(FirebaseComment.class).getId());
                    }
                } else {
                    Log.e(TAG, "Error getting Comments for Discussion: " + discussionId.getKey(),
                            task.getException());
                }
            }
        });
        return commentIds;
    }

    @Override
    public List<DiscussionId> getDiscussionsList(final DocumentId documentId) {
        Log.i(TAG, "Attempting to get Discussions list for Document" + documentId.getKey());
        final CollectionReference collRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(documentId.getGroupId().getKey())
                .collection(FirebaseCollections.DOCS.str())
                .document(documentId.getKey())
                .collection(FirebaseCollections.DISCUSSIONS.str());
        final List<DiscussionId> discussionIds = new ArrayList<>();
        collRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.i(TAG, "Added snapshot for Disscussion: " + document.getId()
                                + " to the list of ids");
                        discussionIds.add(document.toObject(FirebaseDiscussion.class).getId());
                    }
                } else {
                    Log.e(TAG, "Error getting Discussion for : " + documentId.getKey(),
                            task.getException());
                }
            }
        });
        return discussionIds;
    }

    @Override
    public Discussion addDiscussion(final NewDiscussionRequest request) throws FileNotFoundException {
        Log.i(TAG, "Attempting to add Discussion to the Document " + request.documentId.getKey());
        final DocumentReference docRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(request.getDocumentId().getGroupId().getKey())
                .collection(FirebaseCollections.DOCS.str())
                .document(request.getDocumentId().getKey())
                .collection(FirebaseCollections.COMMENTS.str())
                .document();
        final FirebaseDiscussion discussion = new FirebaseDiscussion();
        discussion.id  = new DiscussionId(request.getDocumentId(), docRef.getId(), request.getDiscussion().getLocation());
        discussion.status = DiscussionStatus.UNKNOWN.toString();
        discussion.timestamp = null;
        docRef.set(discussion).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "Discussion " + docRef.getId() +
                        " was successfully written to Document " + request.getDocumentId().getKey());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to add a new discussion to Document " + request.getDocumentId().getKey(), e);
                discussion.id = null;
            }
        });
        if (discussion.id == null) {
            return null;
        }

        return addComment(new AddCommentRequest(discussion.id, request.getDiscussion().getComment()));
    }

    @Override
    public Discussion addComment(final AddCommentRequest request) throws FileNotFoundException {
        Log.i(TAG, "Attempting to add Comment to the Discussion" + request.getDiscussionId().getKey());
        DocumentReference docRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(request.getDiscussionId().getGroupId().getKey())
                .collection(FirebaseCollections.DOCS.str())
                .document(request.getDiscussionId().getDocumentId().getKey())
                .collection(FirebaseCollections.DISCUSSIONS.str())
                .document(request.getDiscussionId().getKey())
                .collection(FirebaseCollections.COMMENTS.str())
                .document();


        final FirebaseComment comment = new FirebaseComment();
        comment.id = new CommentId(request.getDiscussionId(), docRef.getId());
        comment.author = request.getComment().getAuthor();
        comment.creationTimestamp = null;
        comment.editTimestamp = null;
        comment.content.text = request.getComment().getText();
        comment.content.attachments = new FirebaseAttachment[request.getComment().getAttachments().size()];

        {
            int cnt = 0;
            for (AttachmentSketch attachment : request.getComment().getAttachments()) {
                Attachment newAttachment = addAttachment(new NewAttachmentRequest(
                        new CommentId(request.getDiscussionId(), docRef.getId()),
                        attachment));
                if (newAttachment == null)
                    return null;
                comment.content.attachments[cnt++] = new FirebaseAttachment(newAttachment);
            }
        }
        docRef.set(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "New Comment was added to Discussion: " + request.getDiscussionId().getKey());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "An error occurred while trying to add a comment to Discussion " + request.getDiscussionId().getKey());
                comment.id = null;
            }
        });

        if (comment.id == null) {
            return null;
        }
        return getDiscussion(request.getDiscussionId());
    }

    @Override
    public Attachment addAttachment(final NewAttachmentRequest request) throws FileNotFoundException {
        final String attachmentPath = "attachments/" + request.getCommentId().getGroupId().getKey() +
                FirebaseAuth.getInstance().getUid() + "/" + UUID.randomUUID();
        StorageReference storageReference = storage.getReference();
        StorageReference attachmentReference = storageReference.child(attachmentPath);
        // Todo: create thread to upload in parallel
        // Todo: add listeners
        attachmentReference.putStream(new FileInputStream(new File(request.getAttachment().getPath())))
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Could not upload image " + request.getAttachment().getPath()
                        + " to a storage location: " + attachmentPath, e);
            }
        });
        DocumentReference docRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(request.getCommentId().getGroupId().getKey())
                .collection(FirebaseCollections.DOCS.str())
                .document(request.getCommentId().getDocumentId().getKey())
                .collection(FirebaseCollections.DISCUSSIONS.str())
                .document(request.getCommentId().getDiscussion().getKey())
                .collection(FirebaseCollections.ATTACHMENTS.str())
                .document();
        final FirebaseAttachment firebaseAttachment = new FirebaseAttachment();
        firebaseAttachment.creationTimestamp = null;
        firebaseAttachment.type = null;
        firebaseAttachment.id = new AttachmentId(request.getCommentId(), docRef.getId());
        firebaseAttachment.storageReference = attachmentPath;
        docRef.set(firebaseAttachment).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseAttachment.id = null;
                Log.e(TAG, "Failed to add Attachment for the path: " + request.getAttachment().getPath(), e);
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "Loaded Attachment to the Discussion: " + request.getCommentId().getDiscussion().getKey());
            }
        });
        if (firebaseAttachment.id == null) {
            return null;
        }
        return getAttachment(firebaseAttachment.getId());
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

    static public class FirebaseAttachment {
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

    static public class FirebaseGroup {
        protected GroupId groupId;
        @ServerTimestamp
        protected Date creationTimestamp;

        public FirebaseGroup() {
        }

        public GroupId getGroupId() {
            return groupId;
        }

        public Date getCreationTimestamp() {
            return creationTimestamp;
        }
    }
}
