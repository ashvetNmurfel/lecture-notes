package ru.spbau.lecturenotes;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.spbau.lecturenotes.data.PdfFileStorage;

public class PdfListAdapter extends ArrayAdapter<PdfFileStorage> {
    private final Activity context;
    private final ArrayList<PdfFileStorage> pdfs;

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0)
    {
        return true;
    }

    public PdfListAdapter(Activity context, ArrayList<PdfFileStorage> pdfs) {
        super(context, R.layout.listview_pdf_item, pdfs);
        this.context = context;
        this.pdfs = pdfs;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_pdf_item, null);
        TextView nameTextField = (TextView) rowView.findViewById(R.id.pdf_list_item_name);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.pdf_list_item_info);
        nameTextField.setText(pdfs.get(position).getName());
        infoTextField.setText(pdfs.get(position).getInfo());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                PdfFileStorage storage = pdfs.get(position);
                if (storage.isDirectory()) {
                    intent = new Intent(context, MainMenuActivity.class);
                    intent.putExtra("nodeId", storage.getName());
                } else {
                    intent = new Intent(context, PDFActivity_.class);
                    intent.putExtra("filename", storage.getFile());
                }
                context.startActivity(intent);
            }
        });
        return rowView;
    }
}
