package ru.spbau.lecturenotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.Arrays;

import ru.spbau.lecturenotes.data.PdfComment;

public class PdfActivity extends AppCompatActivity {
    public static final int COMMENT_MODE_ACTIVITY_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        ArrayList<PdfPage> arrayList = new ArrayList<>(Arrays.asList(new PdfPage(), new PdfPage(), new PdfPage()));

        TouchableListView lv = (TouchableListView) findViewById(R.id.pdfPictureListView);
        PdfPageAdapter adapter = new PdfPageAdapter(this, arrayList, lv);
        lv.setAdapter(adapter);

        View view = adapter.getView(0, null, null);
        PhotoView pagePicture = (PhotoView) view.findViewById(R.id.pagePicture);
    }

    public void onClickPlusButtonGoToCommentMode(View view) {
        Intent intent = new Intent(this, CommentActivity.class);
        startActivity(intent);
        startActivityForResult(intent, COMMENT_MODE_ACTIVITY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COMMENT_MODE_ACTIVITY_CODE) {

        }
    }
}
