package ru.spbau.lecturenotes.uiElements.PdfViewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.temp.PdfComment;

public class PdfCommentAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater layoutInflater;
    ArrayList<PdfComment> commentList;

    public PdfCommentAdapter(Context context, ArrayList<PdfComment> commentList) {
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
    public View getView(int i, View oldView, ViewGroup viewGroup) {
        View view = oldView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_comment_item, viewGroup, false);
        }
        PdfComment comment = commentList.get(i);

        TextView textView = view.findViewById(R.id.pdfComment);
        textView.setText(comment.text);

        return view;
    }
}
