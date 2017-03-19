package com.example.new_home.termin30_test.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.new_home.termin30_test.R;
import com.example.new_home.termin30_test.model.NavigationItem;

import java.util.ArrayList;

/**
 * Created by New_home on 19.3.2017.
 */

public class DrawerAdapter extends BaseAdapter {
    // A reference to the context (i.e. the activity containing the adapter)
    Context context;
    ArrayList<NavigationItem> items;

    // Constructor should at least have context as a parameter
    public DrawerAdapter(Context context, ArrayList<NavigationItem> items) {

        this.context = context;
        this.items = items;
    }

    // Returns the item count
    @Override
    public int getCount() {
        return items.size();
    }

    // Returns an item
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    // Returns an item ID
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // Returns a view that corresponds with an item
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_item, null);
        }

        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        TextView tvSubtitle = (TextView) view.findViewById(R.id.subtitle);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.icon);

        tvTitle.setText(items.get(position).getTitle());
        tvSubtitle.setText(items.get(position).getSubtitle());
        ivIcon.setImageResource(items.get(position).getIcon());

        return view;
    }
}