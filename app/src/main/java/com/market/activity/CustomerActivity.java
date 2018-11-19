package com.market.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.market.R;
import com.market.adapter.CustomerGoodListAdapter;
import com.market.adapter.GoodListAdapter;
import com.market.util.JsonUtil;
import com.market.util.PostGetUtil;
import com.market.vo.Good;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CustomerActivity extends AppCompatActivity {

    private ListView listView;

    private List<Good> goodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.customer_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 0L);
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            Map<String,String> map = new HashMap<>();
            //map.put("sId",id + "");
            String s = PostGetUtil.SendGetRequest("/Good/querycustomergoodList", map);
            return JsonUtil.jsonDeserialize2List(s, Good.class);
        }).thenAccept(list -> {
            if (list == null) {
                goodList = new ArrayList<>();
            } else {
                goodList = new ArrayList<>();
                goodList.addAll(list);
            }
        });
        future.join();
        listView = findViewById(R.id.good_list_custom);
        CustomerGoodListAdapter adapter = new CustomerGoodListAdapter(goodList,this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id1) -> {
            Intent detailIntent  = new Intent(CustomerActivity.this,GoodDetailActivity.class);
            detailIntent.putExtra("id", id);
            detailIntent.putExtra("good",goodList.get(position));
            startActivity(detailIntent);
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.custom_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(CustomerActivity.this,ShoppingCartActivity.class);
                cartIntent.putExtra("id",id);
                startActivity(cartIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout :
                getSharedPreferences("login",MODE_PRIVATE).edit().clear().commit();
                Intent logout = new Intent(CustomerActivity.this,MainActivity.class);
                startActivity(logout);
                finish();
        }
        return true;
    }

}
