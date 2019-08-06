package com.example.user.myaitalia.modules;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.myaitalia.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivty extends AppCompatActivity {

    TextInputEditText txtUsername,txtEmail, txtPassword, txtConfirmPass, txtPhone;
    Button btnRegister;
    RadioGroup genderGroup;
    ProgressDialog PD;
    int G = 1;
    // Advanced Apis
    RequestQueue queue ;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activty);

        initialize();

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.male:
                        G = 1;
                        break;
                    case R.id.female:
                        G = 0;
                        break;
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                PD = new ProgressDialog(SignUpActivty.this);
                PD.setMessage("Please Wait....");
                PD.show();
                final String U = txtUsername.getText().toString();
                final String E = txtEmail.getText().toString();
                final String P = txtPassword.getText().toString();
                final String CP = txtConfirmPass.getText().toString();
                final String PHN = txtPhone.getText().toString();
                int output = checkInput(U,E,P,CP,PHN);
                switch (output){
                    case 1 :
                        sendAndRequestResponse(U, E, P, PHN, G);
                        break;
                    case 303 :
                        Toast.makeText(getApplicationContext(), "Please Fill All The Fields", Toast.LENGTH_SHORT).show();
                        PD.cancel();
                        break;
                    case 304 :
                        Toast.makeText(getApplicationContext(), "Minimum lenght of password is 8", Toast.LENGTH_SHORT).show();
                        PD.cancel();
                        break;
                    case 305 :
                        Toast.makeText(getApplicationContext(), "Your password doesn't match", Toast.LENGTH_SHORT).show();
                        PD.cancel();
                        break;


                }
            }
        });



    }

    private int checkInput(String u, String e, String p, String cp, String phn) {

        if(u.isEmpty() || p.isEmpty() || cp.isEmpty()|| phn.isEmpty()|| e.isEmpty()){
            return 303;
        }
        if(p.length() < 7){
            return 304;
        }
        if (!p.equals(cp))
        {
            return 305;
        }
        return 1;
    }

    public void sendAndRequestResponse(String U, String E, String P, String PHN, int G) {
        String url = "http://myaitalia.com/backend/register_user.php?uname="+U+"&email="+E+"&pass="+P+"&phone="+PHN+"&gender="+G;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(SignUpActivty.this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                PD.cancel();
                try {
                    JSONObject J = new JSONObject(response);
                    int success = J.getInt("success");
                    if(success == 1){
                        startActivity(new Intent(SignUpActivty.this, SignUpConfirmationActivity.class));
                        String M = J.getString("message");
                        Toast.makeText(getApplicationContext(),M, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(SignUpActivty.this, "Error Occured", Toast.LENGTH_SHORT).show();
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
        txtUsername = findViewById(R.id.edt_username);
        txtEmail = findViewById(R.id.edt_email);
        txtPassword = findViewById(R.id.edt_password);
        txtConfirmPass = findViewById(R.id.edt_cnfrm_password);
        txtPhone = findViewById(R.id.edt_phone);
        genderGroup = findViewById(R.id.gender_group);
        btnRegister = findViewById(R.id.btn_signup);


    }

}
