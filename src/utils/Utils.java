package utils;

@SuppressWarnings({"UtilityClass", "UtilityClassWithoutPrivateConstructor"})
public class Utils {
    public static boolean isdigit(int c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isdelimiter(int c) {
        return Utils.isspace(c) || c == -1 || c == '(' || c == ')' || c == '"' || c == ';' || c == '\n';
    }

    public static boolean isspace(int c) {
        return c == ' ' || c == '\t' || c == '\n';
    }

    public static void endWithError(int i, String s, Object ... args) {
        System.err.printf(s, args);
        System.exit(1);
    }
}
