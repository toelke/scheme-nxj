import objects.*;
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
    SchObject theEmptyList;


    public Repl (InputStream i, OutputStream o) {
        in = new MyInputStream(i);
        out = new PrintStream(o);

        sfalse = new SchOBoolean(false);
        strue = new SchOBoolean(true);
        theEmptyList = new SchOTheEmptyList();
    }

    public SchObject read() throws IOException {
        int c;

        Utils.eat_whitespace(in);

        c = in.read();

        if (c == '#') {
            c = in.read();
            switch(c) {
                case 't':
                    return strue;
                case 'f':
                    return sfalse;
                case '\\':
                    return SchOCharacter.readCharacter(in);
                default:
                    System.err.printf("unknown boolean or character literal\n");
                    System.exit(1);
            }
        } else if (Utils.isdigit(c) || c == '-' && Utils.isdigit(in.peek())) {
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
                Utils.endWithError(1,"number not followed by delimiter!\n");
            }
        } else if (c == '"') {
            StringBuilder sb = new StringBuilder();
            while ((c = in.read()) != '"') {
                if (c == '\\') {
                    c = in.read();
                    if (c == 'n') {
                        c = '\n';
                    }
                }
                if (c == -1) {
                    Utils.endWithError(1, "non-terminated string literal\n");
                }
                sb.append((char)c);
            }
            return new SchOString(sb.toString());
        } else if (c == '(') {
            return read_pair();
        } else {
            System.err.printf("Unexpected %c\n", (char)c);
            System.exit(1);
        }
        Utils.endWithError(1, "Read Illegal State!\n");
        return null; // Java is dumb
    }

    private SchObject read_pair() throws IOException {
        Utils.eat_whitespace(in);

        int c = in.read();
        if (c == ')') {
            return theEmptyList;
        }
        in.unread(c);

        SchObject car_obj = read();

        Utils.eat_whitespace(in);

        c = in.read();

        if (c == '.') {
            c = in.peek();
            if (!Utils.isdelimiter(c)) {
                Utils.endWithError(1, "dot not followed by delimiter!");
            }
            SchObject cdr_obj = read();
            Utils.eat_whitespace(in);
            c = in.read();
            if (c != ')') {
                Utils.endWithError(1, "Where is the right paren?");
            }
            return new SchOPair(car_obj, cdr_obj);
        } else {
            in.unread(c);
            SchObject cdr_obj = read_pair();
            return new SchOPair(car_obj, cdr_obj);
        }
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
            case CHARACTER:
                out.printf("#\\");
                char c = ((SchOCharacter) obj).value;
                switch(c) {
                    case '\n':
                        out.printf("newline");
                        break;
                    case ' ':
                        out.printf("space");
                        break;
                    default:
                        out.printf("%c", c);
                        break;
                }
                break;
            case STRING:
                out.printf("\"");
                for (char s: ((SchOString)obj).value.toCharArray()) {
                    switch(s) {
                        case '\n':
                            out.printf("\\n");
                            break;
                        case '\\':
                            out.printf("\\\\");
                            break;
                        case '"':
                            out.printf("\\\"");
                            break;
                        default:
                            out.printf("%c", s);
                            break;
                    }
                }
                out.printf("\"");
                break;
            case THEEMPTYLIST:
                out.printf("()");
                break;
            case PAIR:
                out.print("(");
                write_pair((SchOPair)obj);
                out.print(")");
                break;
            default:
                Utils.endWithError(1, "cannot write unknown type!\n");
                break;
        }
    }

    private void write_pair(SchOPair pair) {
        SchObject car_obj = pair.car();
        SchObject cdr_obj = pair.cdr();
        write (car_obj);
        if (cdr_obj.ispair()) {
            out.print(" ");
            write_pair((SchOPair)cdr_obj);
        } else if (!cdr_obj.istheemptylist()) {
            out.print(" . ");
            write(cdr_obj);
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

