package ru.spbau.lecturenotes.services;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.spbau.lecturenotes.storage.Comment;
import ru.spbau.lecturenotes.storage.DatabaseInterface;
import ru.spbau.lecturenotes.storage.Discussion;
import ru.spbau.lecturenotes.storage.ListenerController;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.identifiers.DiscussionId;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;

public class CommentSyncService {
    private static final String TAG = "CommentSyncService";
    protected final DatabaseInterface db;

    public CommentSyncService(DatabaseInterface db) {
        this.db = db;
    }

    public ListenerController listenToDiscussionList(
            final @NotNull DocumentId document,
            final @NotNull ResultListener<List<DiscussionId>> listener) {
        return db.setDiscussionsListListener(document, new DatabaseInterface.DiscussionListListener() {
            @Override
            public void onDiscussionListUpdated(final @NotNull DocumentId documentId) {
                db.getDiscussionIdsList(documentId, listener);
            }
        });
    }

    public void getDiscussions(
            @NotNull List<DiscussionId> discussionIds,
            @NotNull ResultListener<List<Discussion>> listener) {
        db.getDiscussionsList(discussionIds, listener);
    }

    public ListenerController listenToCommentList(
            final @NotNull DiscussionId discussion,
            final @NotNull ResultListener<List<Comment>> listener) {
        return db.setCommentsListListener(discussion, new DatabaseInterface.CommentListListener() {
            @Override
            public void onCommentsListUpdated(final @NotNull DiscussionId discussion) {
                db.getCommentsList(discussion, new ResultListener<List<Comment>>() {
                    @Override
                    public void onResult(List<Comment> result) {
                        Log.i(TAG, "Comments list successfully downloaded. Handling to listener...");
                        listener.onResult(result);
                    }

                    @Override
                    public void onError(Throwable error) {
                        listener.onError(error);
                    }
                });
            }
        });
    }
}
