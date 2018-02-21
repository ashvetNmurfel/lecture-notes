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
import ru.spbau.lecturenotes.storage.identifiers.GroupId;

public class GroupListAdapter extends ArrayAdapter<GroupId> {
    private final Activity context;
    private final List<GroupId> groups;
    private final Consumer<GroupId> onClickCallback;

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

    public GroupListAdapter(Activity context, List<GroupId> groups, Consumer<GroupId> onClickCallback) {
        super(context, R.layout.listview_pdf_item, groups);
        this.context = context;
        this.groups = groups;
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

        holder.group = groups.get(position);
        holder.nameTextField.setText(holder.group.getName());
        holder.infoTextField.setText(holder.group.getDescription());

        return view;
    }

    private void onItemClicked(Context context, GroupId group) {
        Intent intent = new Intent(this.context, DocumentListActivity.class);
        Toast.makeText(context, group.getName(), Toast.LENGTH_LONG).show();
        onClickCallback.accept(group);
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
