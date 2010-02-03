package objects.primproc;

import objects.SchOPair;
import objects.SchObject;
import objects.primproc.SchOPrimProc;

public class SchOPPBoolean extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return ((SchOPair)args).car().isboolean() ? SchObject.strue : SchObject.sfalse;
    }
}
