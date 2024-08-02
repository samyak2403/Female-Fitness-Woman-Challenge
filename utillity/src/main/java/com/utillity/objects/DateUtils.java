package com.utillity.objects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String getDate(long time, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", locale);
        Date date = new Date();
        date.setTime(time);
        return sdf.format(date);
    }

}
