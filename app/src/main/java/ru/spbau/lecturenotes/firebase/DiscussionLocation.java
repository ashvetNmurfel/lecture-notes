package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscussionLocation {
    protected int page;
    protected Rectangle rectangle;

    public DiscussionLocation(int page,
                              @Nullable Rectangle rectangle) {
        this.page = page;
        this.rectangle = rectangle;
    }

    public int getPage() {
        return page;
    }

    @Nullable
    public Rectangle getRectangle() {
        return rectangle;
    }
}
