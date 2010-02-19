package interpreter.objects;

import interpreter.utils.MyInputStream;
import interpreter.utils.Utils;

import java.io.IOException;

public class SchOCharacter extends SchObject {
    public char value;

    public SchOCharacter(char v) {
        value = v;
        type = SchOType.CHARACTER;
    }

    @SuppressWarnings({"FeatureEnvy", "StaticMethodOnlyUsedInOneClass"})
    public static SchObject readCharacter(MyInputStream in) throws IOException {
        int c = in.read();

        switch(c) {
            case -1:
                Utils.endWithError(1, "incomplete character literal!\n");
                break;
            case 's':
                if (in.peek() == 'p') {
                    SchOCharacter.eatExpectedString(in, "pace");
                    SchOCharacter.peekExpectedDelimiter(in);
                    return new SchOCharacter(' ');
                }
                break;
            case 'n':
                if (in.peek() == 'e') {
                    SchOCharacter.eatExpectedString(in, "ewline");
                    SchOCharacter.peekExpectedDelimiter(in);
                    return new SchOCharacter('\n');
                }
                break;
        }
        SchOCharacter.peekExpectedDelimiter(in);
        return new SchOCharacter((char)c);
    }

    private static void peekExpectedDelimiter(MyInputStream in) throws IOException {
        if (!Utils.isdelimiter(in.peek())) {
            Utils.endWithError(1, "character not followed by delimiter\n");
        }
    }

    private static void eatExpectedString(MyInputStream in, String s) throws IOException {
        for (char c: s.toCharArray()) {
            char r = (char)in.read();
            if (c != r) {
                Utils.endWithError(1, "unexpected character: " + r + "\n");
                System.exit(1);
            }
        }
    }
}
