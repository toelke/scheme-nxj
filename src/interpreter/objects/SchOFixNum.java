package interpreter.objects;

import interpreter.objects.SchObject;

public class SchOFixNum extends SchObject {
    public long value;

    public SchOFixNum(long v) {
        value = v;
        type = SchOType.FIXNUM;
    }
}
