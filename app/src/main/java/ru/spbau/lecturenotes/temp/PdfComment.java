package ru.spbau.lecturenotes.temp;

import android.graphics.RectF;

import java.util.Random;

public class PdfComment {
    public String text;
    public RectF rectF;
    public boolean hasAttachment;

    public static PdfComment get() {
        PdfComment comment = new PdfComment();
        Random rand = new Random();
        float l = rand.nextFloat();
        float t = rand.nextFloat();
        float r = rand.nextFloat();
        float b = rand.nextFloat();
        while (r < l) {
            r = rand.nextFloat();
        }
        while (b < t) {
            b = rand.nextFloat();
        }

        comment.rectF = new RectF(l, t, r, b);
        comment.text = comment.rectF.toShortString();
        return comment;
    }
}
