package interpreter.objects.primproc;

import interpreter.objects.SchObject;
import interpreter.objects.primproc.SchOPrimProc;

public class SchOPPInteger extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.car().isfixnum() ? SchObject.strue : SchObject.sfalse;
    }
}
