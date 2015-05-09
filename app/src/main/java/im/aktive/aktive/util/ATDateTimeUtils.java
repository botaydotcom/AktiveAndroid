package im.aktive.aktive.util;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by hoangtran on 19/7/14.
 */

public class ATDateTimeUtils {
    static final String DATETIME_FORMAT_RAILS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    static final String DATETIME_FORMAT_FOR_DISPLAY= "d MMM yyyy, EEE hh:mmaaa";
    static final String DATETIME_FORMAT_FOR_DISPLAY_SAME_YEAR= "d MMM, EEE hh:mmaaa";

    static final String DATE_FORMAT_FOR_DISPLAY = "d MMM yyyy, EEE";
    static final String DATE_FORMAT_FOR_DISPLAY_SAME_YEAR = "d MMM, EEE";
    static final String BIRTH_DATE_FORMAT_FOR_DISPLAY = "d MMM yyyy";

    static final String DATE_BIRTHDAY_FORMAT_DISPLAY= "yyyy-MM-dd";

    static final String TIME_FORMAT_FOR_DISPLAY= "hh:mmaaa";

    static SimpleDateFormat dateTimeFormatter = null;
    static SimpleDateFormat dateTimeFormatterSameYear = null;

    static SimpleDateFormat dateFormatter = null;
    static SimpleDateFormat dateFormatterSameYear = null;

    static SimpleDateFormat timeFormatter = null;
    static SimpleDateFormat railsDateTimeFormatter = null;

    static SimpleDateFormat dateDisplayFormatter = null;
    static SimpleDateFormat serverDateBirthFormatter = null;
    static SimpleDateFormat displayDateBirthFormatter = null;
	
	/*
	 * LAZY INITER
	 */

    private static SimpleDateFormat getDateTimeFormatter()
    {
        if (dateTimeFormatter == null)
        {
            dateTimeFormatter = new SimpleDateFormat(DATETIME_FORMAT_FOR_DISPLAY);
        }
        return dateTimeFormatter;
    }

    private static SimpleDateFormat getDateTimeFormatterSameYear()
    {
        if (dateTimeFormatterSameYear == null)
        {
            dateTimeFormatterSameYear = new SimpleDateFormat(DATETIME_FORMAT_FOR_DISPLAY_SAME_YEAR);
        }
        return dateTimeFormatterSameYear;
    }

    private static SimpleDateFormat getDateFormatter()
    {
        if (dateFormatter == null)
        {
            dateFormatter = new SimpleDateFormat(DATE_FORMAT_FOR_DISPLAY);
        }
        return dateFormatter;
    }

    private static SimpleDateFormat getDateFormatterSameYear()
    {
        if (dateFormatterSameYear == null)
        {
            dateFormatterSameYear = new SimpleDateFormat(DATE_FORMAT_FOR_DISPLAY_SAME_YEAR);
        }
        return dateFormatterSameYear;
    }

    private static SimpleDateFormat getTimeFormatter()
    {
        if (timeFormatter == null)
        {
            timeFormatter = new SimpleDateFormat(TIME_FORMAT_FOR_DISPLAY);
        }
        return timeFormatter;
    }

    private static SimpleDateFormat getDateDisplayFormatter()
    {
        if (dateDisplayFormatter == null)
        {
            dateDisplayFormatter = new SimpleDateFormat(BIRTH_DATE_FORMAT_FOR_DISPLAY);
        }
        return dateDisplayFormatter;
    }

    private static SimpleDateFormat getServerDateBirthFormatter()
    {
        if (serverDateBirthFormatter == null)
        {
            serverDateBirthFormatter = new SimpleDateFormat(DATE_BIRTHDAY_FORMAT_DISPLAY);
            serverDateBirthFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return serverDateBirthFormatter;
    }

    private static SimpleDateFormat getDisplayDateBirthFormatter()
    {
        if (displayDateBirthFormatter == null)
        {
            displayDateBirthFormatter = new SimpleDateFormat(BIRTH_DATE_FORMAT_FOR_DISPLAY);
            displayDateBirthFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return displayDateBirthFormatter;
    }

    private static SimpleDateFormat getRailsDateTimeFormatter()
    {
        if (railsDateTimeFormatter == null)
        {
            railsDateTimeFormatter = new SimpleDateFormat(DATETIME_FORMAT_RAILS);
            railsDateTimeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return railsDateTimeFormatter;
    }

    /**
     * CONVERT DATE TO STRING
     */
    public static String getDateTimeForDisplay(Date date)
    {
        SimpleDateFormat format = getDateTimeFormatter();
        if (checkDateInSameCalendarYear(date, new Date()))
        {
            format = getDateTimeFormatterSameYear();
        }
        return format.format(date);
    }

    public static String getDateForTimePickerDisplay(Date date)
    {
        SimpleDateFormat format = getDateFormatter();
        if (checkDateInSameCalendarYear(date, new Date()))
        {
            format = getDateFormatterSameYear();
        }
        return format.format(date);
    }

    public static String getDateDisplay(Date date)
    {
        SimpleDateFormat format = getDateDisplayFormatter();
        return format.format(date);
    }

    public static String getTimeForDisplay(Date date)
    {
        SimpleDateFormat format = getTimeFormatter();
        return format.format(date);
    }

    public static String getDateDisplayForBirthday(Date date) {
        SimpleDateFormat format = getDisplayDateBirthFormatter();
        return format.format(date);
    }

    public static String dateToRailsString(Date date)
    {
        return getRailsDateTimeFormatter().format(date);
    }

    /**
     * CONVERT STRING TO DATE
     */

    public static Date railStringToDate(String string)
    {
        try {
            return getRailsDateTimeFormatter().parse(string);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date birthdayStringToDate(String string)
    {
        try {
            return getServerDateBirthFormatter().parse(string);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * COMPARISONS
     */

    public static boolean checkDateInSameCalendarDay(Date start, Date end)
    {
        Calendar calendarStart = new GregorianCalendar();
        Calendar calendarEnd = new GregorianCalendar();
        calendarStart.setTime(start);
        calendarEnd.setTime(end);
        return (calendarStart.get(Calendar.DAY_OF_MONTH) == calendarEnd.get(Calendar.DAY_OF_MONTH)
                && calendarStart.get(Calendar.MONTH) == calendarEnd.get(Calendar.MONTH)
                && calendarStart.get(Calendar.YEAR) == calendarEnd.get(Calendar.YEAR));
    }

    public static boolean checkDateInSameCalendarMonth(Date start, Date end)
    {
        Calendar calendarStart = new GregorianCalendar();
        Calendar calendarEnd = new GregorianCalendar();
        calendarStart.setTime(start);
        calendarEnd.setTime(end);
        return (calendarStart.get(Calendar.MONTH) == calendarEnd.get(Calendar.MONTH)
                && calendarStart.get(Calendar.YEAR) == calendarEnd.get(Calendar.YEAR));
    }

    public static boolean checkDateInSameCalendarYear(Date start, Date end)
    {
        Calendar calendarStart = new GregorianCalendar();
        Calendar calendarEnd = new GregorianCalendar();
        calendarStart.setTime(start);
        calendarEnd.setTime(end);
        return (calendarStart.get(Calendar.YEAR) == calendarEnd.get(Calendar.YEAR));
    }

    public static JSONObject getRailsJSONOBjectForDateTime(Date date, String prefix)
    {
        if (date == null)
        {
            return new JSONObject();
        }
        JSONObject result = new JSONObject();
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);

        try {
            result.put(prefix + "(1i)", String.valueOf(calendar.get(Calendar.YEAR)));
            result.put(prefix + "(2i)", String.valueOf(calendar.get(Calendar.MONTH) + 1));
            result.put(prefix + "(3i)", String.valueOf(calendar.get(Calendar.DATE)));
            result.put(prefix + "(4i)", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
            result.put(prefix + "(5i)", String.valueOf(calendar.get(Calendar.MINUTE)));
            return result;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    public static JSONObject updateObjectWithRailsJSONOBjectForDateTime(Date date, String prefix, JSONObject result)
    {
        if (date == null)
        {
            return result;
        }
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        try {
            result.put(prefix + "(1i)", String.valueOf(calendar.get(Calendar.YEAR)));
            result.put(prefix + "(2i)", String.valueOf(calendar.get(Calendar.MONTH) + 1));
            result.put(prefix + "(3i)", String.valueOf(calendar.get(Calendar.DATE)));
            result.put(prefix + "(4i)", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
            result.put(prefix + "(5i)", String.valueOf(calendar.get(Calendar.MINUTE)));
            return result;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    public static JSONObject updateObjectWithRailsJSONOBjectForDate(Date date, String prefix, JSONObject result)
    {
        if (date == null)
        {
            return result;
        }
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        try {
            result.put(prefix + "(1i)", String.valueOf(calendar.get(Calendar.YEAR)));
            result.put(prefix + "(2i)", String.valueOf(calendar.get(Calendar.MONTH) + 1));
            result.put(prefix + "(3i)", String.valueOf(calendar.get(Calendar.DATE)));
            return result;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    public static long dateToTimestamp(Date date) {
        long unixtime = date.getTime() / 1000L;
        return unixtime;
    }

    public static Date addDate(Date date, int calendarField, int calendarValue)
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendarField, calendarValue);
        return calendar.getTime();
    }

    public static String getTimeAgo(Date date)
    {
        long timeAgo = date.getTime();
        long currentTime = new Date().getTime();
        if (timeAgo > currentTime)
        {
            timeAgo = currentTime;
        }
        return android.text.format.DateUtils.getRelativeTimeSpanString(timeAgo).toString();
    }

}
