package interpreter.objects.primproc;

import interpreter.objects.SchOCharacter;
import interpreter.objects.SchOFixNum;
import interpreter.objects.SchOPair;
import interpreter.objects.SchObject;

public class SchOPPCharToInteger extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return new SchOFixNum(((SchOCharacter)((SchOPair)args).car()).value);
    }
}
