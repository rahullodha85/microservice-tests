package Utilities;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by 461967 on 12/23/2015.
 */
public class CommonFunctions {
    /**
     * Unique number funtion
     * @return
     */
    public static int getUniqueNumber() {

        GregorianCalendar date = new GregorianCalendar();

        int jDate = date.get(Calendar.DAY_OF_YEAR);
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int min = date.get(Calendar.MINUTE);
        int sec = date.get(Calendar.SECOND);

        int random = Integer.valueOf(String.valueOf(jDate) + String.valueOf(hour) + String.valueOf(min) + String.valueOf(sec));

        return random;
    }

    /**
     * Consolidate multiple cookies into one cookie header seperated by ';'
     * @param headers
     * @return
     */
    public static String createCookieHeader(List<String> headers) {
        String cookie = "";
        for (String item : headers) {
            if (cookie.equals("")) {
                cookie = item;
            } else {
                cookie = cookie + ";" + item;
            }
        }

        return cookie;
    }
}
