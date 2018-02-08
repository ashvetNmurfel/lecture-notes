package ru.spbau.lecturenotes.firebase;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.EventListener;

public interface DatabaseInterface {

    Document getDocument(DocumentId document);
    Discussion getDiscussion(DiscussionId discussion);
    DiscussionId createDiscussion(NewDiscussionRequest request);
    Discussion addComment(AddCommentRequest request /* contains DiscussionId */);

    ListenerRegistration setDocumentListListener(EventListener listener);
    ListenerRegistration setDocumentListener(DocumentId document, EventListener listener);
    ListenerRegistration setDiscussionListener(DiscussionId discussion, EventListener listener);
}

