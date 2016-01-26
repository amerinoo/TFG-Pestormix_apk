package com.example.albert.pestormix_apk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.models.Drink;

import java.util.List;

/**
 * Created by Albert on 26/01/2016.
 */
public class ItemsAdapter extends BaseAdapter {
    private Context context;
    private List<Drink> drinks;
    private View.OnClickListener removeListener;


    public ItemsAdapter(Context context, List<Drink> drinks) {
        super();
        this.context = context;
        this.drinks = drinks;
    }

    @Override
    public int getCount() {
        return drinks.size();
    }

    @Override
    public Drink getItem(int position) {
        return drinks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Drink drink = getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_list_manually, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.remove = (ImageButton) convertView.findViewById(R.id.remove);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(drink.getName());
        holder.remove.setOnClickListener(removeListener);
        holder.remove.setTag(position);

        return convertView;
    }

    public void setRemoveListener(View.OnClickListener removeListener) {
        this.removeListener = removeListener;
    }

    public void addDrink(Drink drink) {
        drinks.add(drink);
        notifyDataSetChanged();
    }

    public void removeDrink(int position) {
        drinks.remove(position);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView name;
        ImageButton remove;
    }
}
