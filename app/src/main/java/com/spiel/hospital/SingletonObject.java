package com.spiel.hospital;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by learntodrill on 20/02/20.
 */

public class SingletonObject
{

    public  static SingletonObject instance;
    private JSONObject obj_doctorLogin;

    public  static SingletonObject Instance()
    {
        if (instance==null)
        {
            instance=new SingletonObject();
        }
        return  instance;
    }


    public  JSONObject getObj_DoctorDetails()
    {
        return  obj_doctorLogin;
    }

    public  void  setObj_DoctorDeatails(JSONObject obj_doctorLogin)
    {
        this.obj_doctorLogin = obj_doctorLogin;
    }


}
