package com.example.user.myaitalia.modules;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myaitalia.R;
import com.example.user.myaitalia.adapters.ProductAdapter;
import com.example.user.myaitalia.beans.ProductBean;
import com.example.user.myaitalia.interfaces.DataInterface;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.myaitalia.modules.PlanActivity.MY_PREFS_NAME;

public class ProductsActivity extends AppCompatActivity implements DataInterface {
    Button viewCart;
    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    List<ProductBean> productBeanList;
    TextView txtProductCount;
    private SharedPreferences.Editor editor;
    private List<ProductBean> tempList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

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

        tempList = new ArrayList<>();
        recyclerView =(RecyclerView) findViewById(R.id.rv_products);
        viewCart = findViewById(R.id.btn_goto_checkout);
        txtProductCount = findViewById(R.id.txt_prd_count);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new
                DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        productBeanList =new ArrayList<>();
        productBeanList.add(new ProductBean(1,"PIZZA MARGHERITA",6,1,R.drawable.pizza ,false));
        productBeanList.add(new ProductBean(2,"PIZZA BASSO",3,1,R.drawable.pizzabase,false));
        productBeanList.add(new ProductBean(3,"RISOTTO",5,1,R.drawable.risotto,false));
        productBeanList.add(new ProductBean(4,"FARINA TIPO 0",2,1,R.drawable.faina,false));
        productBeanList.add(new ProductBean(5,"CANTUCI",5,1,R.drawable.cantuci,false));
        productBeanList.add(new ProductBean(6,"HAMBURGER",3,1,R.drawable.carne,false));
        productBeanList.add(new ProductBean(7,"FARINA INTEGRALE",2,1,R.drawable.farinaintegrale,false));
        productBeanList.add(new ProductBean(8,"TIRAMASU",4,1,R.drawable.tiramasu,false));
        productBeanList.add(new ProductBean(9,"SALATA",3,1,R.drawable.salad,false));
        productBeanList.add(new ProductBean(10,"PASTA",3,1,R.drawable.pasta,false));





        productAdapter = new ProductAdapter(this,productBeanList, this);
        recyclerView.setAdapter(productAdapter);

        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempList.size() > 0){
                    startActivity(new Intent(ProductsActivity.this, CartActivity.class));
                }
                else{
                    Toast.makeText(ProductsActivity.this, "Please Add Item To Your Cart First", Toast.LENGTH_SHORT).show();
                }
            }
        });
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


    @Override
    public void setValues(List<ProductBean> productBeans) {
        tempList = productBeans;
        txtProductCount.setText("Item Selected: "+ productBeans.size());
    }
}
