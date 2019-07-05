package com.lobxy.cryptoprices.Controller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lobxy.cryptoprices.Model.Datum;
import com.lobxy.cryptoprices.R;

import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private List<Datum> list;

    public CustomRecyclerViewAdapter(List<Datum> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_list_item, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {
        customViewHolder.text_name.setText("Name: " + list.get(i).getName());
        customViewHolder.text_symbol.setText("Sym: " + list.get(i).getSymbol());
        customViewHolder.text_price.setText(list.get(i).getQuote().getUSD().getPrice() + "USD");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
