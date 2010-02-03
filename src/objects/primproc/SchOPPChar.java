package objects.primproc;

import objects.SchOPair;
import objects.SchObject;
import objects.primproc.SchOPrimProc;

public class SchOPPChar extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return ((SchOPair)args).car().ischaracter() ? SchObject.strue : SchObject.sfalse;
    }
}
