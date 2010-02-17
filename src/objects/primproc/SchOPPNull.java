package objects.primproc;

import objects.SchOPair;
import objects.SchObject;
import objects.primproc.SchOPrimProc;

public class SchOPPNull extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.car().istheemptylist() ? SchObject.strue : SchObject.sfalse;
    }
}
