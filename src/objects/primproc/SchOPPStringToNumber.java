package objects.primproc;

import objects.SchOFixNum;
import objects.SchOPair;
import objects.SchOString;
import objects.SchObject;

public class SchOPPStringToNumber extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return new SchOFixNum(Long.parseLong(((SchOString)((SchOPair)args).car()).value));
    }
}
