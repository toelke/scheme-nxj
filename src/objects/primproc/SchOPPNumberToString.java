package objects.primproc;

import objects.SchOFixNum;
import objects.SchOPair;
import objects.SchOString;
import objects.SchObject;

public class SchOPPNumberToString extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return new SchOString("" + ((SchOFixNum)((SchOPair)args).car()).value);
    }
}
