package ru.spbau.lecturenotes;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.Arrays;

import ru.spbau.lecturenotes.data.PdfComment;
import ru.spbau.lecturenotes.services.comments.CommentBuilder;

import static java.lang.Math.round;

public class PdfActivity extends AppCompatActivity {
    private ArrayList<ru.spbau.lecturenotes.data.PdfComment> commentList = new ArrayList<>();
    public static final int COMMENT_MODE_ACTIVITY_CODE = 111;
    private Rect currentRect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        ArrayList<PdfPage> arrayList = new ArrayList<>(Arrays.asList(new PdfPage(), new PdfPage(), new PdfPage()));

        ListView lv = (ListView) findViewById(R.id.pdfPictureListView);
        PdfPageAdapter adapter = new PdfPageAdapter(this, arrayList, lv);
        lv.setAdapter(adapter);

        View view = adapter.getView(0, null, null);
        PhotoView pagePicture = (PhotoView) view.findViewById(R.id.pagePicture);

        DragRectView dragRectView = (DragRectView) findViewById(R.id.dragRect);
        dragRectView.setOnUpCallback(new DragRectView.OnUpCallback() {
            @Override
            public void onRectFinished(Rect rect) {
                currentRect = rect;
            }
        });
    }

    private boolean isInCommentMode;

    public void onClickPlusButtonGoToCommentMode(View view) {
        isInCommentMode = true;

        ListView listView = (ListView) findViewById(R.id.pdfPictureListView);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.pdfPage);
        RelativeLayout viewModeSettings = (RelativeLayout) findViewById(R.id.viewModeSettings);
        RelativeLayout commentModeSettings = (RelativeLayout) findViewById(R.id.commentModeSettings);

        listView.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        viewModeSettings.setVisibility(View.GONE);
        commentModeSettings.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (isInCommentMode) {
            isInCommentMode = false;

            ListView listView = (ListView) findViewById(R.id.pdfPictureListView);
            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.pdfPage);
            RelativeLayout viewModeSettings = (RelativeLayout) findViewById(R.id.viewModeSettings);
            RelativeLayout commentModeSettings = (RelativeLayout) findViewById(R.id.commentModeSettings);

            listView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            viewModeSettings.setVisibility(View.VISIBLE);
            commentModeSettings.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    public void onClickButtonSendComment(View view) {
        EditText editText = (EditText) findViewById(R.id.editTextComment);
        CommentBuilder commentBuilder = new CommentBuilder("anon", editText.getText().toString());

        ImageView imageView = (ImageView) findViewById(R.id.justImage);
        DragRectView dragRect = (DragRectView) findViewById(R.id.dragRect);

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

        int LEFT_OFFSET = (imageView.getWidth() - round(imageView.getDrawable().getIntrinsicWidth() * scaleX)) / 2;

        Log.i("image", LEFT_OFFSET + "");

        float[] points = {0, 0};
        imageView.getImageMatrix().mapPoints(points);
        Log.i("image", points[0] + " " + points[1]);




        RectF result1 = new RectF(currentRect);
        RectF result2 = new RectF(currentRect);

        Matrix matrix = imageView.getImageMatrix();
        Matrix inverse = new Matrix();
        matrix.invert(inverse);

        matrix.mapRect(result1);
        inverse.mapRect(result2);

        Log.i("image", currentRect.toShortString());
        Log.i("image", result1.toShortString());
        Log.i("image", result2.toShortString());



    }
}
