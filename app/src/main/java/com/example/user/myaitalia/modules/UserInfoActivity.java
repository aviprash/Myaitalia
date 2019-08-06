package com.example.user.myaitalia.modules;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.myaitalia.R;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.user.myaitalia.modules.PlanActivity.MY_PREFS_NAME;

public class UserInfoActivity extends AppCompatActivity {

    TextInputLayout txtDiseaseLayout;
    TextInputEditText txtWeight, txtHeight,txtDisease;
    Button btnSubmit;
    Spinner spinnerbodyType, spinnerCigarettes, spinnerAlcohol;
    ProgressDialog PD;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    String url = "";
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        if (findViewById(android.R.id.home) != null) {
            findViewById(android.R.id.home).setVisibility(View.GONE);
        }
        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.appbar_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view, params);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        initialize();

        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("type", null);
        if (restoredText != null) {
            switch (restoredText) {
                case "normal":
                    txtDiseaseLayout.setVisibility(View.GONE);
                    break;
                case "elite":
                    txtDiseaseLayout.setVisibility(View.VISIBLE);
                    new LovelyInfoDialog(this)
                            .setTopColorRes(R.color.colorPink)
                            .setNotShowAgainOptionEnabled(0)
                            .setNotShowAgainOptionChecked(true)
                            .setTitle(R.string.info_title)
                            .setMessage(R.string.info_message)
                            .show();
                    break;
            }
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtHeight.getText().length()>0 && txtWeight.getText().length()>0){

                PD.setMessage("Please Wait..");
                PD.show();
                String W = txtWeight.getText().toString();
                String H = txtHeight.getText().toString();
                String D = txtDisease.getText().toString();
                String C = spinnerCigarettes.getSelectedItem().toString();
                String A = spinnerAlcohol.getSelectedItem().toString();
                String BT = spinnerbodyType.getSelectedItem().toString();
                int UID = prefs.getInt("uid", -1);
                url = "http://myaitalia.com/backend/set_user_info.php?weight=" + W + "&height=" +
                        H + "&cigs=" + C + "&alchl=" + A + "&dis=" + D +
                        "&btype=" + BT + "&uid=" + UID;
                sendAndRequestResponse(W, H, C, A, D, BT, UID);

                }
                else{
                    Toast.makeText(getApplicationContext(), "Enter All Fields First", Toast.LENGTH_SHORT);
                }
            }
        });


    }
    public void sendAndRequestResponse(String W, String H, String C, String A , String D, String BT, int UID) {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(UserInfoActivity.this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                PD.cancel();
                try {
                    JSONObject J = new JSONObject(response);
                    int success = J.getInt("success");
                    if(success == 1){
                        startActivity(new Intent(getApplicationContext(), ProductsActivity.class));
                        String M = J.getString("message");
                        Toast.makeText(getApplicationContext(),M, Toast.LENGTH_SHORT).show();
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

    private void initialize() {
        txtWeight = findViewById(R.id.edt_weight);
        txtHeight = findViewById(R.id.edt_height);
        txtDisease = findViewById(R.id.edt_disease);
        txtDiseaseLayout = findViewById(R.id.txt_disease);
        spinnerCigarettes = findViewById(R.id.spn_cigarettes);
        spinnerAlcohol = findViewById(R.id.spn_alcohol);
        spinnerbodyType = findViewById(R.id.spn_body);
        btnSubmit = findViewById(R.id.btn_submit);

        PD = new ProgressDialog(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}


