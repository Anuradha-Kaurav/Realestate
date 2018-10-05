package com.demo.realstate.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.realstate.R;
import com.demo.realstate.extra.CommonMethod;
import com.demo.realstate.extra.SessionManager;

public class SplashActivity extends AppCompatActivity {
    Snackbar snackbar;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StartAnimations();

        session = new SessionManager(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (CommonMethod.isNetworkAvailable(getApplicationContext())) {
                    launchActivity();

                } else {
                    //
                    snackBarWithAction();
                }
            }
        }, 3000);
    }

    private void launchActivity() {

        if (CommonMethod.isNetworkAvailable(getApplicationContext())) {
//            startActivity(new Intent(SplashActivity.this, SignInActivity.class));
//            finish();
            try {
                Intent intent = null;
                if(session.isLoggedIn()){
                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }else{
                    intent = new Intent(SplashActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }
                startActivity(intent);
                finish();

            } catch (Exception e) {
                Log.e("TAG", "Exception in [SplashActivity] :: [run] :: " + e);
                e.printStackTrace();
            }

        } else {
            snackBarWithAction();
        }
    }



    private void snackBarWithAction() {

        snackbar = Snackbar.make(findViewById(android.R.id.content),R.string.no_internet, Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                launchActivity();
            }
        });
        // Changing message text color
        snackbar.setActionTextColor(Color.RED);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }


    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha_animation);
        anim.reset();
        RelativeLayout l = findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        relativeLayout.clearAnimation();
        relativeLayout.startAnimation(anim);

    }
}
