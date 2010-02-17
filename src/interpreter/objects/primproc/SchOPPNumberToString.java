package interpreter.objects.primproc;

import interpreter.objects.SchOFixNum;
import interpreter.objects.SchOPair;
import interpreter.objects.SchOString;
import interpreter.objects.SchObject;

public class SchOPPNumberToString extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return new SchOString("" + ((SchOFixNum)((SchOPair)args).car()).value);
    }
}
