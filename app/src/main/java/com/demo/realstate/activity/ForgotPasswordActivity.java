package com.demo.realstate.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demo.realstate.R;
import com.demo.realstate.extra.CommonMethod;
import com.demo.realstate.extra.RequestQueueSingleton;
import com.demo.realstate.extra.URLConstants;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    ImageView backButton;
    EditText email_ph, otp;
    TextView otpSentText;
    Button submit;
    KProgressHUD hud;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        backButton = findViewById(R.id.back);
        email_ph = findViewById(R.id.email_ph);
        otp = findViewById(R.id.enter_otp);
        otpSentText = findViewById(R.id.otpsent);
        submit = findViewById(R.id.cntinue);

        requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        hud = KProgressHUD.create(ForgotPasswordActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CommonMethod.isNetworkAvailable(ForgotPasswordActivity.this)) {
                    if(credentialValidation(email_ph)){
                        requestOTP(email_ph.getText().toString().trim());
                    }
                }else{
                    CommonMethod.snackBarWithAction(ForgotPasswordActivity.this, getString(R.string.no_internet));
                }

            }
        });
    }

    private boolean credentialValidation(EditText email_ph) {
        boolean result =  false;
        try {
            //Hiding keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        if(!email_ph.getText().toString().isEmpty()){
            if(TextUtils.isDigitsOnly(email_ph.getText())){
                if(email_ph.getText().toString().trim().length() == 10){
                    result = true;
                }else{
                    CommonMethod.snackBarWithAction(ForgotPasswordActivity.this, "Please enter 10 digit phone number");
                }
            }else{
                if(CommonMethod.isValidEmail(email_ph.getText().toString().trim())){
                    result = true;
                }else{
                    CommonMethod.snackBarWithAction(ForgotPasswordActivity.this, "Please enter valid email id");
                }
            }
        }else{
            CommonMethod.snackBarWithAction(ForgotPasswordActivity.this, getString(R.string.enter_email_ph));
        }

        return result;
    }

    private void  requestOTP(String username){
        hud.show();
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("json: ", json.toString());
        String url = URLConstants.FORGOT_PASSWORD;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("reg res: ", response.toString());

                        hud.dismiss();
                        try {
                            // String status = (String) response.get("status");
                            if (response.getString("status").equalsIgnoreCase("success")) {
                                CommonMethod.simpleSnackBar(ForgotPasswordActivity.this, response.getString("message"));

                                otpSentText.setVisibility(View.VISIBLE);
                                otp.setVisibility(View.VISIBLE);
                            } else {
                                    CommonMethod.simpleSnackBar(ForgotPasswordActivity.this, response.getString("message"));
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hud.dismiss();
                Log.e("Volley Error", error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
