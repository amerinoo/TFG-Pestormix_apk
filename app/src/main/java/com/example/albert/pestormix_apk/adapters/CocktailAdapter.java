package com.example.albert.pestormix_apk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.repositories.CocktailRepository;
import com.example.albert.pestormix_apk.models.Cocktail;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Albert on 26/01/2016.
 */
public class CocktailAdapter extends BaseAdapter {
    private Context context;
    private List<Cocktail> cocktails;

    public CocktailAdapter(Context context, List<Cocktail> cocktails) {
        this.context = context;
        this.cocktails = cocktails;
    }

    @Override
    public int getCount() {
        return cocktails.size();
    }

    @Override
    public Cocktail getItem(int position) {
        return cocktails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Cocktail cocktail = getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_list_manually, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.findViewById(R.id.linear_remove).setVisibility(View.GONE);
        holder.name.setText(cocktail.getName());
        holder.name.setBackgroundResource(R.drawable.primary_color_rectangle_with_solid_background);

        return convertView;
    }

    public void update(Realm realm) {
        cocktails = CocktailRepository.getCocktails(realm);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView name;
    }
}
