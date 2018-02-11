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
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import ru.spbau.lecturenotes.storage.DatabaseInterface;
import ru.spbau.lecturenotes.storage.ListenerController;
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
        final DocumentReference docRef = Schema.document(db, document);
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
        final DocumentReference docRef = Schema.discussion(db, discussion);
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
        final DocumentReference docRef = Schema.attachment(db, attachment);
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

    private static class LoadListListener<C, K> implements OnCompleteListener<QuerySnapshot> {
        private Class<C> clazz;
        private ResultListener<List<K>> listener;
        private Function<C, K> mapper;

        public LoadListListener(Class<C> clazz, ResultListener<List<K>> listener, Function<C, K> mapper) {
            this.clazz = clazz;
            this.listener = listener;
            this.mapper = mapper;
        }

        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                final List<K> result = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    result.add(mapper.apply(document.toObject(clazz)));
                }
                listener.onResult(result);
            } else {
                listener.onError(task.getException());
            }

        }
    }

    @Override
    public void getDocumentsList(final GroupId group, final ResultListener<List<DocumentId>> listener) {
        Log.i(TAG, "Attempting to get Document list for Group" + group.getKey());
        final CollectionReference collRef = Schema.documents(db, group);
        collRef.orderBy("updateTimestamp").get().addOnCompleteListener(
            new LoadListListener<>(FirebaseDocument.class, listener, new Function<FirebaseDocument, DocumentId>() {
                @Override
                public DocumentId apply(FirebaseDocument firebaseDocument) {
                    return firebaseDocument.getId();
                }
            }));
    }

    @Override
    public void getGroupsList(final @NotNull ResultListener<List<GroupId>> listener) {
        Log.i(TAG, "Attempting to get Group list");
        final CollectionReference collRef = Schema.groups(db);
        collRef
                .whereEqualTo("permissions." + FirebaseAuth.getInstance().getUid(), true)
                .get().addOnCompleteListener(
            new LoadListListener<>(FirebaseGroup.class, listener, new Function<FirebaseGroup, GroupId>() {
                @Override
                public GroupId apply(FirebaseGroup firebaseGroup) {
                    return firebaseGroup.getGroupId();
                }
            }));
    }

    @Override
    public void getCommentsList(final DiscussionId discussionId, final @NotNull ResultListener<List<CommentId>> listener) {
        Log.i(TAG, "Attempting to get Comments list for Discussion " + discussionId.getKey());
        final CollectionReference collRef = Schema.comments(db, discussionId);
        collRef.orderBy("creationTimestamp").get().addOnCompleteListener(
            new LoadListListener<>(FirebaseComment.class, listener, new Function<FirebaseComment, CommentId>() {
                @Override
                public CommentId apply(FirebaseComment firebaseComment) {
                    return firebaseComment.getId();
                }
            }));
    }

    @Override
    public void getDiscussionsList(final DocumentId documentId, final ResultListener<List<DiscussionId>> listener) {
        Log.i(TAG, "Attempting to get Discussions list for Document" + documentId.getKey());
        final CollectionReference collRef = Schema.discussions(db, documentId);

        collRef.get().addOnCompleteListener(
            new LoadListListener<>(FirebaseDiscussion.class, listener, new Function<FirebaseDiscussion, DiscussionId>() {
                @Override
                public DiscussionId apply(FirebaseDiscussion firebaseDiscussion) {
                    return firebaseDiscussion.getId();
                }
            }));
    }

    @Override
    public void addDiscussion(final NewDiscussionRequest request, final ResultListener<Discussion> listener) {
        Log.i(TAG, "Attempting to add Discussion to the Document " + request.getDocumentId().getKey());
        final DocumentReference docRef = Schema.discussions(db, request.getDocumentId()).document();
        final FirebaseDiscussion discussion = new FirebaseDiscussion();
        discussion.id = new DiscussionId(request.getDocumentId(), docRef.getId(), request.getDiscussion().getLocation());
        discussion.status = DiscussionStatus.UNKNOWN.toString();
        discussion.timestamp = null;
        docRef.set(discussion).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "Discussion " + docRef.getId() +
                        " was successfully written to Document " + request.getDocumentId().getKey());
                addComment(new AddCommentRequest(discussion.id, request.getDiscussion().getComment()), new ResultListener<Discussion>() {
                    @Override
                    public void onResult(Discussion result) {
                        listener.onResult(result);
                    }

                    @Override
                    public void onError(Throwable error) {
                        docRef.delete();
                        listener.onError(error);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to add a new discussion to Document " + request.getDocumentId().getKey(), e);
                listener.onError(e);
            }
        });
    }

    @Override
    public void addComment(final AddCommentRequest request, ResultListener<Discussion> listener) {
        Log.i(TAG, "Attempting to add Comment to the Discussion" + request.getDiscussionId().getKey());
        DocumentReference docRef = Schema.comments(db, request.getDiscussionId())
                .document();


        final FirebaseComment comment = new FirebaseComment();
        comment.id = new CommentId(request.getDiscussionId(), docRef.getId());
        comment.author = request.getComment().getAuthor();
        comment.creationTimestamp = null;
        comment.editTimestamp = null;
        comment.content.text = request.getComment().getText();
        new AttachmentUploaderListener(comment, docRef, request.getComment().getAttachments(), listener).uploadNextAttachment();
    }

    private class AttachmentUploaderListener implements ResultListener<Attachment> {
        protected FirebaseComment comment;
        protected DocumentReference docRef;
        protected List<AttachmentSketch> attachments;
        protected ResultListener<Discussion> listener;
        protected List<FirebaseAttachment> uploadedAttachments = new ArrayList<>();

        public AttachmentUploaderListener(FirebaseComment comment, DocumentReference docRef, List<AttachmentSketch> attachments, ResultListener<Discussion> listener) {
            this.comment = comment;
            this.docRef = docRef;
            this.attachments = attachments;
            this.listener = listener;
        }

        public void uploadNextAttachment() {
            if (attachments.size() == uploadedAttachments.size()) {
                comment.content.attachments = uploadedAttachments.toArray(new FirebaseAttachment[attachments.size()]);
                docRef.set(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "New Comment was added to Discussion: ");
                        getDiscussion(comment.getId().getDiscussion(), listener);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "An error occurred while trying to add a comment to Discussion ");
                        listener.onError(e);
                    }
                });
                return;
            }
            AttachmentSketch attachmentSketch = attachments.get(uploadedAttachments.size());
            NewAttachmentRequest newAttachmentRequest = new NewAttachmentRequest(comment.getId(), attachmentSketch);
            addAttachment(newAttachmentRequest, this);
        }

        @Override
        public void onResult(Attachment result) {
            uploadedAttachments.add(new FirebaseAttachment(result));
            uploadNextAttachment();
        }

        @Override
        public void onError(Throwable error) {
            listener.onError(error);
        }
    }

    @Override
    public void addAttachment(final NewAttachmentRequest request, final @NotNull ResultListener<Attachment> listener) {
        final String attachmentPath = "attachments/" + request.getCommentId().getGroupId().getKey() +
                FirebaseAuth.getInstance().getUid() + "/" + UUID.randomUUID();
        StorageReference storageReference = storage.getReference();
        final StorageReference attachmentReference = storageReference.child(attachmentPath);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(request.getAttachment().getPath()));
        } catch (FileNotFoundException e) {
            listener.onError(e);
            return;
        }
        attachmentReference.putStream(inputStream)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Could not upload attachment " + request.getAttachment().getPath()
                                + " to a storage location: " + attachmentPath, e);
                        listener.onError(e);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG, "Successfully uploaded attachment to the storage");
                DocumentReference docRef = Schema.attachments(db, request.getCommentId().getDiscussion())
                        .document();
                final FirebaseAttachment firebaseAttachment = new FirebaseAttachment();
                firebaseAttachment.creationTimestamp = null;
                firebaseAttachment.type = null;
                firebaseAttachment.id = new AttachmentId(request.getCommentId(), docRef.getId());
                firebaseAttachment.storageReference = attachmentPath;
                docRef.set(firebaseAttachment).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to add Attachment for the path: " + request.getAttachment().getPath(), e);
                        attachmentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "Attachment was successfully deleted from the storage");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Failed to delete Attachemnt from the storage", e);
                            }
                        });
                        // todo: just log it ^
                        listener.onError(e);
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Loaded Attachment to the Discussion: " + request.getCommentId().getDiscussion().getKey());
                        getAttachment(firebaseAttachment.getId(), listener);
                    }
                });
            }
        });
    }

    @Override
    public ListenerController setDocumentListListener(final @NotNull GroupId group,
                                                      final @NotNull DocumentListChangeListener listener) {
        CollectionReference collection = Schema.documents(db, group);
        ListenerRegistration registration = collection.addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                listener.onDocumentListUpdated(group);
            }
        });
        return new FirebaseListenerController(registration);
    }

    @Override
    public ListenerController setGroupsListListener(final @NotNull GroupListChangeListener listener) {
        CollectionReference collection = Schema.groups(db);
        ListenerRegistration registration = collection
                .whereEqualTo("permissions." + FirebaseAuth.getInstance().getUid(), true)
                .addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        listener.onGroupListUpdated();
                    }
        });
        return new FirebaseListenerController(registration);
    }

    @Override
    public ListenerController setDiscussionsListListener(final @NotNull DocumentId document,
                                                         final @NotNull DiscussionListListener listener) {
        CollectionReference collection = Schema.discussions(db, document);
        ListenerRegistration registration = collection
                .addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        listener.onDiscussionListUpdated(document);
                    }
                });
        return new FirebaseListenerController(registration);
    }

    @Override
    public ListenerController setCommentsListListener(final @NotNull DiscussionId discussion,
                                                      final @NotNull CommentListListener listener) {
        CollectionReference collection = Schema.comments(db, discussion);
        ListenerRegistration registration = collection
                .addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        listener.onCommentsListUpdated(discussion);
                    }
                });
        return new FirebaseListenerController(registration);
    }
}
