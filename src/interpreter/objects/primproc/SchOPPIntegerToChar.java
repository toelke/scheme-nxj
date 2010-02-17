package interpreter.objects.primproc;

import interpreter.objects.SchOCharacter;
import interpreter.objects.SchOFixNum;
import interpreter.objects.SchOPair;
import interpreter.objects.SchObject;

public class SchOPPIntegerToChar extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return new SchOCharacter((char) ((SchOFixNum) ((SchOPair)args).car()).value);
    }
}
