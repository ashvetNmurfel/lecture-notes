package ru.spbau.lecturenotes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.spbau.lecturenotes.data.PdfFileStorage;

public class PdfListAdapter extends ArrayAdapter<PdfListAdapter> {
    public PdfListAdapter(Activity context, ArrayList<PdfFileStorage> pdfs) {
        super(context, R.layout.listview_pdf_item);
        this.context = context;
        this.pdfs = pdfs;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_pdf_item, null, true);
        TextView nameTextField = (TextView) rowView.findViewById(R.id.pdf_list_item_name);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.pdf_list_item_info);
        nameTextField.setText(pdfs.get(position).getName());
        infoTextField.setText(pdfs.get(position).getInfo());
        return rowView;
    }

    private final Activity context;
    private final ArrayList<PdfFileStorage> pdfs;
}
