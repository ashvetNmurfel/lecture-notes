package ru.spbau.lecturenotes.storage.requests;

import org.jetbrains.annotations.NotNull;

import ru.spbau.lecturenotes.storage.DiscussionLocation;

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
