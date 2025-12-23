package g3.hydrantmana.common.utils;

public class StringUtils {

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String join(String delimiter, String... parts) {
        if(parts == null || parts.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for(String part : parts) {
            if(part != null) sb.append(part).append(delimiter);
        }
        if(sb.length() > 0) sb.setLength(sb.length() - delimiter.length());
        return sb.toString();
    }
}
