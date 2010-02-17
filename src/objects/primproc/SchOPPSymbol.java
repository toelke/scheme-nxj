package objects.primproc;

import objects.SchOPair;
import objects.SchObject;
import objects.primproc.SchOPrimProc;

public class SchOPPSymbol extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.car().issymbol() ? SchObject.strue : SchObject.sfalse;
    }
}
