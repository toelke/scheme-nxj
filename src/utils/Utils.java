package utils;

@SuppressWarnings({"UtilityClass", "UtilityClassWithoutPrivateConstructor"})
public class Utils {
    public static boolean isdigit(int c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isdelimiter(int c) {
        return Utils.isspace(c) || c == -1 || c == '(' || c == ')' || c == '"';
    }

    public static boolean isspace(int c) {
        return c == ' ' || c == '\t' || c == '\n';
    }
}
