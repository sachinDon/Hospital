package com.spiel.hospital;

import android.app.Application;

import com.msg91.sendotpandroid.library.internal.SendOTP;

/**
 * Created by Rahul Hooda on 14/7/17.
 */

public class BaseApplication extends Application {

    AppEnvironment appEnvironment;

    @Override
    public void onCreate() {
        super.onCreate();
        appEnvironment = AppEnvironment.SANDBOX;
        SendOTP.initializeApp(this,"com.spiel.hospital");
    }

    public AppEnvironment getAppEnvironment() {
        return appEnvironment;
    }

    public void setAppEnvironment(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
    }
}
