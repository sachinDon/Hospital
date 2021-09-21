package com.spiel.hospital;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class AlertDialogView {
    AlertDialog alertDialog_Box;
    public  void  showAlert(Activity activity, String str_title,String str_messsage,String str_flag)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setTitle(str_title);
        builder1.setMessage(str_messsage);
        builder1.setCancelable(false);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (str_flag.equalsIgnoreCase("close"))
                        {
                            activity.finish();
                        }

                    }
                });
        alertDialog_Box = builder1.create();
        alertDialog_Box.show();
    }

}
