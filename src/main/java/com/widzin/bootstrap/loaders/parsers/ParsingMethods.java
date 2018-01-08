package com.widzin.bootstrap.loaders.parsers;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParsingMethods {

    public static String getPeriodSeason(String directory) {
        return new File(directory).getName().replaceAll("_", "/");
    }

    public static Date parseDate(String dateStr, String format, Locale locale) {
        Date dateObject = null;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, locale);
        try {
            dateObject = dateFormatter.parse(dateStr);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return dateObject;
    }

    public static String cutAge(String text) {
        return text.split(" ")[0] + "-"
                + text.split(" ")[1] + "-"
                + text.split(" ")[2];
    }

    public static String getFormattedDate(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.format(date);
        } else {
            return "null";
        }
    }
}
