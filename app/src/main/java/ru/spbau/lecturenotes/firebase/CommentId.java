package ru.spbau.lecturenotes.firebase;

class CommentId {
    protected DiscussionId discussion;
    protected String key;

    public DiscussionId getDiscussion() {
        return discussion;
    }

    public String getKey() {
        return key;
    }
}
