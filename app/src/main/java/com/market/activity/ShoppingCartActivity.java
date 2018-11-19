package com.market.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.market.R;
import com.market.adapter.CustomerGoodListAdapter;
import com.market.util.JsonUtil;
import com.market.util.PostGetUtil;
import com.market.vo.Good;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ShoppingCartActivity extends AppCompatActivity {

    private ListView listView;

    private List<Good> goodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 0L);
        CompletableFuture.supplyAsync(() -> {
            Map<String,String> map = new HashMap<>();
            map.put("customerid",id + "");
            String result = PostGetUtil.SendGetRequest("/Good/queryaddedlist", map);
            return JsonUtil.jsonDeserialize2List(result,Good.class);
        }).thenAccept(list -> {
            if (list == null) {
                goodList = new ArrayList<>();
            } else {
                goodList = new ArrayList<>();
                goodList.addAll(list);
            }
        }).join();
        listView = findViewById(R.id.cart_good_list);
        CustomerGoodListAdapter adapter = new CustomerGoodListAdapter(goodList,this);
        listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
