package com.example.ahmadmaulana.qrscan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//import ezvcard.Ezvcard;
//import ezvcard.VCard;

public class ScanResultActivity extends AppCompatActivity {

    private RecyclerView recView;
    private Product2Adapter mAdapter;
    private DBHelper db;
    private List<Product2> product2List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        Bundle bundle = getIntent().getExtras();
        recView = (RecyclerView) findViewById(R.id.recycler_view);
        product2List = new ArrayList<>();

        db = new DBHelper(this);
        product2List.addAll(db.getAllProducts2());

        mAdapter = new Product2Adapter(this, product2List);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recView.setLayoutManager(mLayoutManager);
        recView.setItemAnimator(new DefaultItemAnimator());
        recView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recView.setAdapter(mAdapter);
       // String url = bundle.getString("prod_url");
        //String location = bundle.getString("prod_location");
        //int price = bundle.getInt("prod_price");
        //Toast.makeText(getApplicationContext(),url+" "+location+" "+price, Toast.LENGTH_SHORT).show();

    }
}
