package ru.spbau.lecturenotes.firebase;

import com.google.firebase.firestore.ListenerRegistration;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

class FirebaseProxy implements DatabaseInterface {
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
}
