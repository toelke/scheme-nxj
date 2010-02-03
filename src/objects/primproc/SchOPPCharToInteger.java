package objects.primproc;

import objects.SchOCharacter;
import objects.SchOFixNum;
import objects.SchOPair;
import objects.SchObject;

public class SchOPPCharToInteger extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return new SchOFixNum(((SchOCharacter)((SchOPair)args).car()).value);
    }
}
