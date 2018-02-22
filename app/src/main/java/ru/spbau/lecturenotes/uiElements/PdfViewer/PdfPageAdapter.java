package ru.spbau.lecturenotes.uiElements.PdfViewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.Arrays;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.temp.PdfPage;

public class PdfPageAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<PdfPage> pageList;
    private final LayoutInflater layoutInflater;
    private final ListView parent;

    private ArrayList<Integer> pageImages;
    {
        pageImages = new ArrayList<>(Arrays.asList(
                R.drawable.term1_algebra_03,
                R.drawable.term1_algebra_04,
                R.drawable.term1_algebra_05,
                R.drawable.term1_algebra_06,
                R.drawable.term1_algebra_07,
                R.drawable.term1_algebra_08,
                R.drawable.term1_algebra_09,
                R.drawable.term1_algebra_10,
                R.drawable.term1_algebra_11,
                R.drawable.term1_algebra_12));
    }


    public PdfPageAdapter(Context context, ArrayList<PdfPage> pageList, ListView parent) {
        this.context = context;
        this.pageList = pageList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.parent = parent;
    }

    @Override
    public int getCount() {
        return pageImages.size();
    }

    @Override
    public Object getItem(int i) {
        return pageList.get(i);
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
        }

        RelativeLayout relativeLayout = (RelativeLayout) view;
        PhotoView photoView = (PhotoView) relativeLayout.getChildAt(0);
        photoView.setImageResource(pageImages.get(i));
        return view;
    }
}
