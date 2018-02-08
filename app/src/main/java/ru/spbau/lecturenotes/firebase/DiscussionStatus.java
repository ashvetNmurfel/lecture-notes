package ru.spbau.lecturenotes.firebase;

enum DiscussionStatus {
    UNKNOWN,

    FIX_REQUESTED,
    NOT_A_BUG,
    FIX_IN_PROGRESS,

    NOT_ANSWERED,
    ANSWERED,
    DISCUSSION_IN_PROGRESS,
    OUTDATED,
    DUPLICATE
}
