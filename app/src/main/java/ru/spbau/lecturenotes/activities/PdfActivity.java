package ru.spbau.lecturenotes.activities;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.Arrays;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.temp.PdfPage;
import ru.spbau.lecturenotes.uiElements.PdfViewer.DragRectView;
import ru.spbau.lecturenotes.uiElements.PdfViewer.PdfPageAdapter;

public class PdfActivity extends AppCompatActivity {
    private ArrayList<ru.spbau.lecturenotes.temp.PdfComment> commentList = new ArrayList<>();
    private Rect currentRect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        ArrayList<PdfPage> arrayList = new ArrayList<>(Arrays.asList(new PdfPage(), new PdfPage(), new PdfPage()));

        ListView lv = findViewById(R.id.pdfPictureListView);
        PdfPageAdapter adapter = new PdfPageAdapter(this, arrayList, lv);
        lv.setAdapter(adapter);

        DragRectView dragRectView = findViewById(R.id.dragRect);
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

        ListView listView = findViewById(R.id.pdfPictureListView);
        FrameLayout frameLayout = findViewById(R.id.pdfPage);
        RelativeLayout viewModeSettings = findViewById(R.id.viewModeSettings);
        RelativeLayout commentModeSettings = findViewById(R.id.commentModeSettings);

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
//        CommentBuilder commentBuilder = new CommentBuilder("anon", editText.getText().toString());

//        ImageView imageView = (ImageView) findViewById(R.id.justImage);
        PhotoView photoView = (PhotoView) findViewById(R.id.photoView);
        DragRectView dragRect = (DragRectView) findViewById(R.id.dragRect);

        Log.i("image", "CurrentRect");
        Log.i("image", currentRect.toShortString());

        Log.i("image", "getMatrix");
        tryMatrix(photoView.getMatrix());

        Log.i("image", "getImageMatrix");
        tryMatrix(photoView.getImageMatrix());

        Matrix matrix = new Matrix();

        Log.i("image", "getDisplayMatrix");
        photoView.getDisplayMatrix(matrix);
        tryMatrix(matrix);

        Log.i("image", "getSuppMatrix");
        photoView.getSuppMatrix(matrix);
        tryMatrix(matrix);
    }

    private void tryMatrix(Matrix matrix) {
        RectF result1 = new RectF(currentRect);
        RectF result2 = new RectF(currentRect);

        Matrix inverse = new Matrix();
        matrix.invert(inverse);

        matrix.mapRect(result1);
        inverse.mapRect(result2);

        Log.i("image", round(result1).toShortString());
        Log.i("image", round(result2).toShortString());
    }

    private Rect round(RectF rectF) {
        Rect rect = new Rect();
        rectF.round(rect);
        return rect;
    }
}
