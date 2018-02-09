package ru.spbau.lecturenotes.firebase;

public class CommentId {
    protected DiscussionId discussion;
    protected String key;

    public DiscussionId getDiscussion() {
        return discussion;
    }

    public String getKey() {
        return key;
    }
}
