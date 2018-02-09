package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FirebaseObjectsConvertor {
    public static Document toDocument(@NotNull final FirebaseProxy.FirebaseDocument fdoc,
                                      @NotNull final List<DiscussionId> discussions) {
        return new Document(fdoc.getId(),
                discussions,
                fdoc.getStorageReference(),
                fdoc.getUpdateTimestamp());
    }

    public static Discussion toDiscussion(@NotNull final FirebaseProxy.FirebaseDiscussion fdiscussion,
                                          @NotNull final List<CommentId> commentsList) {
        return new Discussion(
                fdiscussion.getId(),
                DiscussionStatus.valueOf(fdiscussion.getStatus()),
                fdiscussion.getTimestamp(),
                commentsList
        );
    }

}
