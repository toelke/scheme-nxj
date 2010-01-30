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

    public boolean isquoted() {
        return is_tagged_list(SchObject.quote_symbol);
    }

    public SchObject caddr() {
        return ((SchOPair)((SchOPair)((SchOPair) this).cdr()).cdr()).car();
    }

    public SchObject cadr() {
        return ((SchOPair)((SchOPair) this).cdr()).car();
    }

    public enum SchOType {
        BOOLEAN, CHARACTER, STRING, THEEMPTYLIST, PAIR, SYMBOL, FIXNUM
    }

    public SchOType type;
}
