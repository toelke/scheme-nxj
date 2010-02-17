package objects;

public class SchOPair extends SchObject {
    public SchObject car, cdr;

    public SchOPair(SchObject a, SchObject d) {
        car = a;
        cdr = d;
        type = SchOType.PAIR;
    }

    public void set_car(SchObject a) {
        car = a;
    }

    public void set_cdr(SchObject d) {
        cdr = d;
    }
}
