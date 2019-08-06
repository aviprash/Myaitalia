package com.example.user.myaitalia.modules;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.TotalCaptureResult;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.myaitalia.R;
import com.example.user.myaitalia.adapters.CartAdapter;
import com.example.user.myaitalia.adapters.ProductAdapter;
import com.example.user.myaitalia.beans.ProductBean;
import com.example.user.myaitalia.interfaces.DataInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import static com.example.user.myaitalia.modules.PlanActivity.MY_PREFS_NAME;

public class CartActivity extends AppCompatActivity implements DataInterface {

    Button btnCheckOut;
    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    List<ProductBean> productBeanList;
    private TextView txtTotal;
    private List<ProductBean> cartBeanList;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressDialog P;
    private String uid = "12";
    private SharedPreferences.Editor editor;
    private double Total;
    private int cartID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartBeanList = new ArrayList<>();
        P = new ProgressDialog(this);
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

        recyclerView =(RecyclerView) findViewById(R.id.rv_cart);
        btnCheckOut = findViewById(R.id.btn_check_out);
        txtTotal = findViewById(R.id.txt_total);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new
                DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        cartAdapter = new CartAdapter(this, ProductAdapter.cartList, this);
        recyclerView.setAdapter(cartAdapter);

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(
                    View v) {
                P.setMessage("Please Wait...!");
                P.show();
//                btnCheckOut.setEnabled(false);
//                if(cartBeanList.size() > 0)
                   sendCartToServer(cartBeanList);
            }
        });

    }

    private void sendCartToServer(final List<ProductBean> cartBeanList) {

        String url = "http://myaitalia.com/backend/get_next_cart_id.php?uid="+uid;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            private int cartId;

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject J = new JSONObject(response);
                    int success = J.getInt("success");
                    cartID = J.getInt("cart_id");
                    if(success == 1){
                        for(int i=0; i<cartBeanList.size(); i++){
                            sendCartItemToServer(cartBeanList.get(i), cartID);
                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("TAG","Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }
    boolean isOrderCreated = false;

    private void sendCartItemToServer(ProductBean productBean, int CID) {

        String currentDate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String url = "http://myaitalia.com/backend/create_new_order.php?cart_id="+CID+"&prod_id="+productBean.getProductID()+"&prod_quantity="+productBean.getProductQuantity()+"&uid="+uid+"&ord_time="+currentDate;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                P.cancel();
                try{

                    JSONObject J = new JSONObject(response);
                    int success = J.getInt("success");

                    if(success == 1){
                        Log.d("MYAITALIA", 1+"");
                        if(!isOrderCreated){
                            Toast.makeText(CartActivity.this, "Order Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CartActivity.this, PaytmCheckoutActivity.class)
                                    .putExtra("cart_id", cartID)
                                    .putExtra("total",Total));
                            isOrderCreated = true;
                        }
                    }
                    else{
                        Log.d("MYAITALIA", 0+"");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG","Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);

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
        this.cartBeanList = productBeans;
        Total = 0;
        for (ProductBean P : productBeans) {
            Total += P.getProductTotal();
        }
        txtTotal.setText("Total Amount:  € "+ Total);

    }
}
