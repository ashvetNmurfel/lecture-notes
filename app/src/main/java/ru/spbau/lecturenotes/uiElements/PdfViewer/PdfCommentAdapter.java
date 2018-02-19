package ru.spbau.lecturenotes.uiElements.PdfViewer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.services.CommentSyncService;
import ru.spbau.lecturenotes.storage.Comment;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.firebase.FirebaseProxy;
import ru.spbau.lecturenotes.storage.identifiers.DiscussionId;

public class PdfCommentAdapter extends BaseAdapter {
    private CommentSyncService commentSyncService = new CommentSyncService(FirebaseProxy.getInstance());
    private Context context;
    LayoutInflater layoutInflater;
    List<DiscussionId> discussionIdList;

    public PdfCommentAdapter(Context context, List<DiscussionId> discussionIdList) {
        this.context = context;
        this.discussionIdList = discussionIdList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return discussionIdList.size();
    }

    @Override
    public Object getItem(int i) {
        return discussionIdList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View oldView, final ViewGroup viewGroup) {
        View view = oldView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_comment_item, viewGroup, false);
        }
        TextView textView = view.findViewById(R.id.pdfComment);
        textView.setText(R.string.CommentIsLoading);
        return view;
    }
}
