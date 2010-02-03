package objects.primproc;

import objects.SchOPair;
import objects.SchObject;
import objects.primproc.SchOPrimProc;

public class SchOPPProcedure extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return ((SchOPair)args).car().isprimproc() ? SchObject.strue : SchObject.sfalse;
    }
}
