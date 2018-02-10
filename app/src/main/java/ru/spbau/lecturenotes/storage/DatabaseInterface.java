package ru.spbau.lecturenotes.storage;

import com.google.firebase.firestore.ListenerRegistration;

import org.androidannotations.rclass.IRClass;

import java.io.FileNotFoundException;
import java.util.EventListener;
import java.util.List;

import ru.spbau.lecturenotes.storage.requests.AddCommentRequest;
import ru.spbau.lecturenotes.storage.requests.NewAttachmentRequest;
import ru.spbau.lecturenotes.storage.requests.NewDiscussionRequest;
import ru.spbau.lecturenotes.storage.Attachment;
import ru.spbau.lecturenotes.storage.AttachmentId;
import ru.spbau.lecturenotes.storage.CommentId;
import ru.spbau.lecturenotes.storage.Discussion;
import ru.spbau.lecturenotes.storage.DiscussionId;
import ru.spbau.lecturenotes.storage.Document;
import ru.spbau.lecturenotes.storage.DocumentId;
import ru.spbau.lecturenotes.storage.GroupId;

public interface DatabaseInterface {

    void getDocument(DocumentId document, ResultListener<Document> listener);

    void getDiscussion(DiscussionId discussion, ResultListener<Discussion> listener);

    void getAttachment(AttachmentId attachment, ResultListener<Attachment> listener);

    void getDocumentsList(GroupId group, ResultListener<List<DocumentId>> listener);

    void getGroupsList(ResultListener<List<GroupId>> listener);

    void getCommentsList(DiscussionId discussionId, ResultListener<List<CommentId>> listResultListener);

    void getDiscussionsList(DocumentId documentId, ResultListener<List<DiscussionId>> listener);

    Discussion addDiscussion(NewDiscussionRequest request) throws FileNotFoundException;

    Discussion addComment(AddCommentRequest request /* contains DiscussionId */) throws FileNotFoundException;

    Attachment addAttachment(NewAttachmentRequest request) throws FileNotFoundException;

    ListenerRegistration setDocumentListListener(EventListener listener, GroupId group);

    ListenerRegistration setGroupsListListener(EventListener listener);

    ListenerRegistration setDocumentListener(DocumentId document, EventListener listener);

    ListenerRegistration setDiscussionListener(DiscussionId discussion, EventListener listener);
}

