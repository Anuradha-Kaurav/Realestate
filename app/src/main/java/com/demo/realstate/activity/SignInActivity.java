package com.demo.realstate.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demo.realstate.R;
import com.demo.realstate.extra.CommonMethod;
import com.demo.realstate.extra.RequestQueueSingleton;
import com.demo.realstate.extra.SessionManager;
import com.demo.realstate.extra.URLConstants;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = SignInActivity.class.getSimpleName();
    KProgressHUD hud;
    EditText email_ph, password;
    boolean isPasswordVisible;
    ImageView showHidePassword;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        String msg = i.getStringExtra("msg");
        if(msg!= null){
            CommonMethod.snackBarWithAction(SignInActivity.this, msg);
        }
        setContentView(R.layout.activity_new_signin);
        requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        initView();
    }

    private void initView() {
        email_ph = findViewById(R.id.email_ph);
        password = findViewById(R.id.password);
        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.forgotPassword).setOnClickListener(this);
        findViewById(R.id.notAMember).setOnClickListener(this);
        showHidePassword  = findViewById(R.id.visibility);

        hud = KProgressHUD.create(SignInActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        if(!isPasswordVisible){
            showHidePassword.setImageDrawable(getDrawable(R.drawable.ic_visibility));
        }else{
            showHidePassword.setImageDrawable(getDrawable(R.drawable.ic_visibility_off));
        }

        showHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHidePassword(isPasswordVisible);
            }
        });
    }

    private void showHidePassword(boolean passwordVisibility){
        if(!passwordVisibility){
            // show password
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            showHidePassword.setImageDrawable(getDrawable(R.drawable.ic_visibility));
            isPasswordVisible = true;
        }else{
            // hide password
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            showHidePassword.setImageDrawable(getDrawable(R.drawable.ic_visibility_off));
            isPasswordVisible = false;
        }
    }

    @Override
    public void onClick(View view) {

        if (CommonMethod.isNetworkAvailable(this)) {
            switch (view.getId()) {
                case R.id.loginButton:
                    credentialValidation();
                    break;
                case R.id.forgotPassword:
                    startActivity(new Intent(this, ForgotPasswordActivity.class));
                    break;
                case R.id.notAMember:
                    startActivity(new Intent(this, NewSignupActivity.class));
                    break;
                default:
                    break;
            }
        } else {
            CommonMethod.snackBarWithAction(this, getString(R.string.no_internet));
        }
    }

    private void credentialValidation() {
        try {
            //Hiding keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        String enteredEmail_ph = email_ph.getText().toString().trim();
        String enteredPassword = password.getText().toString().trim();
        if (!TextUtils.isEmpty(enteredEmail_ph) && !TextUtils.isEmpty(enteredPassword)) {
            loginService(enteredEmail_ph, enteredPassword);
        } else {
            if (enteredEmail_ph.length() <= 0) {
                email_ph.setError("Please Enter Your UserName");
            } else if (enteredPassword.length() <= 0) {
                password.setError("Please Enter Your Password");
            }
        }
    }

    private void loginService(String email_ph, String password) {

            hud.show();
            JSONObject json = new JSONObject();
            try {
                json.put("username", email_ph);
                json.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = URLConstants.LOGIN;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            try {
                                String id = null, emailId = null, mobileno = null, accType = null;
                                hud.dismiss();
                                //String status = (String) response.get("status");
//                                if (status.equalsIgnoreCase("success")) {
                                if (response.has("userId")) {
                                    id = response.getString("userId");
                                    emailId = response.getString("emailId");
                                    mobileno = response.getString("mobileno");
                                    accType = response.getString("acctype");

                                    userLogin(emailId, id, mobileno, accType);

                                } else {
                                    CommonMethod.simpleSnackBar(SignInActivity.this, response.get("message").toString());
                                }
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hud.dismiss();
                    System.out.println("Error getting response");
                }
            });
            requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void userLogin( String emailId, String id, String phNum, String accType){

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.createLoginSession(emailId, id, phNum, accType);
        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();

    }
}
