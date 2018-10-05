package com.demo.realstate.extra;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class CommonMethod {

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
    private static Snackbar snackbar;

    public static void snackBarWithAction(Activity activity, String value) {

        snackbar = Snackbar.make(activity.findViewById(android.R.id.content), value, Snackbar.LENGTH_INDEFINITE).setAction("OK ", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();

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

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void simpleSnackBar(Activity activity, String value) {

        snackbar = Snackbar.make(activity.findViewById(android.R.id.content), value, Snackbar.LENGTH_SHORT);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }
    private static void sendMail(Activity activity) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "sumit@dominatorhouse.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Likes for like,complaint id :13123123 " );
        activity.startActivity(Intent.createChooser(emailIntent, "Send email..."));

    }


}
