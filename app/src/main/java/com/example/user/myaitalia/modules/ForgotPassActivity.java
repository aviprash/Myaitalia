package com.example.user.myaitalia.modules;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import static com.example.user.myaitalia.modules.PlanActivity.MY_PREFS_NAME;

public class ForgotPassActivity extends AppCompatActivity {

    Button btnForgotPass;
    EditText edtEmail;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressDialog P;
    TextView txtForgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        P = new ProgressDialog(this);
        P.setMessage("Please Wait..!");
        btnForgotPass = findViewById(R.id.btn_forgot_pass);
        edtEmail = findViewById(R.id.edt_email_frgt_pass);
        txtForgotPass = findViewById(R.id.txt_response);

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P.show();
                String email = "";
                if(email.length() < 0){
                    sendPassToEmailIfExist(email);   
                }
                else{
                    Toast.makeText(ForgotPassActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        
        
        
    }

    private void sendPassToEmailIfExist(String email) {
        String url = "http://myaitalia.com/backend/forgot_password_email.php?email="+email;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            private int uid;

            @Override
            public void onResponse(String response) {
                P.cancel();

                try {
                    JSONObject J = new JSONObject(response);
                    int success = J.getInt("success");
                    String message = J.getString("message");
                    if(success == 1){
                        txtForgotPass.setText(message);
                    }
                    else{
                        txtForgotPass.setText(message);
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
}
