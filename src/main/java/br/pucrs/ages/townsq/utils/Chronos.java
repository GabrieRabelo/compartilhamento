package br.pucrs.ages.townsq.utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

public class Chronos {

    private static final PrettyTime prettyTime = new PrettyTime(new Locale("pt", "BR"));

    public static String dateToPrettyTimeString(Date date){
        return prettyTime.format(date);
    }

    public static String dateToPrettyTimeString(Timestamp timestamp){
        return prettyTime.format(new Date(timestamp.getTime()));
    }

}
