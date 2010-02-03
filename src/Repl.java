import objects.*;
import objects.primproc.*;
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

        define_variable(SchOSymbol.makeSymbol("null?"), new SchOPPNull(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("boolean?"), new SchOPPBoolean(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("string?"), new SchOPPString(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("character?"), new SchOPPChar(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("integer?"), new SchOPPInteger(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("symbol?"), new SchOPPSymbol(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("pair?"), new SchOPPPair(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("procedure?"), new SchOPPProcedure(), the_global_environment);

        define_variable(SchOSymbol.makeSymbol("int->char"), new SchOPPIntegerToChar(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("char->int"), new SchOPPCharToInteger(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("symbol->string"), new SchOPPSymbolToString(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("string->symbol"), new SchOPPStringToSymbol(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("string->number"), new SchOPPStringToNumber(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("number->string"), new SchOPPNumberToString(), the_global_environment);

        define_variable(SchOSymbol.makeSymbol("+"), new SchOPPPlus(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("-"), new SchOPPSub(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("*"), new SchOPPMul(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("/"), new SchOPPQuot(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("%"), new SchOPPRem(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("="), new SchOPPNumberEqual(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol(">"), new SchOPPGreaterThan(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("<"), new SchOPPLessThan(), the_global_environment);

        define_variable(SchOSymbol.makeSymbol("cons"), new SchOPPCons(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("car"), new SchOPPCar(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("cdr"), new SchOPPCdr(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("set-car!"), new SchOPPSetCar(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("set-cdr!"), new SchOPPSetCdr(), the_global_environment);
        define_variable(SchOSymbol.makeSymbol("list"), new SchOPPList(), the_global_environment);

        define_variable(SchOSymbol.makeSymbol("eq?"), new SchOPPEq(), the_global_environment);
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
            return SchObject.quote_symbol.cons(read().cons(SchObject.theEmptyList));
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

    private void add_binding_to_frame(SchObject var, SchObject val, SchObject frame) {
        ((SchOPair)frame).set_car(var.cons(((SchOPair)frame).car()));
        ((SchOPair)frame).set_cdr(val.cons(((SchOPair)frame).cdr()));
    }

    private SchObject extend_environment(SchObject vars, SchObject vals, SchObject base) {
        return vars.make_frame(vals).cons(base);
    }

    private SchObject lookup_variable_value(SchObject var, SchObject env) {
        while (!env.istheemptylist()) {
            SchObject frame = env.first_frame();
            SchObject vars = frame.frame_variables();
            SchObject vals = frame.frame_values();
            while(!vars.istheemptylist()) {
                if (var == ((SchOPair)vars).car())
                    return ((SchOPair)vals).car();
                vars = ((SchOPair)vars).cdr();
                vals = ((SchOPair)vals).cdr();
            }
            //noinspection AssignmentToMethodParameter
            env = env.enclosing_environment();
        }
        Utils.endWithError(1, "Unbound variable '" + ((SchOSymbol)var).value + "'\n");
        return null;
    }

    private void set_variable_value(SchObject var, SchObject val, SchObject env) {
        while (!env.istheemptylist()) {
            SchObject frame = env.first_frame();
            SchObject vars = frame.frame_variables();
            SchObject vals = frame.frame_values();
            while(!vars.istheemptylist()) {
                if (var == ((SchOPair)vars).car()) {
                    ((SchOPair)vals).set_car(val);
                    return;
                }
                vars = ((SchOPair)vars).cdr();
                vals = ((SchOPair)vals).cdr();
            }
            //noinspection AssignmentToMethodParameter
            env = env.enclosing_environment();
        }
        Utils.endWithError(1, "Unbound variable '" + ((SchOSymbol)var).value + "'\n");
    }

    private void define_variable(SchObject var, SchObject val, SchObject env) {
        SchObject frame = env.first_frame();
        SchObject vars = frame.frame_variables();
        SchObject vals = frame.frame_values();

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
        set_variable_value(exp.assignment_variable(), eval(exp.assignment_value(), env), env);
        return SchObject.ok_symbol;
    }

    private SchObject eval_definition(SchObject exp, SchObject env) {
        define_variable(exp.definition_variable(), eval(exp.definition_value(), env), env);
        return SchObject.ok_symbol;
    }

    @SuppressWarnings({"AssignmentToMethodParameter"})
    public SchObject eval(SchObject exp, SchObject env) {
        for(;;)
        if (exp.is_self_evaluating()) {
            return exp;
        } else if (exp.isquoted()) {
            return exp.text_of_quotation();
        } else if (exp.isvariable()) {
            return lookup_variable_value(exp, env);
        } else if (exp.is_assignment()) {
            return eval_assignment(exp, env);
        } else if (exp.is_definition()) {
            return eval_definition(exp, env);
        } else if (exp.is_if()) {
            exp = eval(exp.if_predicate(), env).istrue() ? exp.if_consequent() : exp.if_alternative();
        } else if (exp.islambda()) {
            return new SchOCompoundProc(exp.lambda_parameters(), exp.lambda_body(), env);
        } else if (exp.is_application()) {
            SchObject proc = eval(exp.operator(), env);
            SchObject args = list_of_values(exp.operands(), env);
            if (proc.isprimproc()) return ((SchOPrimProc)proc).fn(args);
            else if (proc.iscompproc()) {
                SchOCompoundProc cproc = (SchOCompoundProc) proc;
                env = extend_environment(cproc.parameters, args, cproc.env);
                exp = cproc.body;
                while (!exp.is_last_exp()) {
                    eval(exp.first_exp(), env);
                    exp = exp.rest_exp();
                }
                exp = exp.first_exp();
            } else {
                Utils.endWithError(1, "unknown procedure type\n");
            }
        } else {
            Utils.endWithError(1, "cannot eval unknown expression type\n");
        }
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
            case PRIMITIVE_PROC:
            case COMPOUND_PROC:
                out.print("#<procedure>\n");
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

    public SchObject list_of_values(SchObject exp, SchObject env) {
        if (exp.is_no_operands()) return SchObject.theEmptyList;
        else return eval(exp.first_operand(), env).cons(list_of_values(exp.rest_operands(), env));
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

