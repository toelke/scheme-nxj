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
                System.err.printf("incomplete character literal!\n");
                System.exit(1);
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
            System.err.printf("character not followed by delimiter\n");
            System.exit(1);
        }
    }

    private static void eatExpectedString(MyInputStream in, String s) throws IOException {
        for (char c: s.toCharArray()) {
            char r = (char)in.read();
            if (c != r) {
                System.err.printf("unexpected character: %c\n", r);
                System.exit(1);
            }
        }
    }
}
