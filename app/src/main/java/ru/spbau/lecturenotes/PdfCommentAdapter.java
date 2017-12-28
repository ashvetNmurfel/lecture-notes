package ru.spbau.lecturenotes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        if (comment.getAttachments().size() > 0) {
            setPic(((PictureAttachment) comment.getAttachments().get(0)).getPicturePath(), textView);
        }
        return view;
    }

    private void setPic(String mCurrentPhotoPath, View textView) {
        ImageView mImageView = (ImageView) textView.findViewById(R.id.myImage);

        if (mImageView == null) {
            Toast.makeText(context, "problememes: mImageView == null", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the dimensions of the View
        int targetW = 50;
        int targetH = 50;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

}
