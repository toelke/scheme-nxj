package objects;

public class SchObject {
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

    public boolean is_tagged_list(SchObject tag) {
        if (ispair()) {
            @SuppressWarnings({"ClassReferencesSubclass"}) SchObject the_car = ((SchOPair)this).car();
            return the_car.issymbol() && the_car == tag;
        }
        return false;
    }

    public enum SchOType {
        BOOLEAN, CHARACTER, STRING, THEEMPTYLIST, PAIR, SYMBOL, FIXNUM
    }

    public SchOType type;
}
