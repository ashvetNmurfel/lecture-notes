package ru.spbau.lecturenotes.data.attachments;

import android.graphics.drawable.Drawable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PictureAttachment extends CommentAttachment {
    private String path = null;
    private Drawable drawable = null;

    public PictureAttachment(@Nullable final String picturePath,
                             @Nullable final Drawable pictureDrawable,
                             @NotNull final String author) {
        super(author);
        assert pictureDrawable != null || picturePath != null;
        //todo: download image and upload it to firebase.
        // Then delegate work with a file to the firebase.
        drawable = pictureDrawable;
        path = picturePath;
    }

    public PictureAttachment(@NotNull final String picturePath,
                             @NotNull final String author) {
        this(picturePath, null, author);
    }

    public PictureAttachment(@NotNull final Drawable pictureDrawable,
                             @NotNull final String author) {
        this(null, pictureDrawable, author);
    }

    public String getPicturePath() {
        assert path != null;
        return path;
    }

    public Drawable getDrawable() {
        assert drawable != null;
        return drawable;
    }

    public boolean hasDrawable() {
        return drawable != null;
    }

    public boolean hasPath() {
        return path != null;
    }
}
