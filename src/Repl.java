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


    public Repl (InputStream i, OutputStream o) {
        in = new MyInputStream(i);
        out = new PrintStream(o);

    }

    private SchObject cons(SchObject a, SchObject b) {
        return new SchOPair(a, b);
    }

    public SchObject read() throws IOException {
        int c;

        Utils.eat_whitespace(in);

        c = in.read();

        if (c == '#') {
            c = in.read();
            switch(c) {
                case 't':
                    return SchObject.strue;
                case 'f':
                    return SchObject.sfalse;
                case '\\':
                    return SchOCharacter.readCharacter(in);
                default:
                    System.err.printf("unknown boolean or character literal\n");
                    System.exit(1);
            }
        } else if (c == '\'') {
            return cons(SchObject.quote_symbol, cons(read(), SchObject.theEmptyList));
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
        } else if (Utils.isinitial(c) || (c == '+' || c == '-') && Utils.isdelimiter(in.peek())) {
            StringBuilder sb = new StringBuilder();
            while (Utils.isinitial(c) || Utils.isdigit(c) || c == '+' || c == '-') {
                sb.append((char)c);
                c = in.read();
            }
            if (Utils.isdelimiter(c)) {
                in.unread(c);
                return SchOSymbol.makeSymbol(sb.toString());
            } else {
                Utils.endWithError(1, "Symbol does not end with delimiter. Found '" + (char)c + "'\n");
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

    private SchObject text_of_quotation(SchObject exp) {
        return exp.cadr();
    }


    private SchObject read_pair() throws IOException {
        Utils.eat_whitespace(in);

        int c = in.read();
        if (c == ')') {
            return SchObject.theEmptyList;
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

    }

    public SchObject eval(SchObject exp) {
        if (exp.is_self_evaluating()) {
            return exp;
        } else if (exp.isquoted()) {
            return text_of_quotation(exp);
        } else {
            Utils.endWithError(1, "cannot eval unknown expression type\n");
        }
        return null;
    }

    public void write(SchObject obj) {
        switch(obj.type) {
            case FIXNUM:
                out.printf("%d", ((SchOFixNum)obj).value);
                break;
            case BOOLEAN:
                out.printf("#%c", obj.isfalse() ? 'f':'t');
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
            case SYMBOL:
                out.print(((SchOSymbol)obj).value);
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

