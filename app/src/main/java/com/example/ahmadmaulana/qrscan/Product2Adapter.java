package com.example.ahmadmaulana.qrscan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Andre Tampubolon (andre.tampubolon@idstar.co.id) on 1/24/2019.
 */
public class Product2Adapter extends RecyclerView.Adapter<Product2Adapter.MyViewHolder> {

    private Context ctxt;
    private List<Product2> productList;

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvUrl, tvLocation, tvPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUrl = itemView.findViewById(R.id.tvUrl);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }

    public Product2Adapter(Context ctxt, List<Product2> list){
        this.ctxt = ctxt;
        this.productList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product2 prod = productList.get(position);
        holder.tvUrl.setText(prod.getUrl());
        holder.tvLocation.setText(prod.getLokasi());
        holder.tvPrice.setText(""+prod.getHarga());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
