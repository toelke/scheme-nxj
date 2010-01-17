package utils;

import java.io.IOException;

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

    public static void eat_whitespace(MyInputStream in) throws IOException {
        int c;

        while ((c = in.read()) != -1) {
            if (Utils.isspace(c)) {
                continue;
            } else if (c == ';') {
                //noinspection StatementWithEmptyBody
                while ((c = in.read()) != -1 && c != '\n');
                continue;
            }
            in.unread(c);
            break;
        }
    }
}
