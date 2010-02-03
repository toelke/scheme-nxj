package objects;

@SuppressWarnings({"ClassReferencesSubclass"})
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

    public boolean is_self_evaluating() {
        return isboolean() || isfixnum() || ischaracter() || isstring();
    }

    public boolean isvariable() {
        return issymbol();
    }

    public boolean is_tagged_list(SchObject tag) {
        if (ispair()) {
            SchObject the_car = ((SchOPair)this).car();
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

    public boolean isquoted() {
        return is_tagged_list(SchObject.quote_symbol);
    }

    public SchObject caddr() {
        return ((SchOPair)((SchOPair)((SchOPair) this).cdr()).cdr()).car();
    }

    public SchObject cadr() {
        return ((SchOPair)((SchOPair) this).cdr()).car();
    }

    public SchObject cdddr() {
        return ((SchOPair)((SchOPair)((SchOPair) this).cdr()).cdr()).cdr();
    }

    public SchObject cadddr() {
        return ((SchOPair) ((SchOPair) ((SchOPair) ((SchOPair) this).cdr()).cdr()).cdr()).car();
    }

    public SchObject definition_value() {
        return caddr();
    }

    public SchObject definition_variable() {
        return cadr();
    }

    public SchObject assignment_value() {
        return caddr();
    }

    public SchObject assignment_variable() {
        return cadr();
    }

    public SchObject text_of_quotation() {
        return cadr();
    }

    public SchObject enclosing_environment() {
        return ((SchOPair) this).cdr();
    }

    public SchObject first_frame() {
        return ((SchOPair) this).car();
    }

    public SchObject make_frame(SchObject val) {
        return cons(val);
    }

    public SchObject frame_variables() {
        return ((SchOPair) this).car();
    }

    public SchObject frame_values() {
        return ((SchOPair) this).cdr();
    }

    public SchObject cons(SchObject b) {
        return new SchOPair(this, b);
    }

    public boolean istrue() {
        return !isfalse();
    }

    public SchObject if_alternative() {
        if (cdddr().istheemptylist()) {
            return sfalse;
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

    public boolean is_application() {
        return ispair();
    }

    public SchObject operator() {
        return ((SchOPair)this).car();
    }

    public SchObject operands() {
        return ((SchOPair)this).cdr();
    }

    public boolean is_no_operands() {
        return istheemptylist();
    }

    public SchObject first_operand() {
        return ((SchOPair)this).car();
    }

    public SchObject rest_operands() {
        return ((SchOPair)this).cdr();
    }

    public enum SchOType {
        BOOLEAN, CHARACTER, STRING, THEEMPTYLIST, PAIR, SYMBOL, PRIMITIVE_PROC, FIXNUM
    }

    public SchOType type;
}
