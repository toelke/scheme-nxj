package interpreter.objects;

@SuppressWarnings({"ClassReferencesSubclass", "OverlyComplexClass"})
public class SchObject {
    public static SchObject sfalse = new SchOBoolean(false);
    public static SchObject strue = new SchOBoolean(true);
    public static SchObject quote_symbol = SchOSymbol.makeSymbol("quote");
    public static SchObject define_symbol = SchOSymbol.makeSymbol("define");
    public static SchObject set_symbol = SchOSymbol.makeSymbol("set!");
    public static SchObject ok_symbol = SchOSymbol.makeSymbol("ok");
    public static SchObject theEmptyList = new SchOTheEmptyList();
    public static SchObject the_empty_environment = SchObject.theEmptyList;
    public static SchObject if_symbol = SchOSymbol.makeSymbol("if");
    public static SchObject lambda_symbol = SchOSymbol.makeSymbol("lambda");
    public static SchObject begin_symbol = SchOSymbol.makeSymbol("begin");
    public static SchObject cond_symbol = SchOSymbol.makeSymbol("cond");
    public static SchObject let_symbol = SchOSymbol.makeSymbol("let");
    public static SchObject and_symbol = SchOSymbol.makeSymbol("and");
    public static SchObject or_symbol = SchOSymbol.makeSymbol("or");

    public boolean isfixnum() {
        return type == SchOType.FIXNUM;
    }

    public boolean isboolean() {
        return type == SchOType.BOOLEAN;
    }

    public boolean ischaracter() {
        return type == SchOType.CHARACTER;
    }

    public boolean isstring() {
        return type == SchOType.STRING;
    }

    public boolean istheemptylist() {
        return type == SchOType.THEEMPTYLIST;
    }

    public boolean ispair() {
        return type == SchOType.PAIR;
    }

    public boolean issymbol() {
        return type == SchOType.SYMBOL;
    }

    public boolean isprimproc() {
        return type == SchOType.PRIMITIVE_PROC;
    }

    public boolean iscompproc() {
        return type == SchOType.COMPOUND_PROC;
    }

    public boolean is_self_evaluating() {
        return isboolean() || isfixnum() || ischaracter() || isstring();
    }

    public boolean isvariable() {
        return issymbol();
    }

    public boolean is_tagged_list(SchObject tag) {
        if (ispair()) {
            SchObject the_car = car();
            return the_car.issymbol() && the_car == tag;
        }
        return false;
    }

    public boolean isfalse() {
        return this == SchObject.sfalse;
    }

    public boolean is_assignment() {
        return is_tagged_list(SchObject.set_symbol);
    }

    public boolean is_definition() {
        return is_tagged_list(SchObject.define_symbol);
    }

    public boolean is_if() {
        return is_tagged_list(SchObject.if_symbol);
    }

    public boolean is_and() {
        return is_tagged_list(SchObject.and_symbol);
    }

    public boolean is_or() {
        return is_tagged_list(SchObject.or_symbol);
    }

    public boolean is_cond() {
        return is_tagged_list(SchObject.cond_symbol);
    }

    public boolean is_let() {
        return is_tagged_list(SchObject.let_symbol);
    }

    public boolean isquoted() {
        return is_tagged_list(SchObject.quote_symbol);
    }

    public boolean islambda() {
        return is_tagged_list(SchObject.lambda_symbol);
    }

    public boolean isbegin() {
        return is_tagged_list(SchObject.begin_symbol);
    }

    public SchObject lambda_parameters() {
        return cadr();
    }

    public SchObject lambda_body() {
        return cddr();
    }

    public SchObject begin_actions() {
        return cdr();
    }

    public boolean is_last_exp() {
        return cdr().istheemptylist();
    }

    public SchObject first_exp() {
        return car();
    }

    public SchObject rest_exp() {
        return cdr();
    }

    public SchObject car() {
        //noinspection RedundantCast
        return ((SchOPair)this).car;
    }

    public SchObject cdr() {
        //noinspection RedundantCast
        return ((SchOPair)this).cdr;
    }

    public SchObject caddr() {
        return cdr().cdr().car();
    }

    public SchObject caar() {
        return car().car();
    }

    public SchObject cddr() {
        return cdr().cdr();
    }

    public SchObject cdar() {
        return car().cdr();
    }

    public SchObject cadr() {
        return cdr().car();
    }

    public SchObject cdddr() {
        return cdr().cdr().cdr();
    }

    public SchObject cdadr() {
        return cdr().car().cdr();
    }

    public SchObject caadr() {
        return cdr().car().car();
    }

    public SchObject cadddr() {
        return cdr().cdr().cdr().car();
    }

    public SchObject cadadr() {
        return cdr().car().cdr().car();
    }

    public SchObject definition_value() {
        if (cadr().issymbol()) return caddr();
        else return make_lambda(cdadr(), cddr());
    }

    public SchObject definition_variable() {
        if (cadr().issymbol()) return cadr();
        else return caadr();
    }

    public SchObject assignment_value() {
        return caddr();
    }

    private SchObject make_lambda(SchObject parameters, SchObject body) {
        return SchObject.lambda_symbol.cons(parameters.cons(body));
    }

    public SchObject make_begin() {
        return SchObject.begin_symbol.cons(this);
    }

    public SchObject assignment_variable() {
        return cadr();
    }

    public SchObject text_of_quotation() {
        return cadr();
    }

    public SchObject enclosing_environment() {
        return cdr();
    }

    public SchObject first_frame() {
        return car();
    }

    public SchObject make_frame(SchObject val) {
        return cons(val);
    }

    public SchObject frame_variables() {
        return car();
    }

    public SchObject frame_values() {
        return cdr();
    }

    public SchObject cons(SchObject b) {
        return new SchOPair(this, b);
    }

    public boolean istrue() {
        return !isfalse();
    }

    public SchObject if_alternative() {
        if (cdddr().istheemptylist()) {
            return SchObject.sfalse;
        } else {
            return cadddr();
        }
    }

    public SchObject if_consequent() {
        return caddr();
    }

    public SchObject if_predicate() {
        return cadr();
    }

    public SchObject and_tests() {
        return cdr();
    }

    public SchObject or_tests() {
        return cdr();
    }

    public SchObject cond_predicate() {
        if (cdr().istheemptylist()) return SchObject.strue;
        return caadr();
    }

    public SchObject cond_consequent() {
        if (cdr().istheemptylist()) return SchObject.quote_symbol.cons(SchObject.theEmptyList.cons(SchObject.theEmptyList));
        return cadadr();
    }

    public SchObject cond_rest() {
        return SchObject.cond_symbol.cons(cddr());
    }

    public boolean is_application() {
        return ispair();
    }

    public SchObject operator() {
        return car();
    }

    public SchObject operands() {
        return cdr();
    }

    public boolean is_no_operands() {
        return istheemptylist();
    }

    public SchObject first_operand() {
        return car();
    }

    public SchObject rest_operands() {
        return cdr();
    }

    public SchObject let_to_application() {
        return make_application(make_lambda(let_parameters(), let_body()), let_arguments());
    }

    private SchObject let_arguments() {
        return let_bindings().bindings_arguments();
    }

    private SchObject bindings_arguments() {
        return istheemptylist() ? SchObject.theEmptyList : car().binding_argument().cons(cdr().bindings_arguments());
    }

    private SchObject binding_argument() {
        return cadr();
    }

    private SchObject let_parameters() {
        return let_bindings().bindings_parameters();
    }

    private SchObject bindings_parameters() {
        return istheemptylist() ? SchObject.theEmptyList : car().binding_parameter().cons(cdr().bindings_parameters());
    }

    private SchObject binding_parameter() {
        return car();
    }

    private SchObject let_bindings() {
        return cadr();
    }

    private SchObject let_body() {
        return cddr();
    }

    private SchObject make_application(SchObject operator, SchObject operands) {
        return operator.cons(operands);
    }

    public SchObject apply_operator() {
        return car();
    }

    public SchObject apply_operands() {
        return cdr().prepare_apply_operands();
    }

    private SchObject prepare_apply_operands() {
        if (cdr().istheemptylist()) return car();
        return car().cons(cdr().prepare_apply_operands());
    }

    public SchObject eval_expression() {
        return car();
    }

    public SchObject eval_environment() {
        return cadr();
    }

    public enum SchOType {
        BOOLEAN, CHARACTER, STRING, THEEMPTYLIST, PAIR, SYMBOL, PRIMITIVE_PROC, COMPOUND_PROC, FIXNUM
    }

    public SchOType type;
}
