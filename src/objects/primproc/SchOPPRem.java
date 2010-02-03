package objects.primproc;

import objects.SchOFixNum;
import objects.SchOPair;
import objects.SchObject;

public class SchOPPRem extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return new SchOFixNum(((SchOFixNum)((SchOPair)args).car()).value % ((SchOFixNum)((SchOPair)args).cadr()).value);
    }
}
