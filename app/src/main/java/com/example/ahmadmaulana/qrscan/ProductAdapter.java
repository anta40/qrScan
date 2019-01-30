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
 * Created by Andre Tampubolon (andre.tampubolon@idstar.co.id) on 1/30/2019.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<Product> productList;
    private Context ctxt;

    public ProductAdapter(Context ctxt, List<Product> list){
        this.ctxt = ctxt;
        this.productList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_new, parent, false);

        return new ProductAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product prod = productList.get(position);
        holder.tvJumlah.setText("Jumlah: "+prod.getJumlah());
        holder.tvHargaJual.setText("Harga jual: "+prod.getHargaJual());
        holder.tvHargaBeli.setText("Harga beli: "+prod.getHargaBeli());
        holder.tvNama.setText(prod.getNama());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNama, tvHargaJual, tvHargaBeli, tvJumlah;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvHargaBeli = itemView.findViewById(R.id.tv_harga_beli);
            tvHargaJual = itemView.findViewById(R.id.tv_harga_jual);
            tvJumlah = itemView.findViewById(R.id.tv_jumlah);
        }
    }
}
