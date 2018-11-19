package com.market.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.market.R;
import com.market.util.PostGetUtil;
import com.market.util.ToastUtil;
import com.market.vo.Good;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GoodDetailActivity extends AppCompatActivity {

    private Button addShoppingCartButton;

    private TextView goodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 0L);
        Good good = (Good) intent.getSerializableExtra("good");
        addShoppingCartButton = findViewById(R.id.addShoppingCart);
        goodName = findViewById(R.id.c_good_name);
        if (good != null) {
            goodName.setText(good.getGoodName());
        }

        addShoppingCartButton.setOnClickListener(view -> {
            CompletableFuture.supplyAsync(() -> {
                Map<String, String> map = new HashMap<>();
                map.put("customerid",id + "");
                map.put("goodid",good == null ? "" : good.getId() + "");
                return PostGetUtil.SendGetRequest("/shopping/addgood", map);
            }).thenAccept(result -> {
                if (result == null) {
                    ToastUtil.showToast(GoodDetailActivity.this,"添加异常",Toast.LENGTH_LONG);
                } else if (result.equals("0")) {
                    ToastUtil.showToast(GoodDetailActivity.this,"添加成功",Toast.LENGTH_LONG);
                    finish();
                } else {
                    ToastUtil.showToast(GoodDetailActivity.this,"添加失败",Toast.LENGTH_LONG);
                }
            });
        });

    }
}
