package objects.primproc;

import objects.SchOCharacter;
import objects.SchOFixNum;
import objects.SchOPair;
import objects.SchObject;

public class SchOPPIntegerToChar extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return new SchOCharacter((char) ((SchOFixNum) ((SchOPair)args).car()).value);
    }
}
