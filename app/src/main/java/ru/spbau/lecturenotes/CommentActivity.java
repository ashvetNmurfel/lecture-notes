package ru.spbau.lecturenotes;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnViewDragListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.logging.Logger;

import ru.spbau.lecturenotes.data.*;
import ru.spbau.lecturenotes.services.comments.CommentBuilder;

public class CommentActivity extends AppCompatActivity {
    private ArrayList<ru.spbau.lecturenotes.data.PdfComment> commentList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }

    public void onClickToggle(View view) {
        View dragRect = findViewById(R.id.dragRect);
        dragRect.setVisibility(dragRect.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    public void onClickButtonSendComment(View view) {
        EditText editText = (EditText) findViewById(R.id.editTextComment);
        CommentBuilder commentBuilder = new CommentBuilder("anon", editText.getText().toString());

        PhotoView pdfPicture = (PhotoView) findViewById(R.id.pdfPicture);
        DragRectView dragRect = (DragRectView) findViewById(R.id.dragRect);
        ImageView imageView = (ImageView) findViewById(R.id.justImage);

        Log.i("image", imageView.getWidth() + " " + imageView.getHeight());
        Log.i("image", imageView.getDrawable().getIntrinsicWidth() + " " + imageView.getDrawable().getIntrinsicHeight() + " ");

        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        Log.i("image", scaleX + " " + scaleY);

        Log.i("image",
                imageView.getDrawable().getIntrinsicWidth() * scaleX + " " +
                imageView.getDrawable().getIntrinsicHeight() * scaleY);

        commentList.add(commentBuilder.toPdfComment());
    }
}
