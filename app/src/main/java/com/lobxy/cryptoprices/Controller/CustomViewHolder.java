package com.lobxy.cryptoprices.Controller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lobxy.cryptoprices.R;

class CustomViewHolder extends RecyclerView.ViewHolder {

    public TextView text_name;
    public TextView text_symbol;
    public TextView text_price;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);

        text_name = itemView.findViewById(R.id.listitem_name);
        text_symbol = itemView.findViewById(R.id.listitem_symbol);
        text_price = itemView.findViewById(R.id.listitem_price);

    }
}
