package com.demo.realstate;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import java.lang.reflect.Method;

/**
 * Created by shabb on 9/10/2018.
 */

public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

//        if(Build.VERSION.SDK_INT>=24){
//            try{
//                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
//                m.invoke(null);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
    }
}
