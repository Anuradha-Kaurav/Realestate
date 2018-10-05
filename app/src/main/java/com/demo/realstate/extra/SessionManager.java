package com.demo.realstate.extra;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.demo.realstate.activity.HomeActivity;
import com.demo.realstate.activity.SignInActivity;

import java.util.HashMap;

/**
 * Created by shabb on 9/8/2018.
 */

public class SessionManager {
    public static String TAG = SessionManager.class.getCanonicalName();
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "realstate";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_USEREMAIl = "email";
    public static final String KEY_USERNAME = "name";
    public static final String KEY_USERPHONENUM = "phone";

    public static final String KEY_USERAccType = "accType";

    // Email address (make variable public to access from outside)
    public static final String KEY_USERID = "id";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String email, String id, String phNum, String accType) {
        try {
            Log.i(TAG, "In [createLoginSession] :: email : " + email);
            Log.i(TAG, "In [createLoginSession] :: id : " + id);
            Log.i(TAG, "In [createLoginSession] :: phNum : "+ phNum);
            Log.i(TAG, "In [createLoginSession] :: accType : "+ accType);
            // Storing login value as TRUE
            editor.putBoolean(IS_LOGIN, true);
            // Storing name in pref
            editor.putString(KEY_USEREMAIl, email);
            // Storing email in pref
            editor.putString(KEY_USERPHONENUM, phNum);
            editor.putString(KEY_USERAccType, accType);
            editor.putString(KEY_USERID, id);
            // commit changes
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, "Exception in [createLoginSession] :: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        try {
            Log.i(TAG, "In [checkLogin] :: isLoggedIn : " + this.isLoggedIn());
            // Check login status
            if (!this.isLoggedIn()) {
                // user is not logged in redirect him to Login com.cowtit.quote.Activity
                Intent i = new Intent(_context, SignInActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new com.cowtit.quote.Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Staring Login com.cowtit.quote.Activity
                _context.startActivity(i);
            } else {
                Intent intent = new Intent(_context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                _context.startActivity(intent);

            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in [checkLogin] :: " + e);
            e.printStackTrace();
        }

    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_USEREMAIl, pref.getString(KEY_USEREMAIl, null));
        user.put(KEY_USERPHONENUM, pref.getString(KEY_USERPHONENUM, null));
        user.put(KEY_USERAccType, pref.getString(KEY_USERAccType, null));
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));

        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        try {
            // Clearing all data from Shared Preferences
            editor.clear();
            editor.commit();

//            // After logout redirect user to Loing com.cowtit.quote.Activity
//            Intent i = new Intent(_context, SignInActivity.class);
//            // Closing all the Activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new com.cowtit.quote.Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            // Staring Login com.cowtit.quote.Activity
//            _context.startActivity(i);

        } catch (Exception e) {
            Log.e(TAG, "Exception in [logoutUser] :: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}