package interpreter;

import interpreter.objects.*;
import interpreter.objects.primproc.*;
import interpreter.utils.Environment;
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

    public Repl (InputStream i, OutputStream o) {
        in = new MyInputStream(i);
        out = new PrintStream(o);
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
                    Utils.endWithError(1, "unknown boolean or character literal\n");
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
            Utils.endWithError(1, "Unexpected " + (char)c + "\n");
        }
        Utils.endWithError(1, "Read Illegal State!\n");
        return null; // Java is dumb
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
        Environment.define_variable(exp.definition_variable(), eval(exp.definition_value(), env), env);
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
                env = Environment.extend_environment(cproc.parameters, args, cproc.env);
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
                out.print("" + ((SchOFixNum)obj).value);
                break;
            case BOOLEAN:
                out.print("#" + (obj.isfalse() ? "f":"t"));
                break;
            case CHARACTER:
                out.print("#\\");
                char c = ((SchOCharacter) obj).value;
                switch(c) {
                    case '\n':
                        out.print("newline");
                        break;
                    case ' ':
                        out.print("space");
                        break;
                    default:
                        out.print("" + c);
                        break;
                }
                break;
            case SYMBOL:
                out.print(((SchOSymbol)obj).value);
                break;
            case STRING:
                out.print("\"");
                for (char s: ((SchOString)obj).value.toCharArray()) {
                    switch(s) {
                        case '\n':
                            out.print("\\n");
                            break;
                        case '\\':
                            out.print("\\\\");
                            break;
                        case '"':
                            out.print("\\\"");
                            break;
                        default:
                            out.print("" + s);
                            break;
                    }
                }
                out.print("\"");
                break;
            case THEEMPTYLIST:
                out.print("()");
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
}

