package ru.spbau.lecturenotes.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.lecturenotes.Utilities;

public class CommentsBundle {
    protected ArrayList<PdfComment> comments = new ArrayList<>();
    protected final String fileId;
    protected final int inFileId;

    protected CommentsBundle(@NotNull final String fileId, int inFileId) {
        this.inFileId = inFileId;
        this.fileId = fileId;
    }

    protected CommentsBundle(@NotNull final String fileId, int inFileId,
                             @NotNull final List<PdfComment> comments) {
        this(fileId, inFileId);
        this.comments = Utilities.convertListToArrayList(comments);
    }

    public boolean addComment(@NotNull final PdfComment comment) {
        comments.add(comment);
        return true;
    }

    public String getFileId() {
        return fileId;
    }

    public int getInFileId() {
        return inFileId;
    }
}