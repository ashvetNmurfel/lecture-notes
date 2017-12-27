package ru.spbau.lecturenotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.spbau.lecturenotes.data.PdfComment;

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
        ((TextView) view.findViewById(R.id.pdfComment)).setText(comment.getContent());
        ((TextView) view.findViewById(R.id.pdfComment)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_launcher, 0, 0, 0);

        return null;
    }
}
