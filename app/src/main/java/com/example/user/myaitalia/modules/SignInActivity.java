package com.example.user.myaitalia.modules;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.myaitalia.R;
import com.example.user.myaitalia.util.MyApplication;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.user.myaitalia.modules.PlanActivity.MY_PREFS_NAME;

public class SignInActivity extends AppCompatActivity {
    TextInputEditText txtUserName, txtPassword;
    Button btnLogin;
    TextView txtForgotPass, txtSignUp;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    ProgressDialog P;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("isNewComer", 1);
        editor.apply();
        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int UID = prefs.getInt("uid", -1);
        if(UID != -1){
            startActivity(new Intent(this, PlanActivity.class));
        }
        P = new ProgressDialog(this);

        initialize();

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.txtv_signup:
                startActivity(new Intent(SignInActivity.this, SignUpActivty.class));
                break;
            case R.id.txtv_forgot_pass:
                startActivity(new Intent(SignInActivity.this, ForgotPassActivity.class));
                break;
            case R.id.btn_login:
                if(txtUserName.getText().length()>0 && txtPassword.getText().length()>0) {
                    String Email = txtUserName.getText().toString();
                    String Pass = txtPassword.getText().toString();
//                authenticateUser(Email,Pass);
                    P.setMessage("Please Wait....");
                    P.show();
                    sendAndRequestResponse(Email, Pass);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Enter All Fields First", Toast.LENGTH_SHORT).show();
                }
                    break;


        }
    }

    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
    private void initialize() {
        txtUserName = findViewById(R.id.edt_login_email);
        txtPassword = findViewById(R.id.edt_login_pass);
        txtForgotPass = findViewById(R.id.txtv_forgot_pass);
        txtSignUp = findViewById(R.id.txtv_signup);


    }
    private void sendAndRequestResponse(String email, String pass) {
        String url = "http://myaitalia.com/backend/login_user.php?email="+email+"&pass="+pass;

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
                    if(success == 1){

                        uid = J.getJSONObject("user").getInt("uid");
                        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putInt("uid", uid);
                        editor.apply();
                        startActivity(new Intent(SignInActivity.this, PlanActivity.class));

                    }
                    else{
                        Toast.makeText(SignInActivity.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
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
