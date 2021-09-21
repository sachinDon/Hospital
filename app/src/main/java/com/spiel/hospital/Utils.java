package com.spiel.hospital;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Utils
{

public  static boolean isNetworkAwailabel(Context context)
{
    boolean isConnected;
    ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkinfo = null;
    if(cm != null);
    {
        networkinfo = cm.getActiveNetworkInfo();
    }
    isConnected = networkinfo != null && networkinfo.isConnectedOrConnecting();
    if(!isConnected)
    {
        Toast.makeText(context,"Please Check your internet Connection",Toast.LENGTH_SHORT).show();
    }

    return isConnected;
}

}
