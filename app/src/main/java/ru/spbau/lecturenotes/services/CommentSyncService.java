package ru.spbau.lecturenotes.services;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.spbau.lecturenotes.storage.DatabaseInterface;
import ru.spbau.lecturenotes.storage.ListenerController;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.identifiers.CommentId;
import ru.spbau.lecturenotes.storage.identifiers.DiscussionId;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;

public class CommentSyncService {
    protected final DatabaseInterface db;

    public CommentSyncService(DatabaseInterface db) {
        this.db = db;
    }

    public ListenerController listenToDiscusionList(
            final @NotNull DocumentId document,
            final @NotNull ResultListener<List<DiscussionId>> listener) {
        return db.setDiscussionsListListener(document, new DatabaseInterface.DiscussionListListener() {
            @Override
            public void onDiscussionListUpdated(final @NotNull DocumentId documentId) {
                db.getDiscussionsList(documentId, listener);
            }
        });
    }

    public ListenerController listenToCommentList(
            final @NotNull DiscussionId discussion,
            final @NotNull ResultListener<List<CommentId>> listener) {
        return db.setCommentsListListener(discussion, new DatabaseInterface.CommentListListener() {
            @Override
            public void onCommentsListUpdated(final @NotNull DiscussionId discussion) {
                db.getCommentsList(discussion, listener);
            }
        });
    }
}
