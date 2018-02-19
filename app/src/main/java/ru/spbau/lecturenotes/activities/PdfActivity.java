package ru.spbau.lecturenotes.activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.services.CommentSyncService;
import ru.spbau.lecturenotes.storage.Discussion;
import ru.spbau.lecturenotes.storage.DiscussionLocation;
import ru.spbau.lecturenotes.storage.Rectangle;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.firebase.FirebaseProxy;
import ru.spbau.lecturenotes.storage.identifiers.DiscussionId;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;
import ru.spbau.lecturenotes.storage.requests.CommentSketch;
import ru.spbau.lecturenotes.storage.requests.DiscussionSketch;
import ru.spbau.lecturenotes.temp.PdfComment;
import ru.spbau.lecturenotes.temp.PdfPage;
import ru.spbau.lecturenotes.uiElements.PdfViewer.DragRectView;
import ru.spbau.lecturenotes.uiElements.PdfViewer.PdfCommentAdapter;
import ru.spbau.lecturenotes.uiElements.PdfViewer.PdfPageAdapter;
import ru.spbau.lecturenotes.uiElements.PdfViewer.ShowRectView;

public class PdfActivity extends AppCompatActivity {
    private ArrayList<PdfComment> commentList = new ArrayList<>();
    private DocumentId documentId;
    private int currentPage = 0;

    private CommentSyncService commentSyncService = new CommentSyncService(FirebaseProxy.getInstance());

    private List<DiscussionId> discussionIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        documentId = (DocumentId) getIntent().getExtras().get("documentId");
        setTitle(documentId.getFilename());

        commentSyncService.listenToDiscussionList(documentId, new ResultListener<List<DiscussionId>>() {
            @Override
            public void onResult(List<DiscussionId> result) {
                discussionIdList = result;
            }

            @Override
            public void onError(Throwable error) {
            }
        });


        ArrayList<PdfPage> pageArrayList = new ArrayList<>(Arrays.asList(new PdfPage(), new PdfPage(), new PdfPage()));

        final ListView pdfPageListView = findViewById(R.id.pdfPictureListView);
        final PdfPageAdapter pdfPageAdapter = new PdfPageAdapter(this, pageArrayList, pdfPageListView);
        pdfPageListView.setAdapter(pdfPageAdapter);

        pdfPageListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (currentPage != firstVisibleItem) {
                    Log.i("pdf", "currentPage " + firstVisibleItem);
                    currentPage = firstVisibleItem;
                    onPageChange();
                }
            }
        });

        final ListView commentListView = findViewById(R.id.commentsList);
        PdfCommentAdapter commentAdapter = new PdfCommentAdapter(this, commentList);
        commentListView.setAdapter(commentAdapter);

        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PdfComment pdfComment = (PdfComment) adapterView.getAdapter().getItem(i);
                RelativeLayout pageView = (RelativeLayout) pdfPageListView.getChildAt(0);
                PhotoView pagePictureView = (PhotoView) pageView.getChildAt(0);
                ShowRectView showRectView = (ShowRectView) pageView.getChildAt(2);

                int height = pagePictureView.getDrawable().getIntrinsicHeight();
                int width = pagePictureView.getDrawable().getIntrinsicWidth();

                Matrix m = new Matrix();
                pagePictureView.getDisplayMatrix(m);
                m.preScale(width, height);

                RectF rectF = new RectF(pdfComment.rectF);
                m.mapRect(rectF);
                Rect rect = new Rect();
                rectF.round(rect);

                Log.i("ShowRect", rect.toShortString());

                showRectView.setRect(rect);
            }
        });

        commentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });


        commentList.add(ru.spbau.lecturenotes.temp.PdfComment.get());
        commentList.add(ru.spbau.lecturenotes.temp.PdfComment.get());
        commentList.add(ru.spbau.lecturenotes.temp.PdfComment.get());
    }

    private void onPageChange() {

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
        EditText editText = findViewById(R.id.editTextComment);

        PhotoView photoView = findViewById(R.id.photoView);
        DragRectView dragRect = findViewById(R.id.dragRect);

        PdfComment comment = new PdfComment();
        comment.text = editText.getText().toString();

        int height = photoView.getDrawable().getIntrinsicHeight();
        int width = photoView.getDrawable().getIntrinsicWidth();

        RectF rectF = new RectF(dragRect.getCurrentRect());

        Matrix m = new Matrix();
        photoView.getDisplayMatrix(m);
        m.invert(m);
        m.postScale((float) 1.0 / width, (float) 1.0 / height);

        m.mapRect(rectF);
        comment.rectF = rectF;
        comment.text = rectF.toShortString();
        commentList.add(comment);

        DiscussionLocation discussionLocation = new DiscussionLocation(currentPage, new Rectangle());
        CommentSketch commentSketch = new CommentSketch(editText.getText().toString());
        DiscussionSketch discussionSketch = new DiscussionSketch(commentSketch, discussionLocation);

        commentSyncService.addDiscussion(documentId, discussionSketch, new ResultListener<Discussion>() {
            @Override
            public void onResult(Discussion result) {
                Toast.makeText(getApplicationContext(), "Comment saved to DB", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(), "Comment WAS NOT saved to DB", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void onToggleButtonChangeMode(View view) {
        DragRectView dragRectView = findViewById(R.id.dragRect);
        dragRectView.setVisibility(dragRectView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }
}
