package ru.spbau.lecturenotes.storage;

import java.io.File;
import java.util.List;

import ru.spbau.lecturenotes.storage.identifiers.AttachmentId;
import ru.spbau.lecturenotes.storage.identifiers.CommentId;
import ru.spbau.lecturenotes.storage.identifiers.DiscussionId;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;
import ru.spbau.lecturenotes.storage.identifiers.GroupId;
import ru.spbau.lecturenotes.storage.requests.AddCommentRequest;
import ru.spbau.lecturenotes.storage.requests.AddDocumentRequest;
import ru.spbau.lecturenotes.storage.requests.NewAttachmentRequest;
import ru.spbau.lecturenotes.storage.requests.NewDiscussionRequest;

public interface DatabaseInterface {

    void getDocument(DocumentId document, ResultListener<Document> listener);

    void getDiscussion(DiscussionId discussion, ResultListener<Discussion> listener);

    void getAttachment(AttachmentId attachment, ResultListener<Attachment> listener);

    void getComment(CommentId comment, ResultListener<Comment> listener);

    void getCommentsList(DiscussionId discussionId, ResultListener<List<Comment>> listener);

    void getCommentsList(List<CommentId> ids, ResultListener<List<Comment>> listener);

    void getDiscussionsList(List<DiscussionId> ids, ResultListener<List<Discussion>> listener);

    void getAttachmentsList(List<AttachmentId> ids, ResultListener<List<Attachment>> listener);

    void getDocumentIdsList(GroupId group, ResultListener<List<DocumentId>> listener);

    void getGroupIdsList(ResultListener<List<GroupId>> listener);

    void getCommentIdsList(DiscussionId discussionId, ResultListener<List<CommentId>> listResultListener);

    void getDiscussionIdsList(DocumentId documentId, ResultListener<List<DiscussionId>> listener);

    void addDiscussion(NewDiscussionRequest request, ResultListener<Discussion> listener);

    void addDocument(AddDocumentRequest request, ResultListener<Document> listener);

    void addComment(AddCommentRequest request /* contains DiscussionId */, ResultListener<Discussion> listener);

    void addAttachment(NewAttachmentRequest request, ResultListener<Attachment> listener);

    void getAttachmentContent(Attachment attachment, File file, ResultListener<LocalFile<AttachmentId>> listener);

    void getDocumentFile(Document document, File file, ResultListener<LocalFile<DocumentId>> listener);

    ListenerController setDocumentListListener(GroupId group, DocumentListChangeListener listener);

    ListenerController setGroupsListListener(GroupListChangeListener listener);

    ListenerController setDiscussionsListListener(DocumentId document, DiscussionListListener listener);

    ListenerController setCommentsListListener(DiscussionId discussion, CommentListListener listener);

    UserInfo getUserInfo();

    interface DocumentListChangeListener {
        void onDocumentListUpdated(GroupId id);
    }

    interface GroupListChangeListener {
        void onGroupListUpdated();
    }

    interface DiscussionListListener {
        void onDiscussionListUpdated(DocumentId document);
    }

    interface CommentListListener {
        void onCommentsListUpdated(DiscussionId discussion);
    }
}

