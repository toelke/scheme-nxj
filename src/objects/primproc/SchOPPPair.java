package objects.primproc;

import objects.SchOPair;
import objects.SchObject;
import objects.primproc.SchOPrimProc;

public class SchOPPPair extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.car().ispair() ? SchObject.strue : SchObject.sfalse;
    }
}
