package ru.spbau.lecturenotes.uiElements.PdfViewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.services.CommentSyncService;
import ru.spbau.lecturenotes.storage.Comment;
import ru.spbau.lecturenotes.storage.firebase.FirebaseProxy;

public class PdfCommentAdapter extends BaseAdapter {
    private CommentSyncService commentSyncService = new CommentSyncService(FirebaseProxy.getInstance());
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Comment> commentList;

    public PdfCommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentList.get(i);
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
        textView.setText(commentList.get(i).getContent().getText());
        return view;
    }
}
