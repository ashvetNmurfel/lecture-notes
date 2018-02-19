package ru.spbau.lecturenotes.storage;

import org.jetbrains.annotations.Nullable;

public class DiscussionLocation {
    protected int page;
    protected Rectangle rectangle;

    public DiscussionLocation() {


    }
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
