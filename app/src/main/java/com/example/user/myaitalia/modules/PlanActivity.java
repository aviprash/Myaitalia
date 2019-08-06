package com.example.user.myaitalia.modules;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.user.myaitalia.R;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

public class PlanActivity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    CardView cardNormal, cardElite, cardShop;
    public static final String MY_PREFS_NAME = "MYAITALIA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        if(findViewById(android.R.id.home) != null){
            findViewById(android.R.id.home).setVisibility(View.GONE);
        }
        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.appbar_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER );
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view, params);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.my_orders:
                startActivity(new Intent(getApplicationContext(), ViewOrderActivity.class) );
                break;
            case R.id.sign_out:
                editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.remove("uid");
                editor.apply();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class) );
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.card_normal:
                editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("type", "normal");
                editor.apply();
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
            case R.id.card_elite:
                editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("type", "elite");
                editor.apply();
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
            case R.id.btn_about_normal:
                Toast.makeText(this, "normal", Toast.LENGTH_SHORT).show();
                new LovelyInfoDialog(this)
                        .setTopColorRes(R.color.colorPink)
                        .setTitle(R.string.info_title)
                        .setMessage(R.string.info_about_normal)
                        .show();
                break;
            case R.id.btn_about_elite:
                Toast.makeText(this, "elite", Toast.LENGTH_SHORT).show();
                new LovelyInfoDialog(this)
                        .setTopColorRes(R.color.colorPink)
                        .setTitle(R.string.info_title)
                        .setMessage(R.string.info_about_elite)
                        .show();
                break;
            case R.id.img_shop:
                startActivity(new Intent(this, ProductsActivity.class));
                break;
        }
    }
}
