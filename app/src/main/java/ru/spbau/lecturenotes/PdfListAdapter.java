package ru.spbau.lecturenotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
        Holder holder;
        if (view == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.listview_pdf_item, null);
            holder = new Holder();
            holder.nameTextField = (TextView) view.findViewById(R.id.pdf_list_item_name);
            holder.infoTextField = (TextView) view.findViewById(R.id.pdf_list_item_info);
            view.setOnClickListener(holder);
            view.setTag(holder);
        } else {
            holder = (Holder)view.getTag();
        }

        holder.storage = pdfs.get(position);
        holder.nameTextField.setText(holder.storage.getName());
        holder.infoTextField.setText(holder.storage.getInfo());

        return view;
    }

    private void onItemClicked(Context context, PdfFileStorage storage) {
        Intent intent;
        if (storage.isDirectory()) {
            intent = MainMenuActivity.createIntentForNode(this.context, storage.getName());
        } else {
            intent = PDFActivity.createIntentForFile(this.context, storage.getFile());
            Toast.makeText(context, storage.getFile(), Toast.LENGTH_LONG).show();
        }
        this.context.startActivity(intent);
    }


    private class Holder implements View.OnClickListener {
        private TextView nameTextField;
        private TextView infoTextField;
        private PdfFileStorage storage;

        @Override
        public void onClick(View view) {
            PdfListAdapter.this.onItemClicked(view.getContext(), storage);
        }
    }
}
