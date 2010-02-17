package interpreter;

import interpreter.objects.*;
import interpreter.objects.primproc.*;
import interpreter.utils.MyInputStream;
import interpreter.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

@SuppressWarnings({"OverlyComplexClass"})
public class Repl {
    private MyInputStream in;
    private PrintStream out;

    private static SchObject the_global_environment;

    public Repl (InputStream i, OutputStream o) {
        in = new MyInputStream(i);
        out = new PrintStream(o);

        Repl.the_global_environment = Repl.make_environment();
    }

    public static SchObject make_environment() {
        SchObject env = Repl.setup_environment();

        Repl.define_variable(SchOSymbol.makeSymbol("null?"), new SchOPPNull(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("boolean?"), new SchOPPBoolean(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("string?"), new SchOPPString(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("character?"), new SchOPPChar(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("integer?"), new SchOPPInteger(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("symbol?"), new SchOPPSymbol(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("pair?"), new SchOPPPair(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("procedure?"), new SchOPPProcedure(), env);

        Repl.define_variable(SchOSymbol.makeSymbol("int->char"), new SchOPPIntegerToChar(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("char->int"), new SchOPPCharToInteger(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("symbol->string"), new SchOPPSymbolToString(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("string->symbol"), new SchOPPStringToSymbol(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("string->number"), new SchOPPStringToNumber(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("number->string"), new SchOPPNumberToString(), env);

        Repl.define_variable(SchOSymbol.makeSymbol("+"), new SchOPPPlus(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("-"), new SchOPPSub(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("*"), new SchOPPMul(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("/"), new SchOPPQuot(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("%"), new SchOPPRem(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("="), new SchOPPNumberEqual(), env);
        Repl.define_variable(SchOSymbol.makeSymbol(">"), new SchOPPGreaterThan(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("<"), new SchOPPLessThan(), env);

        Repl.define_variable(SchOSymbol.makeSymbol("cons"), new SchOPPCons(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("car"), new SchOPPCar(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("cdr"), new SchOPPCdr(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("set-car!"), new SchOPPSetCar(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("set-cdr!"), new SchOPPSetCdr(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("list"), new SchOPPList(), env);

        Repl.define_variable(SchOSymbol.makeSymbol("eq?"), new SchOPPEq(), env);

        Repl.define_variable(SchOSymbol.makeSymbol("apply"), new SchOPPApply(), env);

        Repl.define_variable(SchOSymbol.makeSymbol("eval"), new SchOPPEval(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("environment"), new SchOPPEnvironment(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("null-environment"), new SchOPPNullEnvironment(), env);
        Repl.define_variable(SchOSymbol.makeSymbol("interactive-environment"), new SchOPPInteractiveEnvironment(), env);
        return env;
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

    private static void add_binding_to_frame(SchObject var, SchObject val, SchObject frame) {
        ((SchOPair)frame).set_car(var.cons(frame.car()));
        ((SchOPair)frame).set_cdr(val.cons(frame.cdr()));
    }

    private static SchObject extend_environment(SchObject vars, SchObject vals, SchObject base) {
        return vars.make_frame(vals).cons(base);
    }

    private SchObject lookup_variable_value(SchObject var, SchObject env) {
        while (!env.istheemptylist()) {
            SchObject frame = env.first_frame();
            SchObject vars = frame.frame_variables();
            SchObject vals = frame.frame_values();
            while(!vars.istheemptylist()) {
                if (var == vars.car())
                    return vals.car();
                vars = vars.cdr();
                vals = vals.cdr();
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
                if (var == vars.car()) {
                    ((SchOPair)vals).set_car(val);
                    return;
                }
                vars = vars.cdr();
                vals = vals.cdr();
            }
            //noinspection AssignmentToMethodParameter
            env = env.enclosing_environment();
        }
        Utils.endWithError(1, "Unbound variable '" + ((SchOSymbol)var).value + "'\n");
    }

    private static void define_variable(SchObject var, SchObject val, SchObject env) {
        SchObject frame = env.first_frame();
        SchObject vars = frame.frame_variables();
        SchObject vals = frame.frame_values();

        while (!vars.istheemptylist()) {
            if (var == vars.car()) {
                ((SchOPair)vals).set_car(val);
                return;
            }
            vars = vars.cdr();
            vals = vals.cdr();
        }
        Repl.add_binding_to_frame(var, val, frame);
    }

    public static SchObject setup_environment() {
        return Repl.extend_environment(SchObject.theEmptyList, SchObject.theEmptyList, SchObject.the_empty_environment);
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
        Repl.define_variable(exp.definition_variable(), eval(exp.definition_value(), env), env);
        return SchObject.ok_symbol;
    }

    @SuppressWarnings({"AssignmentToMethodParameter"})
    public SchObject eval(SchObject exp, SchObject env) {
        tailcall:
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
        } else if (exp.is_cond()) {
            exp = eval(exp.cond_predicate(), env).istrue() ? exp.cond_consequent() : exp.cond_rest();
        } else if (exp.is_let()) {
            exp = exp.let_to_application();
        } else if (exp.islambda()) {
            return new SchOCompoundProc(exp.lambda_parameters(), exp.lambda_body(), env);
        } else if (exp.isbegin()) {
            exp = exp.begin_actions();
            while (!exp.is_last_exp()) {
                eval(exp.first_exp(), env);
                exp = exp.rest_exp();
            }
            exp = exp.first_exp();
        } else if (exp.is_and()) {
            exp = exp.and_tests();
            if (exp.istheemptylist()) return SchObject.strue;
            while (!exp.is_last_exp()) {
                SchObject result = eval(exp.first_exp(), env);
                if (result.isfalse()) return result;
                exp = exp.rest_exp();
            }
            exp = exp.first_exp();
         } else if (exp.is_or()) {
            exp = exp.or_tests();
            if (exp.istheemptylist()) return SchObject.sfalse;
            while (!exp.is_last_exp()) {
                SchObject result = eval(exp.first_exp(), env);
                if (result.istrue()) return result;
                exp = exp.rest_exp();
            }
            exp = exp.first_exp();
        } else if (exp.is_application()) {
            SchObject proc = eval(exp.operator(), env);
            SchObject args = list_of_values(exp.operands(), env);

            if (proc.isprimproc() && ((SchOPrimProc)proc).is_apply()) {
                proc = args.apply_operator();
                args = args.apply_operands();
            }

            if (proc.isprimproc() && ((SchOPrimProc)proc).is_eval()) {
                exp = args.eval_expression();
                env = args.eval_environment();
                //noinspection UnnecessaryLabelOnContinueStatement
                continue tailcall;
            }

            if (proc.isprimproc()) return ((SchOPrimProc)proc).fn(args);
            else if (proc.iscompproc()) {
                @SuppressWarnings({"ConstantConditions"}) SchOCompoundProc cproc = (SchOCompoundProc) proc;
                env = Repl.extend_environment(cproc.parameters, args, cproc.env);
                exp = cproc.body.make_begin();
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

    private SchObject list_of_values(SchObject exp, SchObject env) {
        if (exp.is_no_operands()) return SchObject.theEmptyList;
        else return eval(exp.first_operand(), env).cons(list_of_values(exp.rest_operands(), env));
    }

    public static void main(String... args) {
        Repl r = new Repl(System.in, System.out);

        try {
            //noinspection InfiniteLoopStatement
            for(;;) {
                System.out.printf("> ");
                r.write(r.eval(r.read(), Repl.the_global_environment));
                System.out.printf("\n");
            }
        } catch (IOException ignored) {}
    }

    public static SchObject get_the_global_environment() {
        return Repl.the_global_environment;
    }
}

