package com.quyuanjin.imseven.utils;

import com.google.gson.Gson;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHelper {

    public static String getCurrentTime(){
        Date day=new Date();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println(df.format(day));


        return df.format(day);
    }

}
