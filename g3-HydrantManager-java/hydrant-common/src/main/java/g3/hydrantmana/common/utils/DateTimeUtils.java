package g3.hydrantmana.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DEFAULT_PATTERN);
    }

    public static String format(LocalDateTime dateTime, String pattern) {
        if(dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parse(String text) {
        return parse(text, DEFAULT_PATTERN);
    }

    public static LocalDateTime parse(String text, String pattern) {
        if(text == null || text.isEmpty()) return null;
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern));
    }
}
