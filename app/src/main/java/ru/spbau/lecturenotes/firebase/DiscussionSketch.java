package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

public class DiscussionSketch {
    protected CommentSketch comment;
    protected DiscussionLocation location;

    public DiscussionSketch(@NotNull CommentSketch comment, @NotNull DiscussionLocation location) {
        this.comment = comment;
        this.location = location;
    }

    @NotNull
    public CommentSketch getComment() {
        return comment;
    }

    @NotNull
    public DiscussionLocation getLocation() {
        return location;
    }
}
