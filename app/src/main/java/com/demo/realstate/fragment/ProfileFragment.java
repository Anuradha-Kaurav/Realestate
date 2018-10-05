package com.demo.realstate.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.realstate.R;
import com.demo.realstate.activity.HomeActivity;
import com.demo.realstate.activity.SignInActivity;
import com.demo.realstate.extra.SessionManager;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private static String TAG = HomeFragment.class.getCanonicalName();

    Context context;
    TextView userName, userEmail, userPhnum;
    TextView logout;
    SessionManager sessionManager;
    HashMap<String, String> userData;

    @SuppressLint("ValidFragment")
    public ProfileFragment(Context context) {
            this.context = context;
    }

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        userPhnum = view.findViewById(R.id.userPhnum);
        logout = view.findViewById(R.id.logout);

        userEmail.setVisibility(View.GONE);
        userPhnum.setVisibility(View.GONE);

        userData = sessionManager.getUserDetails();

       // userName.setText(userData.get(SessionManager.KEY_USERNAME));
        userName.setText("Hello, ");

        if(userData.get(SessionManager.KEY_USEREMAIl)!=null && !userData.get(SessionManager.KEY_USEREMAIl).isEmpty()){
            userEmail.setText(userData.get(SessionManager.KEY_USEREMAIl));
            userEmail.setVisibility(View.VISIBLE);
        }

        if(userData.get(SessionManager.KEY_USERPHONENUM)!=null && !userData.get(SessionManager.KEY_USERPHONENUM).isEmpty()){
            userPhnum.setText(userData.get(SessionManager.KEY_USERPHONENUM));
            userPhnum.setVisibility(View.VISIBLE);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logoutUser();
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

}
