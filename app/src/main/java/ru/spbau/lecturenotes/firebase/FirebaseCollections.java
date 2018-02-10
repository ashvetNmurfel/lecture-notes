package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

public enum FirebaseCollections {
    GROUPS("groups"),
    DOCS("documents"),
    DISCUSSIONS("discussions"),
    COMMENTS("comments"),
    ATTACHMENTS("attachments");

    private String str;

    FirebaseCollections(@NotNull final String str) {
        this.str = str;
    }

    public String str() {
        return str;
    }
}
