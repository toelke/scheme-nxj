package interpreter.objects.primproc;

import interpreter.objects.SchObject;
import interpreter.objects.primproc.SchOPrimProc;

public class SchOPPString extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.car().isstring() ? SchObject.strue : SchObject.sfalse;
    }
}
