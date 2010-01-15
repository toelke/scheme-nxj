import objects.SchOBoolean;
import objects.SchOFixNum;
import objects.SchObject;
import utils.MyInputStream;
import utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class Repl {
    MyInputStream in;
    PrintStream out;
    SchObject sfalse;
    SchObject strue;


    public Repl (InputStream i, OutputStream o) {
        in = new MyInputStream(i);
        out = new PrintStream(o);

        sfalse = new SchOBoolean(false);
        strue = new SchOBoolean(true);
    }

    public SchObject read() throws IOException {
        int c;

        while ((c = in.read()) != -1) {
            if (Utils.isspace(c)) {
                continue;
            } else if (c == '#') {
                c = in.read();
                switch(c) {
                    case 't':
                        return strue;
                    case 'f':
                        return sfalse;
                    default:
                        System.err.printf("unknown boolean literal\n");
                        System.exit(1);
                }
            } else if (Utils.isdigit(c) || c == '-') {
                short sign = 1;
                long num = 0;
                if (c == '-')
                    sign = -1;
                else
                    in.unread(c);

                while(Utils.isdigit(c = in.read()))
                    num = num * 10 + (c - '0');

                num *= sign;
                if (Utils.isdelimiter(c)) {
                    in.unread(c);
                    return new SchOFixNum(num);
                } else {
                    System.err.printf("number not followed by delimiter!\n");
                    System.exit(1);
                }
            } else {
                System.err.printf("Unexpected %c\n", (char)c);
                System.exit(1);
            }
        }
        System.err.printf("Read Illegal State!\n");
        System.exit(1);
        return null; // Java is dumb
    }

    private boolean isfalse(SchObject obj) {
        return obj == sfalse;
    }

    public SchObject eval(SchObject exp) {
        return exp;
    }

    public void write(SchObject obj) {
        switch(obj.type) {
            case FIXNUM:
                out.printf("%d", ((SchOFixNum)obj).value);
                break;
            case BOOLEAN:
                out.printf("#%c", isfalse(obj) ? 'f':'t');
                break;
            default:
                System.err.printf("cannot write unknown type!\n");
                System.exit(1);
                break;
        }
    }

    public static void main(String... args) {
        Repl r = new Repl(System.in, System.out);

        try {
            //noinspection InfiniteLoopStatement
            for(;;) {
                System.out.printf("> ");
                r.write(r.eval(r.read()));
                System.out.printf("\n");
            }
        } catch (IOException ignored) {}
    }
}

