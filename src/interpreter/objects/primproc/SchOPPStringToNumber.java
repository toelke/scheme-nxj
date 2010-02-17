package interpreter.objects.primproc;

import interpreter.objects.SchOFixNum;
import interpreter.objects.SchOPair;
import interpreter.objects.SchOString;
import interpreter.objects.SchObject;

public class SchOPPStringToNumber extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return new SchOFixNum(Long.parseLong(((SchOString)((SchOPair)args).car()).value));
    }
}
