package objects.primproc;

import objects.SchOPair;
import objects.SchObject;
import objects.primproc.SchOPrimProc;

public class SchOPPString extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return ((SchOPair)args).car().isstring() ? SchObject.strue : SchObject.sfalse;
    }
}
