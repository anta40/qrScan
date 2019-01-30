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
    private ProductAdapter mAdapter;
    private DBHelper db;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

       // Bundle bundle = getIntent().getExtras();
        //String[] aaa = bundle.getStringArray("scan_result");

        //Toast.makeText(getApplicationContext(), aaa[1].replace("FN:","")+" "+aaa[2].replace("FN:","")+
          //      " "+aaa[3].replace("FN:","")+" "+aaa[4].replace("FN:","")
            //    +" "+aaa[5].replace("FN:",""), Toast.LENGTH_LONG).show();

        recView = (RecyclerView) findViewById(R.id.recycler_view);
        productList = new ArrayList<>();



        db = new DBHelper(this);
        productList.addAll(db.getAllProducts());

        mAdapter = new ProductAdapter(this, productList);
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
