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

import java.util.ArrayList;
import java.util.List;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.data.PdfFileStorage;
import ru.spbau.lecturenotes.storage.identifiers.GroupId;

public class GroupListAdapter extends ArrayAdapter<GroupId> {
    private final Activity context;
    private final List<GroupId> groups;

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

    public GroupListAdapter(Activity context, List<GroupId> groups) {
        super(context, R.layout.listview_pdf_item, groups);
        this.context = context;
        this.groups = groups;
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

        holder.group = groups.get(position);
        holder.nameTextField.setText(holder.group.getName());
        holder.infoTextField.setText(holder.group.getKey());

        return view;
    }

    private void onItemClicked(Context context, GroupId group) {
        Intent intent;
        if (false) {
            //intent = MainMenuActivity.createIntentForNode(this.context, storage.getName());
            //this.context.startActivity(intent);
        } else {
            //intent = PDFActivity.createIntentForFile(this.context, storage.getFile());
            Toast.makeText(context, group.getName(), Toast.LENGTH_LONG).show();
        }
    }


    private class Holder implements View.OnClickListener {
        private TextView nameTextField;
        private TextView infoTextField;
        private GroupId group;

        @Override
        public void onClick(View view) {
            GroupListAdapter.this.onItemClicked(view.getContext(), group);
        }
    }
}
