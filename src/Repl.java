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

    SchObject the_global_environment;

    public Repl (InputStream i, OutputStream o) {
        in = new MyInputStream(i);
        out = new PrintStream(o);

        the_global_environment = setup_environment();
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

    private SchObject enclosing_environment(SchObject exp) {
        return ((SchOPair)exp).cdr();
    }

    private SchObject first_frame(SchObject exp) {
        return ((SchOPair)exp).car();
    }

    private SchObject make_frame(SchObject var, SchObject val) {
        return cons(var, val);
    }

    private SchObject frame_variables(SchObject frame) {
        return ((SchOPair)frame).car();
    }

    private SchObject frame_values(SchObject frame) {
        return ((SchOPair)frame).cdr();
    }

    private void add_binding_to_frame(SchObject var, SchObject val, SchObject frame) {
        ((SchOPair)frame).set_car(cons(var, ((SchOPair)frame).car()));
        ((SchOPair)frame).set_cdr(cons(val, ((SchOPair)frame).cdr()));
    }

    private SchObject extend_environment(SchObject vars, SchObject vals, SchObject base) {
        return cons(make_frame(vars, vals), base);
    }

    private SchObject lookup_variable_value(SchObject var, SchObject env) {
        while (!env.istheemptylist()) {
            SchObject frame = first_frame(env);
            SchObject vars = frame_variables(frame);
            SchObject vals = frame_values(frame);
            while(!vars.istheemptylist()) {
                if (var == ((SchOPair)vars).car())
                    return ((SchOPair)vals).car();
                vars = ((SchOPair)vars).cdr();
                vals = ((SchOPair)vals).cdr();
            }
            //noinspection AssignmentToMethodParameter
            env = enclosing_environment(env);
        }
        Utils.endWithError(1, "Unbound variable '" + ((SchOSymbol)var).value + "'\n");
        return null;
    }

    private void set_variable_value(SchObject var, SchObject val, SchObject env) {
        while (!env.istheemptylist()) {
            SchObject frame = first_frame(env);
            SchObject vars = frame_variables(frame);
            SchObject vals = frame_values(frame);
            while(!vars.istheemptylist()) {
                if (var == ((SchOPair)vars).car()) {
                    ((SchOPair)vals).set_car(val);
                    return;
                }
                vars = ((SchOPair)vars).cdr();
                vals = ((SchOPair)vals).cdr();
            }
            //noinspection AssignmentToMethodParameter
            env = enclosing_environment(env);
        }
        Utils.endWithError(1, "Unbound variable '" + ((SchOSymbol)var).value + "'\n");
    }

    private void define_variable(SchObject var, SchObject val, SchObject env) {
        SchObject frame = first_frame(env);
        SchObject vars = frame_variables(frame);
        SchObject vals = frame_values(frame);

        while (!vars.istheemptylist()) {
            if (var == ((SchOPair)vars).car()) {
                ((SchOPair)vals).set_car(val);
                return;
            }
            vars = ((SchOPair)vars).cdr();
            vals = ((SchOPair)vals).cdr();
        }
        add_binding_to_frame(var, val, frame);
    }

    private SchObject setup_environment() {
        return extend_environment(SchObject.theEmptyList, SchObject.theEmptyList, SchObject.the_empty_environment);
    }

    private SchObject assignment_variable (SchObject exp) {
        return exp.cadr();
    }

    private SchObject assignment_value(SchObject exp) {
        return exp.caddr();
    }

    private SchObject definition_variable(SchObject exp) {
        return exp.cadr();
    }

    private SchObject definition_value(SchObject exp) {
        return exp.caddr();
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

    private SchObject eval_assignment(SchObject exp, SchObject env) {
        set_variable_value(assignment_variable(exp), eval(assignment_value(exp), env), env);
        return SchObject.ok_symbol;
    }

    private SchObject eval_definition(SchObject exp, SchObject env) {
        define_variable(definition_variable(exp), eval(definition_value(exp), env), env);
        return SchObject.ok_symbol;
    }

    public SchObject eval(SchObject exp, SchObject env) {
        if (exp.is_self_evaluating()) {
            return exp;
        } else if (exp.isquoted()) {
            return text_of_quotation(exp);
        } else if (exp.isvariable()) {
            return lookup_variable_value(exp, env);
        } else if (exp.is_assignment()) {
            return eval_assignment(exp, env);
        } else if (exp.is_definition()) {
            return eval_definition(exp, env);
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
                r.write(r.eval(r.read(), r.the_global_environment));
                System.out.printf("\n");
            }
        } catch (IOException ignored) {}
    }
}

