package com.example.ahmadmaulana.qrscan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Andre Tampubolon (andre.tampubolon@idstar.co.id) on 1/30/2019.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<Product> productList;
    private Context ctxt;
    private MyClickListener clickListener;

    public ProductAdapter(Context ctxt, List<Product> list, MyClickListener listener){
        this.ctxt = ctxt;
        this.productList = list;
        this.clickListener = listener;
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
        holder.tvHargaJual.setText("Harga: "+prod.getHargaJual());
       // holder.tvHargaBeli.setText("Harga beli: "+prod.getHargaBeli());
        holder.tvNama.setText(prod.getNama());


        Picasso.get().load(prod.getUrl()).fit().into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNama, tvHargaJual, tvHargaBeli, tvJumlah;
        public Button btnInc, btnDec, btnDel;
        public ImageView imgView;
        MyClickListener listener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);
            //tvHargaBeli = itemView.findViewById(R.id.tv_harga_beli);
            tvHargaJual = itemView.findViewById(R.id.tv_harga_jual);
            tvJumlah = itemView.findViewById(R.id.tv_jumlah);
            btnInc = itemView.findViewById(R.id.btn_inc);
            btnDec = itemView.findViewById(R.id.btn_dec);
            btnDel = itemView.findViewById(R.id.btn_del);
            imgView = itemView.findViewById(R.id.image_view);

            btnInc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onIncrement(view, getAdapterPosition());
                }
            });

            btnDec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onDecrement(view, getAdapterPosition());
                }
            });

            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onDelete(view, getAdapterPosition());
                }
            });
        }
    }

    public interface MyClickListener {
        void onIncrement(View v, int position);
        void onDecrement(View v, int position);
        void onDelete(View v, int position);
    }
}
