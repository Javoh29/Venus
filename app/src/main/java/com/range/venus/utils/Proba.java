package com.range.venus.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Proba {

    private void get(String oldingi_yil){

        String ad = "09." + oldingi_yil;

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.yyyy");
        try {
            Date date = dateFormat.parse(ad);

            boolean tugadi = true;
            while (tugadi){
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MONTH, 1);
                date =  cal.getTime();
                if(dateFormat.format(new Date()).equals(dateFormat.format(date))){

                    tugadi = false;
                    return;
                } else{

                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
