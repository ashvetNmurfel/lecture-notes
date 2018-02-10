package ru.spbau.lecturenotes.firebase;

import com.google.firebase.firestore.ListenerRegistration;

import java.io.FileNotFoundException;
import java.util.EventListener;
import java.util.List;

import ru.spbau.lecturenotes.firebase.requestStructures.AddCommentRequest;
import ru.spbau.lecturenotes.firebase.requestStructures.NewAttachmentRequest;
import ru.spbau.lecturenotes.firebase.requestStructures.NewDiscussionRequest;
import ru.spbau.lecturenotes.firebase.uiData.Attachment;
import ru.spbau.lecturenotes.firebase.uiData.AttachmentId;
import ru.spbau.lecturenotes.firebase.uiData.CommentId;
import ru.spbau.lecturenotes.firebase.uiData.Discussion;
import ru.spbau.lecturenotes.firebase.uiData.DiscussionId;
import ru.spbau.lecturenotes.firebase.uiData.Document;
import ru.spbau.lecturenotes.firebase.uiData.DocumentId;
import ru.spbau.lecturenotes.firebase.uiData.GroupId;

public interface DatabaseInterface {

    Document getDocument(DocumentId document);
    Discussion getDiscussion(DiscussionId discussion);
    Attachment getAttachment(AttachmentId attachment);

    List<DocumentId> getDocumentsList(GroupId group);
    List<GroupId> getGroupsList();
    List<CommentId> getCommentsList(DiscussionId discussionId);
    List<DiscussionId> getDiscussionsList(DocumentId documentId);

    Discussion addDiscussion(NewDiscussionRequest request) throws FileNotFoundException;
    Discussion addComment(AddCommentRequest request /* contains DiscussionId */) throws FileNotFoundException;
    Attachment addAttachment(NewAttachmentRequest request) throws FileNotFoundException;

    ListenerRegistration setDocumentListListener(EventListener listener, GroupId group);
    ListenerRegistration setGroupsListListener(EventListener listener);
    ListenerRegistration setDocumentListener(DocumentId document, EventListener listener);
    ListenerRegistration setDiscussionListener(DiscussionId discussion, EventListener listener);
}

