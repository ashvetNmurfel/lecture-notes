package ru.spbau.lecturenotes.activities;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.services.CommentSyncService;
import ru.spbau.lecturenotes.storage.Comment;
import ru.spbau.lecturenotes.storage.Discussion;
import ru.spbau.lecturenotes.storage.DiscussionLocation;
import ru.spbau.lecturenotes.storage.Rectangle;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.firebase.FirebaseProxy;
import ru.spbau.lecturenotes.storage.identifiers.DiscussionId;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;
import ru.spbau.lecturenotes.storage.requests.CommentSketch;
import ru.spbau.lecturenotes.storage.requests.DiscussionSketch;
import ru.spbau.lecturenotes.temp.PdfPage;
import ru.spbau.lecturenotes.uiElements.PdfViewer.DragRectView;
import ru.spbau.lecturenotes.uiElements.PdfViewer.PdfCommentAdapter;
import ru.spbau.lecturenotes.uiElements.PdfViewer.PdfPageAdapter;
import ru.spbau.lecturenotes.uiElements.PdfViewer.ShowRectView;

public class PdfActivity extends AppCompatActivity {
    private CommentSyncService commentSyncService = new CommentSyncService(FirebaseProxy.getInstance());
    private DocumentId documentId;
    private int currentPage = 0;
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
                setDiscussionListForCurrentPage();
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
        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DiscussionId discussionId = (DiscussionId) adapterView.getAdapter().getItem(i);
                RelativeLayout pageView = (RelativeLayout) pdfPageListView.getChildAt(discussionId.getLocation().getPage());
                PhotoView pagePictureView = (PhotoView) pageView.getChildAt(0);
                ShowRectView showRectView = (ShowRectView) pageView.getChildAt(1);

                int height = pagePictureView.getDrawable().getIntrinsicHeight();
                int width = pagePictureView.getDrawable().getIntrinsicWidth();

                Matrix m = new Matrix();
                pagePictureView.getDisplayMatrix(m);
                m.preScale(width, height);

                RectF rectF = new RectF(discussionId.getLocation().getRectangle().getRect());
                m.mapRect(rectF);
                Rect rect = new Rect();
                rectF.round(rect);

                Log.i("ShowRect", rect.toShortString());

                showRectView.setRect(rect);
            }
        });
    }

    private void setDiscussionListForCurrentPage() {
        final ListView commentListView = findViewById(R.id.commentsList);

        List<DiscussionId> currentPageDiscussionIdList = new ArrayList<>();
        for (DiscussionId discussionId : discussionIdList) {
            if (discussionId.getLocation().getPage() == currentPage) {
                currentPageDiscussionIdList.add(discussionId);
            }
        }

        PdfCommentAdapter commentAdapter = new PdfCommentAdapter(PdfActivity.this, currentPageDiscussionIdList);
        commentListView.setAdapter(commentAdapter);

        for (int k = 0; k < currentPageDiscussionIdList.size(); k++) {
            final int i = k;
            final DiscussionId discussionId = discussionIdList.get(i);
            commentSyncService.listenToCommentList(discussionId, new ResultListener<List<Comment>>() {
                @Override
                public void onResult(List<Comment> result) {
                    if (result.size() == 0) {
                        Log.e("comments", "A discussion " + discussionId.getKey() + " with 0 comments found, ignoring.");
                        return;
                    }
                    Comment comment = result.get(0);
                    LinearLayout linearLayout = (LinearLayout) commentListView.getChildAt(i);
                    TextView textView = (TextView) linearLayout.getChildAt(0);
                    textView.setText(comment.getContent().getText());
                }

                @Override
                public void onError(Throwable error) {
                    Log.e("comments", "Error while loading discussion " + discussionId.getKey());
                }
            });
        }
    }

    private void onPageChange() {

    }


    private boolean isInCommentMode;

    public void onClickPlusButtonGoToCommentMode(View view) {
        changeViewerCommentMode();
    }

    @Override
    public void onBackPressed() {
        if (isInCommentMode) {
            changeViewerCommentMode();
        } else {
            super.onBackPressed();
        }
    }

    public void onClickButtonSendComment(View view) {
        EditText editText = findViewById(R.id.editTextComment);

        PhotoView photoView = findViewById(R.id.photoView);
        DragRectView dragRect = findViewById(R.id.dragRect);

        int height = photoView.getDrawable().getIntrinsicHeight();
        int width = photoView.getDrawable().getIntrinsicWidth();

        RectF rectF = new RectF(dragRect.getCurrentRect());

        Matrix m = new Matrix();
        photoView.getDisplayMatrix(m);
        m.invert(m);
        m.postScale((float) 1.0 / width, (float) 1.0 / height);

        m.mapRect(rectF);

        DiscussionLocation discussionLocation = new DiscussionLocation(currentPage, new Rectangle(rectF));
        CommentSketch commentSketch = new CommentSketch(editText.getText().toString());
        DiscussionSketch discussionSketch = new DiscussionSketch(commentSketch, discussionLocation);

        commentSyncService.addDiscussion(documentId, discussionSketch, new ResultListener<Discussion>() {
            @Override
            public void onResult(Discussion result) {
                Log.i("", "Comment saved to DB");
                Toast.makeText(getApplicationContext(), "Sent!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable error) {
                Log.i("", "Error");
                Toast.makeText(getApplicationContext(), "Error. Comment has not been sent", Toast.LENGTH_SHORT).show();
            }
        });

        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setChecked(false);
        editText.getText().clear();
        dragRect.setVisibility(View.GONE);
        dragRect.invalidate();
        changeViewerCommentMode();
        Toast.makeText(getApplicationContext(), "Sending...", Toast.LENGTH_SHORT).show();
    }

    public void onToggleButtonChangeMode(View view) {
        DragRectView dragRectView = findViewById(R.id.dragRect);
        dragRectView.setVisibility(dragRectView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    private void changeViewerCommentMode() {
        ListView listView = findViewById(R.id.pdfPictureListView);
        FrameLayout frameLayout = findViewById(R.id.pdfPage);
        RelativeLayout viewModeSettings = findViewById(R.id.viewModeSettings);
        RelativeLayout commentModeSettings = findViewById(R.id.commentModeSettings);
        if (isInCommentMode) {
            isInCommentMode = false;
            listView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            viewModeSettings.setVisibility(View.VISIBLE);
            commentModeSettings.setVisibility(View.GONE);
        } else {
            isInCommentMode = true;
            listView.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            viewModeSettings.setVisibility(View.GONE);
            commentModeSettings.setVisibility(View.VISIBLE);
        }
    }
}
