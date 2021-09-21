package com.spiel.hospital;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangable extends BroadcastReceiver
{

    Intent intent = null;
        @Override
    public void onReceive(Context context, Intent intent) {
        try{
if (!Utils.isNetworkAwailabel(context))
{
    if (intent == null)
    {
        intent = new Intent(context,OtpmsgActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }
}
