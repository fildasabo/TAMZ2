package com.example.tamz2aplikace.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tamz2aplikace.R;

import java.util.List;

/**
 *Slouží pro přidělování obrázků medaile do aktivity skore
 */

public class AdapterZebricek extends BaseAdapter {

    private Context context;
    private List<Zebricek> lstSkore;

    public AdapterZebricek(Context context, List<Zebricek> lstSkore) {
        this.context = context;
        this.lstSkore = lstSkore;
    }

    @Override
    public int getCount() {
        return lstSkore.size();
    }

    @Override
    public Object getItem(int position) {
        return lstSkore.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.radek, null);

        ImageView imgTop = (ImageView)view.findViewById(R.id.imgTop);
        TextView txtTop = (TextView)view.findViewById(R.id.txtTop);

        if(lstSkore.get(position).getMedaile() == 1) //top1 - první místo
            imgTop.setImageResource(R.drawable.med1);
        else if(lstSkore.get(position).getMedaile() == 2) //top2 - druhé místo
            imgTop.setImageResource(R.drawable.med2);
        else if(lstSkore.get(position).getMedaile() == 3) //top3 - třetí místo
            imgTop.setImageResource(R.drawable.med3);
        else imgTop.setImageResource(R.drawable.med); //top4 - ostatní místa

        txtTop.setText(String.format("%.1f",lstSkore.get(position).getScore()));
        return view;
    }
}