package interpreter.utils;

import interpreter.objects.SchOPair;
import interpreter.objects.SchOSymbol;
import interpreter.objects.SchObject;
import interpreter.objects.primproc.*;

@SuppressWarnings({"UtilityClass", "UtilityClassWithoutPrivateConstructor"})
public class Environment {
    public static SchObject the_global_environment;

    static {
        Environment.the_global_environment = Environment.make_environment();
    }

    public static SchObject make_environment() {
        SchObject env = Environment.setup_environment();

        Environment.define_variable(SchOSymbol.makeSymbol("null?"), new SchOPPNull(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("boolean?"), new SchOPPBoolean(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("string?"), new SchOPPString(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("character?"), new SchOPPChar(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("integer?"), new SchOPPInteger(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("symbol?"), new SchOPPSymbol(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("pair?"), new SchOPPPair(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("procedure?"), new SchOPPProcedure(), env);

        Environment.define_variable(SchOSymbol.makeSymbol("int->char"), new SchOPPIntegerToChar(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("char->int"), new SchOPPCharToInteger(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("symbol->string"), new SchOPPSymbolToString(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("string->symbol"), new SchOPPStringToSymbol(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("string->number"), new SchOPPStringToNumber(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("number->string"), new SchOPPNumberToString(), env);

        Environment.define_variable(SchOSymbol.makeSymbol("+"), new SchOPPPlus(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("-"), new SchOPPSub(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("*"), new SchOPPMul(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("/"), new SchOPPQuot(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("%"), new SchOPPRem(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("="), new SchOPPNumberEqual(), env);
        Environment.define_variable(SchOSymbol.makeSymbol(">"), new SchOPPGreaterThan(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("<"), new SchOPPLessThan(), env);

        Environment.define_variable(SchOSymbol.makeSymbol("cons"), new SchOPPCons(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("car"), new SchOPPCar(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("cdr"), new SchOPPCdr(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("set-car!"), new SchOPPSetCar(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("set-cdr!"), new SchOPPSetCdr(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("list"), new SchOPPList(), env);

        Environment.define_variable(SchOSymbol.makeSymbol("eq?"), new SchOPPEq(), env);

        Environment.define_variable(SchOSymbol.makeSymbol("apply"), new SchOPPApply(), env);

        Environment.define_variable(SchOSymbol.makeSymbol("eval"), new SchOPPEval(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("environment"), new SchOPPEnvironment(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("null-environment"), new SchOPPNullEnvironment(), env);
        Environment.define_variable(SchOSymbol.makeSymbol("interactive-environment"), new SchOPPInteractiveEnvironment(), env);
        return env;
    }

    public static void add_binding_to_frame(SchObject var, SchObject val, SchObject frame) {
        ((SchOPair)frame).set_car(var.cons(frame.car()));
        ((SchOPair)frame).set_cdr(val.cons(frame.cdr()));
    }

    public static SchObject extend_environment(SchObject vars, SchObject vals, SchObject base) {
        return vars.make_frame(vals).cons(base);
    }

    public static void define_variable(SchObject var, SchObject val, SchObject env) {
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
        Environment.add_binding_to_frame(var, val, frame);
    }

    public static SchObject setup_environment() {
        return Environment.extend_environment(SchObject.theEmptyList, SchObject.theEmptyList, SchObject.the_empty_environment);
    }

    @SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
    public static SchObject get_the_global_environment() {
        return Environment.the_global_environment;
    }
}
