package interpreter.objects.primproc;

import interpreter.objects.SchOFixNum;
import interpreter.objects.SchOPair;
import interpreter.objects.SchObject;

public class SchOPPQuot extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return new SchOFixNum(((SchOFixNum)((SchOPair)args).car()).value / ((SchOFixNum)((SchOPair)args).cadr()).value);
    }
}
