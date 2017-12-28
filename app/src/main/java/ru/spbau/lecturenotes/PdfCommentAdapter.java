package ru.spbau.lecturenotes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ru.spbau.lecturenotes.data.PdfComment;
import ru.spbau.lecturenotes.data.attachments.PictureAttachment;

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
        TextView textView = (TextView) view.findViewById(R.id.pdfComment);
        textView.setText(comment.getContent());
//        Drawable drawable = ((PictureAttachment) comment.getAttachments().get(0)).getDrawable();
//        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

        return view;
    }
}
