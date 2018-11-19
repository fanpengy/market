package com.market.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.market.R;
import com.market.util.JsonUtil;
import com.market.util.PostGetUtil;
import com.market.util.ToastUtil;
import com.market.vo.LoginResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.market.vo.InterfaceConstant.USER_LOGIN;
import static com.market.vo.InterfaceConstant.USER_REGISTER;

public class MainActivity extends AppCompatActivity {

    private EditText userNameEditText;

    private EditText passwordEditText;

    private Button loginButton;

    private Button registerButton;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        userNameEditText  = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        spinner = findViewById(R.id.identitySpinner);
       /* Intent test = new Intent(MainActivity.this,TestActivity.class);
        startActivity(test);
        finish();*/
        List<String> list = Arrays.asList("游客","商户");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        final int[] selected = {0};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected[0] = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected[0] = 1;
            }
        });


        loginButton.setOnClickListener(v -> {
            String user = userNameEditText.getText().toString();
            String pwd = passwordEditText.getText().toString();
            if (user.equals("")) {
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            } else if (pwd.equals("")) {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            } else {
                Map<String,String> map = new HashMap<>();
                map.put("name",userNameEditText.getText().toString());
                map.put("pwd",passwordEditText.getText().toString());
                CompletableFuture.supplyAsync(() -> {
                    String s = PostGetUtil.SendGetRequest(USER_LOGIN, map);
                    return JsonUtil.jsonDeserialize(s, LoginResult.class);
                }).thenAccept(result -> {
                    if (result == null) {
                        ToastUtil.showToast(this, "网络异常", Toast.LENGTH_SHORT);
                    } else {
                        switch (result.getEnum1()) {
                            case 1 :
                                ToastUtil.showToast(this, "用户不存在", Toast.LENGTH_SHORT);
                                userNameEditText.setText("");
                                passwordEditText.setText("");
                                break;
                            case 2 :
                                ToastUtil.showToast(this, "密码错误", Toast.LENGTH_SHORT);
                                passwordEditText.setText("");
                                break;
                            case 3 :
                                SharedPreferences.Editor editc = preferences.edit();
                                editc.putString("name",userNameEditText.getText().toString());
                                editc.putString("pwd",passwordEditText.getText().toString());
                                editc.commit();
                                ToastUtil.showToast(this, "登录成功", Toast.LENGTH_SHORT);
                                Intent customerIntent = new Intent(MainActivity.this,CustomerActivity.class);
                                customerIntent.putExtra("id",result.getId());
                                startActivity(customerIntent);
                                finish();
                                break;
                            case 4 :
                                SharedPreferences.Editor edit = preferences.edit();
                                edit.putString("name",userNameEditText.getText().toString());
                                edit.putString("pwd",passwordEditText.getText().toString());
                                edit.commit();
                                ToastUtil.showToast(this, "登录成功", Toast.LENGTH_SHORT);
                                Intent merchantIntent = new Intent(MainActivity.this,MerchantActivity.class);
                                merchantIntent.putExtra("id",result.getId());
                                startActivity(merchantIntent);
                                finish();
                                break;
                            default:
                                ToastUtil.showToast(this, "后端返回了个什么东西", Toast.LENGTH_SHORT);
                                break;
                        }
                    }
                });
            }

        });

        registerButton.setOnClickListener(view -> {
            String user = userNameEditText.getText().toString();
            String pwd = passwordEditText.getText().toString();
            if (user.equals("")) {
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            } else if (pwd.equals("")) {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            } else {
                Map<String,String> map = new HashMap<>();
                map.put("name",userNameEditText.getText().toString());
                map.put("pwd",passwordEditText.getText().toString());
                map.put("identity",selected[0] + "");
                CompletableFuture.supplyAsync(() -> {
                    try {
                        String result = PostGetUtil.SendGetRequest(USER_REGISTER, map);
                        return result.equals("0") ? "注册成功" : "注册失败";
                    } catch (Exception e) {
                        return "注册异常";
                    }
                }).thenAccept(msg -> {
                    ToastUtil.showToast(MainActivity.this,msg,Toast.LENGTH_LONG);
                });
            }


        });
        String name = preferences.getString("name", null);
        String pwd = preferences.getString("pwd", null);
        if (name != null && pwd != null) {
            userNameEditText.setText(name);
            passwordEditText.setText(pwd);
            //loginButton.performClick();
        }
    }

}
