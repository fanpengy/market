package com.market.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.market.R;
import com.market.util.JsonUtil;
import com.market.util.PostGetUtil;
import com.market.util.ToastUtil;
import com.market.vo.Good;

import java.util.concurrent.CompletableFuture;

public class CreateGoodActivity extends AppCompatActivity {

    private EditText goodNameEditText;

    private EditText reserveEditText;

    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_good);
        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 0L);
        boolean create = intent.getBooleanExtra("create",true);
        Good goodEdit = null;

        goodNameEditText = findViewById(R.id.goodName);
        reserveEditText = findViewById(R.id.goodReserve);
        createButton = findViewById(R.id.goodCreateButton);
        if (!create) {
            goodEdit = (Good) intent.getSerializableExtra("good");
            goodNameEditText.setText(goodEdit.getGoodName());
            reserveEditText.setText(goodEdit.getNum() + "");
            createButton.setText("保存");
        }
        Good finalGoodEdit = goodEdit;
        boolean[] success = { true };
        createButton.setOnClickListener(v -> {
            Good good = new Good(goodNameEditText.getText().toString(),Integer.parseInt(reserveEditText.getText().toString()),id);
            CompletableFuture.runAsync(() -> {
                String s;
                if (create) {
                   s = PostGetUtil.SendPostRequest("/Good/addGood", JsonUtil.jsonSerialize(good));
                } else {
                    good.setId(finalGoodEdit.getId());
                    s = PostGetUtil.SendPostRequest("/Good/editGood", JsonUtil.jsonSerialize(good));
                }
                if (s == null || !s.equals("0")) {
                    ToastUtil.showToast(CreateGoodActivity.this,create ? "创建失败" : "修改失败",Toast.LENGTH_LONG);
                    success[0] = false;
                }
            }).join();
            /*if (success[0]) {
                Intent back = new Intent(CreateGoodActivity.this,MerchantActivity.class);
                back.putExtra("id",id);
                startActivity(back);
                finish();
            }*/
            Intent back = new Intent(CreateGoodActivity.this,MerchantActivity.class);
            back.putExtra("id",id);
            startActivity(back);
            finish();
        });
    }
}
