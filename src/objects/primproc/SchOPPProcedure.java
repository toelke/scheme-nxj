package objects.primproc;

import objects.SchOPair;
import objects.SchObject;
import objects.primproc.SchOPrimProc;

public class SchOPPProcedure extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        SchObject obj = ((SchOPair) args).car();
        return obj.isprimproc() || obj.iscompproc() ? SchObject.strue : SchObject.sfalse;
    }
}
