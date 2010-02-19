package interpreter.utils;

import java.io.IOException;

@SuppressWarnings({"UtilityClass", "UtilityClassWithoutPrivateConstructor", "StaticMethodOnlyUsedInOneClass"})
public class Utils {
    public static boolean isdigit(int c) {
        return Character.isDigit((char)c);
    }

    public static boolean isdelimiter(int c) {
        return Utils.isspace(c) || c == -1 || c == '(' || c == ')' || c == '"' || c == ';' || c == '\n';
    }

    public static boolean isspace(int c) {
        return (char) c == ' ' || (char) c == '\t' || c == '\n';
    }

    public static boolean isinitial(int c) {
        return  (char) c >= 'a' && (char) c <= 'z' || (char) c >= 'A' && (char) c <= 'Z' || c == '*' || c == '/' || c == '>' || c == '<' || c == '=' || c == '?' || c == '!';
    }

    public static void endWithError(int i, String s) {
        System.err.print(s);
        System.exit(i);
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
