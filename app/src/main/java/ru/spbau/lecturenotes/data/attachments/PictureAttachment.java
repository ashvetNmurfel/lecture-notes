package ru.spbau.lecturenotes.data.attachments;

import org.jetbrains.annotations.NotNull;

public class PictureAttachment extends CommentAttachment {
    private final String path;
    public PictureAttachment(@NotNull final String picturePath,
                             @NotNull final String author) {
        super(author);
        path = picturePath;
        //todo: download image and upload it to firebase.
        // Then delegate work with a file to the firebase.
    }

    public String getPicturePath() {
        return path;
    }
}
