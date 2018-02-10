package ru.spbau.lecturenotes.storage.firebase;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.UUID;

import ru.spbau.lecturenotes.storage.Comment;
import ru.spbau.lecturenotes.storage.DatabaseInterface;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.requests.AddCommentRequest;
import ru.spbau.lecturenotes.storage.requests.AttachmentSketch;
import ru.spbau.lecturenotes.storage.requests.NewAttachmentRequest;
import ru.spbau.lecturenotes.storage.requests.NewDiscussionRequest;
import ru.spbau.lecturenotes.storage.Attachment;
import ru.spbau.lecturenotes.storage.AttachmentId;
import ru.spbau.lecturenotes.storage.CommentId;
import ru.spbau.lecturenotes.storage.Discussion;
import ru.spbau.lecturenotes.storage.DiscussionId;
import ru.spbau.lecturenotes.storage.DiscussionStatus;
import ru.spbau.lecturenotes.storage.Document;
import ru.spbau.lecturenotes.storage.DocumentId;
import ru.spbau.lecturenotes.storage.GroupId;

import static android.content.ContentValues.TAG;

public class FirebaseProxy implements DatabaseInterface {
    static FirebaseProxy INSTANCE = new FirebaseProxy();
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    public void getDocument(final @NotNull DocumentId document, final ResultListener<Document> listener) {
        Log.i(TAG, "Attempting to get Document " + document.getKey());
        final DocumentReference docRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(document.getGroupId().getKey())
                .collection(FirebaseCollections.DOCS.str())
                .document(document.getKey());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        Log.i(TAG, "Got document snapshot for Document: " + documentSnapshot.getId());
                        FirebaseDocument firebaseDocument = documentSnapshot.toObject(FirebaseDocument.class);
                        getDiscussionsList(document, new LoadDiscussionListResultListener(
                                firebaseDocument, listener));
                    } else {
                        Log.w(TAG, "Error: could not find snapshot for Document: " + document.getKey());
                        listener.onError(new IllegalArgumentException("...."));
                    }
                } else {
                    Log.e(TAG, "Attempt to get document snapshot for Document " +
                            document.getKey() +
                            " failed with", task.getException());
                    listener.onError(task.getException());
                }
            }
        });
    }


    private static class LoadDiscussionListResultListener implements ResultListener<List<DiscussionId>> {
        public LoadDiscussionListResultListener(FirebaseDocument document, ResultListener<Document> consumer) {
            this.consumer = consumer;
            this.document = document;
        }

        private ResultListener<Document> consumer;
        private FirebaseDocument document;

        @Override
        public void onResult(List<DiscussionId> result) {
            consumer.onResult(FirebaseObjectsConvertor.toDocument(document, result));
        }

        @Override
        public void onError(Throwable error) {
            consumer.onError(error);
        }
    }


    @Override
    public void getDiscussion(final DiscussionId discussion, final @NotNull ResultListener<Discussion> listener) {
        Log.i(TAG, "Attempting to get Discussion" + discussion.getKey());
        final DocumentReference docRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(discussion.getDocumentId().getKey())
                .collection(FirebaseCollections.DOCS.str())
                .document(discussion.getKey());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        Log.i(TAG, "Got document snapshot for Discussion: " + documentSnapshot.getId());
                        getCommentsList(discussion,
                                new LoadCommentsListResultListener(documentSnapshot.toObject(FirebaseDiscussion.class), listener));
                    } else {
                        Log.w(TAG, "Error: could not find snapshot for Discussion: " + discussion.getKey());
                        listener.onError(new IllegalArgumentException("...."));
                    }
                } else {
                    Log.e(TAG, "Attempt to get document snapshot for Discussion " +
                            discussion.getKey() + " failed with", task.getException());
                    listener.onError(task.getException());
                }
            }
        });
    }

    private static class LoadCommentsListResultListener implements ResultListener<List<CommentId>> {
        protected FirebaseDiscussion discussion;
        protected ResultListener<Discussion> listener;

        public LoadCommentsListResultListener(FirebaseDiscussion discussion, ResultListener<Discussion> listener) {
            this.discussion = discussion;
            this.listener = listener;
        }

        @Override
        public void onResult(List<CommentId> result) {
            listener.onResult(FirebaseObjectsConvertor.toDiscussion(discussion, result));
        }

        @Override
        public void onError(Throwable error) {
            listener.onError(error);
        }
    }

    @Override
    public void getAttachment(final AttachmentId attachment, final @NotNull ResultListener<Attachment> listener) {
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
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        Log.i(TAG, "Got document snapshot for Attachment: " + documentSnapshot.getId());
                        listener.onResult(FirebaseObjectsConvertor
                                .toAttachment(documentSnapshot.toObject(FirebaseAttachment.class)));
                    } else {
                        Log.w(TAG, "Error: could not find snapshot for Attachment: " + attachment.getKey());
                        listener.onError(new IllegalArgumentException("...."));
                    }
                } else {
                    Log.e(TAG, "Attempt to get document snapshot for Attachment" +
                            attachment.getKey() +
                            " failed with", task.getException());
                    listener.onError(task.getException());
                }
            }
        });
    }

    @Override
    public void getDocumentsList(final GroupId group, final ResultListener<List<DocumentId>> listener) {
        Log.i(TAG, "Attempting to get Document list for Group" + group.getKey());
        final CollectionReference collRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(group.getKey())
                .collection(FirebaseCollections.DOCS.str());
        collRef.orderBy("updateTimestamp").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    final List<DocumentId> result = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.i(TAG, "Added snapshot for Document: " + document.getId()
                                + " to the list of ids");
                        result.add(document.toObject(FirebaseDocument.class).getId());
                    }
                    listener.onResult(result);
                } else {
                    listener.onError(task.getException());
                }
            }
        });
    }

    @Override
    public void getGroupsList(final @NotNull ResultListener<List<GroupId>> listener) {
        Log.i(TAG, "Attempting to get Group list");
        final CollectionReference collRef = db
                .collection(FirebaseCollections.GROUPS.str());
        collRef
                .whereEqualTo("permissions." + FirebaseAuth.getInstance().getUid(), true)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    final List<GroupId> result = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.i(TAG, "Added snapshot for Group: " + document.getId()
                                + " to the list of ids");
                        result.add(document.toObject(FirebaseGroup.class).getGroupId());
                    }
                    listener.onResult(result);
                } else {
                    Log.e(TAG, "Error getting list of groups", task.getException());
                    listener.onError(task.getException());
                }
            }
        });
    }

    @Override
    public void getCommentsList(final DiscussionId discussionId, final @NotNull ResultListener<List<CommentId>> listener) {
        Log.i(TAG, "Attempting to get Comments list for Discussion " + discussionId.getKey());
        final CollectionReference collRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(discussionId.getGroupId().getKey())
                .collection(FirebaseCollections.DOCS.str())
                .document(discussionId.getDocumentId().getKey())
                .collection(FirebaseCollections.DISCUSSIONS.str())
                .document(discussionId.getKey())
                .collection(FirebaseCollections.COMMENTS.str());
        collRef.orderBy("creationTimestamp").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    final List<CommentId> result = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.i(TAG, "Added snapshot for Comment: " + document.getId()
                                + " to the list of ids");
                        result.add(document.toObject(FirebaseComment.class).getId());
                    }
                    listener.onResult(result);
                } else {
                    Log.e(TAG, "Error getting Comments for Discussion: " + discussionId.getKey(),
                            task.getException());
                    listener.onError(task.getException());
                }
            }
        });
    }

    @Override
    public void getDiscussionsList(final DocumentId documentId, final ResultListener<List<DiscussionId>> listener) {
        Log.i(TAG, "Attempting to get Discussions list for Document" + documentId.getKey());
        final CollectionReference collRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(documentId.getGroupId().getKey())
                .collection(FirebaseCollections.DOCS.str())
                .document(documentId.getKey())
                .collection(FirebaseCollections.DISCUSSIONS.str());

        collRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DiscussionId> result = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.i(TAG, "Added snapshot for Disscussion: " + document.getId()
                                + " to the list of ids");
                        result.add(document.toObject(FirebaseDiscussion.class).getId());
                    }
                    listener.onResult(result);
                } else {
                    Log.e(TAG, "Error getting Discussion for : " + documentId.getKey(),
                            task.getException());
                    listener.onError(task.getException());
                }
            }
        });
    }

    @Override
    public Discussion addDiscussion(final NewDiscussionRequest request) throws FileNotFoundException {
        Log.i(TAG, "Attempting to add Discussion to the Document " + request.getDocumentId().getKey());
        final DocumentReference docRef = db
                .collection(FirebaseCollections.GROUPS.str())
                .document(request.getDocumentId().getGroupId().getKey())
                .collection(FirebaseCollections.DOCS.str())
                .document(request.getDocumentId().getKey())
                .collection(FirebaseCollections.COMMENTS.str())
                .document();
        final FirebaseDiscussion discussion = new FirebaseDiscussion();
        discussion.id = new DiscussionId(request.getDocumentId(), docRef.getId(), request.getDiscussion().getLocation());
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
        return null;
        //return getDiscussion(request.getDiscussionId());
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
        return null;
        //return getAttachment(firebaseAttachment.getId());
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
}
