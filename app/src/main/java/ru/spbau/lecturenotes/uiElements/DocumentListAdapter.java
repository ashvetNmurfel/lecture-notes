package ru.spbau.lecturenotes.uiElements;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.function.Consumer;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.activities.DocumentListActivity;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;

public class DocumentListAdapter extends ArrayAdapter<DocumentId> {
    private final Activity context;
    private final List<DocumentId> documents;
    private final Consumer<DocumentId> onClickCallback;

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

    public DocumentListAdapter(Activity context, List<DocumentId> documents, Consumer<DocumentId> onClickCallback) {
        super(context, R.layout.listview_pdf_item, documents);
        this.context = context;
        this.documents = documents;
        this.onClickCallback = onClickCallback;
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

        holder.document = documents.get(position);
        holder.nameTextField.setText(holder.document.getFilename());
        holder.infoTextField.setText(holder.document.getKey());

        return view;
    }

    private void onItemClicked(Context context, DocumentId document) {
        Intent intent = new Intent(this.context, DocumentListActivity.class);
        Toast.makeText(context, document.getFilename(), Toast.LENGTH_LONG).show();
        onClickCallback.accept(document);
    }


    private class Holder implements View.OnClickListener {
        private TextView nameTextField;
        private TextView infoTextField;
        private DocumentId document;

        @Override
        public void onClick(View view) {
            DocumentListAdapter.this.onItemClicked(view.getContext(), document);
        }
    }
}
