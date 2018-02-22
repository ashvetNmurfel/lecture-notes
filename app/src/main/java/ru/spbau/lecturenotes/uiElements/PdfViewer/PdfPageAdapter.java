package ru.spbau.lecturenotes.uiElements.PdfViewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.storage.LocalFile;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;
import ru.spbau.lecturenotes.temp.PdfPage;

public class PdfPageAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final ListView parent;
    private final File file;
    private PdfRenderer pdfRenderer;
    private final int A4_WIDTH = 900;
    private final int A4_HEIGHT = A4_WIDTH*297/210;

    public PdfPageAdapter(Context context, File file, ListView parent) {
        this.context = context;
        this.file = file;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.parent = parent;

        try {
            ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor
                    .open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(fileDescriptor);
            Log.i("pdf", "Set bitmap image");
        } catch (IOException e) {
            Log.e("pdf", "IOException while reading pdf");
        }
    }

    @Override
    public int getCount() {
        return pdfRenderer == null ? 0 : pdfRenderer.getPageCount();
    }

    @Override
    public Object getItem(int i) {
        if (pdfRenderer != null) {
            PdfRenderer.Page page = pdfRenderer.openPage(i);
            Bitmap bitmap = Bitmap.createBitmap(A4_WIDTH, A4_HEIGHT, Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            page.close();
            return bitmap;
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View oldView, ViewGroup viewGroup) {
        View view = oldView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_page_item, viewGroup, false);
        } else {
            RelativeLayout relativeLayout = (RelativeLayout) view;
            ShowRectView rectView = (ShowRectView) relativeLayout.getChildAt(1);
            rectView.setRect(new Rect());
        }
        if (pdfRenderer != null) {
            PdfRenderer.Page page = pdfRenderer.openPage(i);
            RelativeLayout relativeLayout = (RelativeLayout) view;
            PhotoView photoView = (PhotoView) relativeLayout.getChildAt(0);
            Log.i("getView", photoView.getWidth() + " " + photoView.getHeight());
            Log.i("getView", photoView.getMaxWidth() + " " + photoView.getMaxHeight());
            Bitmap bitmap = Bitmap.createBitmap(A4_WIDTH, A4_HEIGHT, Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            photoView.setImageBitmap(bitmap);
            page.close();
        }
        return view;
    }
}
