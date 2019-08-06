package com.example.user.myaitalia.modules;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.myaitalia.R;
import com.example.user.myaitalia.adapters.CartAdapter;
import com.example.user.myaitalia.adapters.OrderAdapter;
import com.example.user.myaitalia.adapters.ProductAdapter;
import com.example.user.myaitalia.beans.ProductBean;
import com.example.user.myaitalia.interfaces.DataInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.myaitalia.modules.PlanActivity.MY_PREFS_NAME;

public class ViewOrderActivity extends AppCompatActivity  implements DataInterface {

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private int uid;
    RecyclerView recyclerView;
    private ProgressDialog P;
    private ArrayList<ProductBean> orderBeanList;
    private OrderAdapter orderAdapter;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        orderBeanList = new ArrayList<>();
        P = new ProgressDialog(this);
        P.setMessage("Please Wait..!!!");
        P.show();
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

        recyclerView =(RecyclerView) findViewById(R.id.rv_orders);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new
                DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        uid = prefs.getInt("uid", -1);
        getOrders(uid);


    }

    private void getOrders(int userID) {

        String url = "http://myaitalia.com/backend/get_all_orders.php?uid="+uid;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            private int cartId;

            @Override
            public void onResponse(String response) {
                P.cancel();
                try {
                    JSONObject J = new JSONObject(response);
                    int success = J.getInt("success");
                    if(success == 1){
                        JSONArray JA = J.getJSONArray("orders");
                        for(int i = 0; i< JA.length() ; i++)
                        {
                            ProductBean PB = new ProductBean();
                            JSONObject O = JA.getJSONObject(i);
                            int prodId = O.getInt("prod_id");

                            switch (prodId){
                                case 1:
                                    PB.setProductName("PIZZA");
                                    break;
                                case 2:
                                    PB.setProductName("PIZZA BASE");
                                    break;
                                case 3:
                                    PB.setProductName("RISOTTO");
                                    break;
                                case 4:
                                    PB.setProductName("FAINA");
                                    break;
                                case 5:
                                    PB.setProductName("CANTUCI");
                                    break;
                                case 6:
                                    PB.setProductName("CARNE");
                                    break;
                                case 7:
                                    PB.setProductName("FARINA INTEGRALE");
                                    break;
                                case 8:
                                    PB.setProductName("TIRMASU");
                                    break;
                                case 9:
                                    PB.setProductName("SALAD");
                                    break;
                                case 10:
                                    PB.setProductName("PASTA");
                                    break;
                            }
                            PB.setProductQuantity(O.getInt("prod_quantity"));
                            PB.setOrderDate(O.getString("order_time"));

                            orderBeanList.add(PB);
                        }

                        orderAdapter = new OrderAdapter(ViewOrderActivity.this, orderBeanList, ViewOrderActivity.this);
                        recyclerView.setAdapter(orderAdapter);

                    }
                    else{

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

    }
}
