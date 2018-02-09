package ru.spbau.lecturenotes.firebase;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.EventListener;
import java.util.List;

public interface DatabaseInterface {

    Document getDocument(DocumentId document);
    Discussion getDiscussion(DiscussionId discussion);
    Attachment getAttachment(AttachmentId attachment);

    List<DocumentId> getDocumentsList(GroupId group);
    List<GroupId> getGroupsList();
    List<CommentId> getCommentsList(DiscussionId discussionId);
    List<DiscussionId> getDiscussionsList(DocumentId documentId);

    DiscussionId addDiscussion(NewDiscussionRequest request);
    Discussion addComment(AddCommentRequest request /* contains DiscussionId */);

    ListenerRegistration setDocumentListListener(EventListener listener, GroupId group);
    ListenerRegistration setGroupsListListener(EventListener listener);
    ListenerRegistration setDocumentListener(DocumentId document, EventListener listener);
    ListenerRegistration setDiscussionListener(DiscussionId discussion, EventListener listener);
}

