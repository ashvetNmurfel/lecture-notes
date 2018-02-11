package ru.spbau.lecturenotes.storage;

import java.util.EventListener;
import java.util.List;

import ru.spbau.lecturenotes.storage.requests.AddCommentRequest;
import ru.spbau.lecturenotes.storage.requests.NewAttachmentRequest;
import ru.spbau.lecturenotes.storage.requests.NewDiscussionRequest;

public interface DatabaseInterface {

    void getDocument(DocumentId document, ResultListener<Document> listener);

    void getDiscussion(DiscussionId discussion, ResultListener<Discussion> listener);

    void getAttachment(AttachmentId attachment, ResultListener<Attachment> listener);

    void getDocumentsList(GroupId group, ResultListener<List<DocumentId>> listener);

    void getGroupsList(ResultListener<List<GroupId>> listener);

    void getCommentsList(DiscussionId discussionId, ResultListener<List<CommentId>> listResultListener);

    void getDiscussionsList(DocumentId documentId, ResultListener<List<DiscussionId>> listener);

    void addDiscussion(NewDiscussionRequest request, ResultListener<Discussion> listener);

    void addComment(AddCommentRequest request /* contains DiscussionId */, ResultListener<Discussion> listener);

    void addAttachment(NewAttachmentRequest request, ResultListener<Attachment> listener);

    ListenerController setDocumentListListener(EventListener listener, GroupId group);

    ListenerController setGroupsListListener(EventListener listener);

    ListenerController setDocumentListener(DocumentId document, EventListener listener);

    ListenerController setDiscussionListener(DiscussionId discussion, EventListener listener);
}

