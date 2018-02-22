package ru.spbau.lecturenotes.storage.firebase;

import android.support.annotation.NonNull;
import android.text.TextUtils;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

import ru.spbau.lecturenotes.storage.Attachment;
import ru.spbau.lecturenotes.storage.Comment;
import ru.spbau.lecturenotes.storage.DatabaseInterface;
import ru.spbau.lecturenotes.storage.Discussion;
import ru.spbau.lecturenotes.storage.DiscussionStatus;
import ru.spbau.lecturenotes.storage.Document;
import ru.spbau.lecturenotes.storage.ListenerController;
import ru.spbau.lecturenotes.storage.LocalFile;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.User;
import ru.spbau.lecturenotes.storage.UserInfo;
import ru.spbau.lecturenotes.storage.identifiers.AttachmentId;
import ru.spbau.lecturenotes.storage.identifiers.CommentId;
import ru.spbau.lecturenotes.storage.identifiers.DiscussionId;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;
import ru.spbau.lecturenotes.storage.identifiers.GroupId;
import ru.spbau.lecturenotes.storage.requests.AddCommentRequest;
import ru.spbau.lecturenotes.storage.requests.AddDocumentRequest;
import ru.spbau.lecturenotes.storage.requests.AttachmentSketch;
import ru.spbau.lecturenotes.storage.requests.NewAttachmentRequest;
import ru.spbau.lecturenotes.storage.requests.NewDiscussionRequest;

public class FirebaseProxy implements DatabaseInterface {
    private static final String TAG = "FirebaseProxy";
    protected static FirebaseProxy INSTANCE = new FirebaseProxy();
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected FirebaseStorage storage = FirebaseStorage.getInstance();
    protected FirebaseAuth auth = FirebaseAuth.getInstance();

    public static FirebaseProxy getInstance() {
        return INSTANCE;
    }

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
                        Log.i(TAG, "Got documentId snapshot for Document: " + documentSnapshot.getId());
                        FirebaseDocument firebaseDocument = documentSnapshot.toObject(FirebaseDocument.class);
                        getDiscussionIdsList(document, new LoadDiscussionListResultListener(
                                firebaseDocument, listener));
                    } else {
                        Log.w(TAG, "Error: could not find snapshot for Document: " + document.getKey());
                        listener.onError(new IllegalArgumentException("Document " + document.getKey() + "wasn't found"));
                    }
                } else {
                    Log.e(TAG, "Attempt to get documentId snapshot for Document " +
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
                        Log.i(TAG, "Got documentId snapshot for Discussion: " + documentSnapshot.getId());
                        getCommentIdsList(discussion,
                                new LoadCommentsListResultListener(documentSnapshot.toObject(FirebaseDiscussion.class), listener));
                    } else {
                        Log.w(TAG, "Error: could not find snapshot for Discussion: " + discussion.getKey());
                        listener.onError(new IllegalArgumentException("Discussion " + discussion.getKey() + " wasn't found"));
                    }
                } else {
                    Log.e(TAG, "Attempt to get documentId snapshot for Discussion " +
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
                        Log.i(TAG, "Got documentId snapshot for Attachment: " + documentSnapshot.getId());
                        listener.onResult(FirebaseObjectsConvertor
                                .toAttachment(documentSnapshot.toObject(FirebaseAttachment.class)));
                    } else {
                        Log.w(TAG, "Error: could not find snapshot for Attachment: " + attachment.getKey());
                        listener.onError(new IllegalArgumentException("...."));
                    }
                } else {
                    Log.e(TAG, "Attempt to get documentId snapshot for Attachment" +
                            attachment.getKey() +
                            " failed with", task.getException());
                    listener.onError(task.getException());
                }
            }
        });
    }

    @Override
    public void getComment(final CommentId comment, final ResultListener<Comment> listener) {
        Log.i(TAG, "Attempting to get Comment " + comment.getKey());
        final DocumentReference docRef = Schema.comment(db, comment);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        Log.i(TAG, "Got documentId snapshot for Attachment: " + documentSnapshot.getId());
                        listener.onResult(FirebaseObjectsConvertor
                                .toComment(documentSnapshot.toObject(FirebaseComment.class)));
                    } else {
                        Log.w(TAG, "Error: could not find snapshot for Comment: " + comment.getKey());
                        listener.onError(new IllegalArgumentException("...."));
                    }
                } else {
                    Log.e(TAG, "Attempt to get documentId snapshot for Comment" +
                            comment.getKey() +
                            " failed with", task.getException());
                    listener.onError(task.getException());
                }
            }
        });
    }

    private class LoadListListener<C, K> implements ResultListener<K> {
        protected List<C> input;
        protected List<K> result = new ArrayList<>();
        protected ResultListener<List<K>> listener;
        protected BiConsumer<C, ResultListener<K>> downloader;
        public LoadListListener(List<C> input,
                                ResultListener<List<K>> listener,
                                BiConsumer<C, ResultListener<K>> downloader) {
            this.input = input;
            this.listener = listener;
            this.downloader = downloader;
        }
        public void downloadNextItem() {
            if (input.size() == result.size()) {
                listener.onResult(result);
                return;
            }
            C nextItem = input.get(result.size());
            downloader.accept(nextItem, this);
        }
        @Override
        public void onResult(K result) {
            this.result.add(result);
            downloadNextItem();
        }
        @Override
        public void onError(Throwable error) {
            listener.onError(error);
        }
    }

    private static class LoadCollectionListener<C, K> implements OnCompleteListener<QuerySnapshot> {
        private Class<C> clazz;
        private ResultListener<List<K>> listener;
        private Function<C, K> mapper;

        public LoadCollectionListener(Class<C> clazz, ResultListener<List<K>> listener, Function<C, K> mapper) {
            this.clazz = clazz;
            this.listener = listener;
            this.mapper = mapper;
        }

        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                final List<K> result = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    Log.i("Debug", document == null ? "fuck" : "that's fine");
                    result.add(mapper.apply(document.toObject(clazz)));
                }
                listener.onResult(result);
            } else {
                listener.onError(task.getException());
            }

        }
    }

    @Override
    public void getCommentsList(final DiscussionId discussionId, final ResultListener<List<Comment>> listener) {
        Log.i(TAG, "Attempting to get Comments list for Discussion" + discussionId.getKey());
        final CollectionReference collRef = Schema.comments(db, discussionId);

        collRef.get().addOnCompleteListener(
                new LoadCollectionListener<>(FirebaseComment.class, listener,new Function<FirebaseComment, Comment>() {
                    @Override
                    public Comment apply(FirebaseComment firebaseComment) {
                        Log.i(TAG, "Got Comment for Discussion " + discussionId.getKey());
                        return FirebaseObjectsConvertor.toComment(firebaseComment);
                    }
                }));
    }

    @Override
    public void getCommentsList(final List<CommentId> ids, final ResultListener<List<Comment>> listener) {
        Log.i(TAG, "Attempting to get Comments list");
        if (ids.isEmpty()) {
            Log.i(TAG, "CommentIds is empty. Nothing to get");
            return;
        }
        getComment(ids.get(0), new LoadListListener<>(ids, listener, new BiConsumer<CommentId, ResultListener<Comment>>() {
            @Override
            public void accept(CommentId commentId, ResultListener<Comment> listener) {
                Log.i(TAG, "Got Comment " + commentId.getKey());
                getComment(commentId, listener);
            }
        }));
    }

    @Override
    public void getDiscussionsList(final List<DiscussionId> ids, final ResultListener<List<Discussion>> listener) {
        Log.i(TAG, "Attempting to get Discussions list");
        if (ids.isEmpty()) {
            Log.i(TAG, "DiscussionIds is empty. Nothing to get");
            return;
        }
        getDiscussion(ids.get(0), new LoadListListener<>(ids, listener, new BiConsumer<DiscussionId, ResultListener<Discussion>>() {
            @Override
            public void accept(DiscussionId discussionId, ResultListener<Discussion> listener) {
                Log.i(TAG, "Got Discussion " + discussionId.getKey());
                getDiscussion(discussionId, listener);
            }
        }));
    }

    @Override
    public void getAttachmentsList(final List<AttachmentId> ids, final ResultListener<List<Attachment>> listener) {
        Log.i(TAG, "Attempting to get Attachments list");
        new LoadListListener<>(ids, listener, new BiConsumer<AttachmentId, ResultListener<Attachment>>() {
            @Override
            public void accept(AttachmentId attachmentId, ResultListener<Attachment> resultListener) {
                getAttachment(attachmentId, resultListener);
            }
        });
    }

    @Override
    public void getDocumentIdsList(final GroupId group, final ResultListener<List<DocumentId>> listener) {
        Log.i(TAG, "Attempting to get Document list for Group" + group.getKey());
        final CollectionReference collRef = Schema.documents(db, group);
        collRef.orderBy("updateTimestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(
            new LoadCollectionListener<>(FirebaseDocument.class, listener, new Function<FirebaseDocument, DocumentId>() {
                @Override
                public DocumentId apply(FirebaseDocument firebaseDocument) {
                    return firebaseDocument.getId();
                }
            }));
    }

    @Override
    public void getGroupIdsList(final @NotNull ResultListener<List<GroupId>> listener) {
        Log.i(TAG, "Attempting to get Group list");
        final CollectionReference collRef = Schema.groups(db);
        collRef
                .whereEqualTo("permissions." + auth.getUid(), true)
                .get().addOnCompleteListener(
            new LoadCollectionListener<>(FirebaseGroup.class, listener, new Function<FirebaseGroup, GroupId>() {
                @Override
                public GroupId apply(FirebaseGroup firebaseGroup) {
                    return firebaseGroup.getId();
                }
            }));
    }

    @Override
    public void getCommentIdsList(final DiscussionId discussionId, final @NotNull ResultListener<List<CommentId>> listener) {
        Log.i(TAG, "Attempting to get Comments list for Discussion " + discussionId.getKey());
        final CollectionReference collRef = Schema.comments(db, discussionId);
        collRef.orderBy("creationTimestamp").get().addOnCompleteListener(
            new LoadCollectionListener<>(FirebaseComment.class, listener, new Function<FirebaseComment, CommentId>() {
                @Override
                public CommentId apply(FirebaseComment firebaseComment) {
                    Log.i(TAG, "Got Comment " + firebaseComment.getId().getKey() + " from " + discussionId.getKey());
                    return firebaseComment.getId();
                }
            }));
    }

    @Override
    public void getDiscussionIdsList(final DocumentId documentId, final ResultListener<List<DiscussionId>> listener) {
        Log.i(TAG, "Attempting to get Discussions list for Document" + documentId.getKey());
        final CollectionReference collRef = Schema.discussions(db, documentId);

        collRef.get().addOnCompleteListener(
            new LoadCollectionListener<>(FirebaseDiscussion.class, listener, new Function<FirebaseDiscussion, DiscussionId>() {
                @Override
                public DiscussionId apply(FirebaseDiscussion firebaseDiscussion) {
                    Log.i(TAG, "Got Discussion from Document " + documentId.getKey());
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
                Log.e(TAG, "Failed to add a new discussionId to Document " + request.getDocumentId().getKey(), e);
                listener.onError(e);
            }
        });
    }

    @Override
    public void addDocument(final AddDocumentRequest request, final ResultListener<Document> listener) {
        final String documentPath = TextUtils.join("/",
                Arrays.asList("documents",
                        request.getGroup().getKey(),
                        FirebaseAuth.getInstance().getUid(),
                        UUID.randomUUID().toString()));
        StorageReference storageReference = storage.getReference();
        final StorageReference documentReference = storageReference.child(documentPath);
        InputStream inputStream = request.getSketch().getPdf();
        documentReference.putStream(inputStream)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Could not upload document to a storage location: " + documentPath, e);
                        listener.onError(e);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG, "Successfully uploaded document to the storage");
                DocumentReference docRef = Schema.documents(db, request.getGroup())
                        .document();
                final FirebaseDocument firebaseDocument = new FirebaseDocument();
                firebaseDocument.updateTimestamp = null;
                firebaseDocument.id = new DocumentId(
                        request.getGroup(),
                        docRef.getId(),
                        request.getSketch().getFilename(),
                        request.getSketch().getDescription());
                firebaseDocument.reference = new FirebaseDocumentDataReference();
                firebaseDocument.reference.storageReference = documentPath;
                firebaseDocument.reference.type = "PDF";
                docRef.set(firebaseDocument).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to add Document for the path: " + documentPath, e);
                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "Document was successfully deleted from the storage");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Failed to delete Document from the storage", e);
                            }
                        });
                        listener.onError(e);
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Loaded Document to the Group: " + request.getGroup().getKey());
                        getDocument(firebaseDocument.getId(), listener);
                    }
                });
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
        comment.author = new User(auth.getUid());
        comment.creationTimestamp = null;
        comment.editTimestamp = null;
        comment.content = new FirebaseCommentContent();
        comment.content.text = request.getComment().getText();
        new AttachmentUploaderListener(comment, docRef, request.getComment().getAttachments(), listener).uploadNextAttachment();
    }

    private class AttachmentUploaderListener implements ResultListener<Attachment> {
        protected FirebaseComment comment;
        protected DocumentReference docRef;
        protected List<AttachmentSketch> attachments;
        protected ResultListener<Discussion> listener;
        protected ArrayList<FirebaseAttachment> uploadedAttachments = new ArrayList<>();

        public AttachmentUploaderListener(FirebaseComment comment,
                                          DocumentReference docRef,
                                          List<AttachmentSketch> attachments,
                                          ResultListener<Discussion> listener) {
            this.comment = comment;
            this.docRef = docRef;
            this.attachments = attachments;
            this.listener = listener;
        }

        public void uploadNextAttachment() {
            if (attachments.size() == uploadedAttachments.size()) {
                comment.content.attachments = uploadedAttachments;
                docRef.set(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "New Comment was added to Discussion: ");
                        getDiscussion(comment.getId().getDiscussionId(), listener);
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
        final String attachmentPath = TextUtils.join("/",
                Arrays.asList("attachments",
                        request.getCommentId().groupId().getKey(),
                        FirebaseAuth.getInstance().getUid(),
                        UUID.randomUUID().toString()));
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
                DocumentReference docRef = Schema.attachments(db, request.getCommentId().getDiscussionId())
                        .document();
                final FirebaseAttachment firebaseAttachment = new FirebaseAttachment();
                firebaseAttachment.creationTimestamp = null;
                firebaseAttachment.type = request.getAttachment().getAttachmentType().toString();
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
                        listener.onError(e);
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Loaded Attachment to the Discussion: " + request.getCommentId().getDiscussionId().getKey());
                        getAttachment(firebaseAttachment.getId(), listener);
                    }
                });
            }
        });
    }

    @Override
    public void getAttachmentContent(final Attachment attachment, final File file,
                                     final ResultListener<LocalFile<AttachmentId>> listener) {
        Log.i(TAG, "Preparing to download Attachment file: " + attachment.getReference());
        StorageReference attachmentReference = storage.getReference(attachment.getReference());
        attachmentReference.getFile(file).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to download attachment from reference " + attachment.getReference(), e);
                  listener.onError(e);
            }
        }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG, "Attachment " + attachment.getReference() + " was downloaded");
                listener.onResult(new LocalFile<>(attachment.getAttachmentId(), file));
            }
        });
    }

    @Override
    public void getDocumentFile(final Document document, final File file,
                                final ResultListener<LocalFile<DocumentId>> listener) {
        Log.i(TAG, "Preparing to download Document file: " + document.getReference());
        StorageReference documentReference = storage.getReference(document.getReference().getStorageReference());
        documentReference.getFile(file).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to download document from reference " + document.getReference(), e);
                listener.onError(e);
            }
        }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG, "Document " + document.getReference() + " was downloaded");
                listener.onResult(new LocalFile<>(document.getId(), file));
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

    @Override
    public UserInfo getUserInfo() {
        if (auth.getCurrentUser() == null) {
            return null;
        }
        return new UserInfo(
                auth.getUid(),
                auth.getCurrentUser().getDisplayName(),
                auth.getCurrentUser().getEmail());
    }
}
