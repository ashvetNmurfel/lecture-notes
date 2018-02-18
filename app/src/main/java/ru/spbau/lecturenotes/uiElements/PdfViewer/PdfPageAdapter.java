package ru.spbau.lecturenotes.uiElements.PdfViewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.temp.PdfPage;

public class PdfPageAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<PdfPage> pageList;
    private final LayoutInflater layoutInflater;
    private final ListView parent;

    public PdfPageAdapter(Context context, ArrayList<PdfPage> pageList, ListView parent) {
        this.context = context;
        this.pageList = pageList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.parent = parent;
    }

    @Override
    public int getCount() {
        return pageList.size();
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
//        PdfPage page = pageList.get(i);
//        final PhotoView photoView = view.findViewById(R.id.pagePicture);
//        photoView.setImageResource(R.drawable.term1_algebra_05);

        final DragRectView dragRectView = view.findViewById(R.id.dragRect);

//        dragRectView.setOnClickCallback(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "DragRect to Zoom", Toast.LENGTH_SHORT).show();
//                view.setVisibility(View.GONE);
//
//                parent.setFrozen(false);
//            }
//        });
//
//        photoView.setOnViewTapListener(new OnViewTapListener() {
//            @Override
//            public void onViewTap(View view, float x, float y) {
//                Toast.makeText(context, "Zoom to DragRect", Toast.LENGTH_SHORT).show();
//                dragRectView.setVisibility(View.VISIBLE);
//
//                parent.setFrozen(true);
//            }
//        });

        dragRectView.setVisibility(View.GONE);

        return view;
    }
}