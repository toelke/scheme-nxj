package interpreter.objects.primproc;

import interpreter.objects.SchObject;
import interpreter.objects.primproc.SchOPrimProc;

public class SchOPPBoolean extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.car().isboolean() ? SchObject.strue : SchObject.sfalse;
    }
}
