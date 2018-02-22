package ru.spbau.lecturenotes.activities;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.services.AttachmentManager;
import ru.spbau.lecturenotes.services.CommentSyncService;
import ru.spbau.lecturenotes.services.DocumentsManager;
import ru.spbau.lecturenotes.services.FileManagerInterface;
import ru.spbau.lecturenotes.services.FileSyncService;
import ru.spbau.lecturenotes.services.MetadataSyncService;
import ru.spbau.lecturenotes.storage.Comment;
import ru.spbau.lecturenotes.storage.Discussion;
import ru.spbau.lecturenotes.storage.DiscussionLocation;
import ru.spbau.lecturenotes.storage.Document;
import ru.spbau.lecturenotes.storage.LocalFile;
import ru.spbau.lecturenotes.storage.Rectangle;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.firebase.FirebaseProxy;
import ru.spbau.lecturenotes.storage.identifiers.CommentId;
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
    private FileSyncService fileSyncService;
    private DocumentId documentId;
    private int currentPage = 0;
    private List<DiscussionId> discussionIdList = new ArrayList<>();
    private ListView commentListView;
    private final List<Comment> commentList = new ArrayList<Comment>();
    private PdfCommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        fileSyncService = new FileSyncService(
                FirebaseProxy.getInstance(),
                AttachmentManager.initInstance(getApplicationContext()),
                DocumentsManager.initInstance(getApplicationContext()));

        documentId = (DocumentId) getIntent().getExtras().get("documentId");

        commentSyncService.getDocument(documentId, new ResultListener<Document>() {
            @Override
            public void onResult(Document result) {
                Log.i("pdf", "Got Document, setting DocDownld Listener");
                fileSyncService.onDocumentDownloaded(result, new ResultListener<LocalFile<DocumentId>>() {
                    @Override
                    public void onResult(LocalFile<DocumentId> result) {
                        Log.i("pdf", "onDocumentDownloaded, onResult");

                        ListView pdfPageListView = findViewById(R.id.pdfPictureListView);
                        PdfPageAdapter pdfPageAdapter = new PdfPageAdapter(getApplicationContext(), result.getFile(), pdfPageListView);
                        pdfPageListView.setAdapter(pdfPageAdapter);
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });
            }

            @Override
            public void onError(Throwable error) {

            }
        });

        commentAdapter = new PdfCommentAdapter(PdfActivity.this, commentList);
        commentListView = findViewById(R.id.commentsList);
        commentListView.setAdapter(commentAdapter);

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

        final ListView pdfPageListView = findViewById(R.id.pdfPictureListView);
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

        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Comment comment = (Comment) adapterView.getAdapter().getItem(i);
                DiscussionId discussionId = comment.getId().getDiscussionId();
                Log.i("onItemClick", discussionId.getLocation().getPage() + "");
                RelativeLayout pageView = (RelativeLayout) pdfPageListView.getChildAt(0);

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


        onPageChange();
    }


    private boolean isShowingRect() {
        ListView pdfPageListView = findViewById(R.id.pdfPictureListView);
        RelativeLayout pageView = (RelativeLayout) pdfPageListView.getChildAt(0);
        if (pageView != null) {
            ShowRectView showRectView = (ShowRectView) pageView.getChildAt(1);
            return !showRectView.getRect().isEmpty();
        }
        return false;
    }

    private void setDiscussionListForCurrentPage() {
        List<DiscussionId> currentPageDiscussionIdList = new ArrayList<>();
        for (DiscussionId discussionId : discussionIdList) {
            if (discussionId.getLocation().getPage() == currentPage) {
                currentPageDiscussionIdList.add(discussionId);
            }
        }

        commentSyncService.getDiscussions(currentPageDiscussionIdList, new ResultListener<List<Discussion>>() {
            @Override
            public void onResult(List<Discussion> result) {
                List<CommentId> commentIdList = new ArrayList<>();
                for (Discussion discussion : result) {
                    if (discussion.getComments().isEmpty()) {
                        continue;
                    }
                    commentIdList.add(discussion.getComments().get(0));
                }

                commentSyncService.getComments(commentIdList, new ResultListener<List<Comment>>() {
                    @Override
                    public void onResult(List<Comment> result) {
                        commentList.clear();
                        commentList.addAll(result);
                        commentAdapter.notifyDataSetChanged();
                        findViewById(R.id.commentsList).setVisibility(View.VISIBLE);
                        findViewById(R.id.commentsListProgressBar).setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });
            }

            @Override
            public void onError(Throwable error) {

            }
        });


        commentAdapter.notifyDataSetChanged();
    }

    private void dropRect() {
        ListView pdfPageListView = findViewById(R.id.pdfPictureListView);
        RelativeLayout pageView = (RelativeLayout) pdfPageListView.getChildAt(0);
        if (pageView != null) {
            ShowRectView showRectView = (ShowRectView) pageView.getChildAt(1);
            showRectView.setRect(new Rect());
        }
    }

    private void onPageChange() {
        setTitle(documentId.getFilename() + " - " + (currentPage + 1));

        dropRect();

        commentList.clear();
        commentAdapter.notifyDataSetChanged();
        findViewById(R.id.commentsList).setVisibility(View.GONE);
        findViewById(R.id.commentsListProgressBar).setVisibility(View.VISIBLE);
        setDiscussionListForCurrentPage();
    }


    private boolean isInCommentMode;

    public void onClickPlusButtonGoToCommentMode(View view) {
        changeViewerCommentMode();
        ListView listView = findViewById(R.id.pdfPictureListView);
        ListAdapter listAdapter = listView.getAdapter();
        PhotoView photoView = findViewById(R.id.photoView);
        photoView.setImageBitmap((Bitmap) listAdapter.getItem(currentPage));
    }

    @Override
    public void onBackPressed() {
        if (isInCommentMode) {
            changeViewerCommentMode();
        } else if (isShowingRect()) {
            dropRect();
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