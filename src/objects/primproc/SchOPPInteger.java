package objects.primproc;

import objects.SchOPair;
import objects.SchObject;
import objects.primproc.SchOPrimProc;

public class SchOPPInteger extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.car().isfixnum() ? SchObject.strue : SchObject.sfalse;
    }
}
