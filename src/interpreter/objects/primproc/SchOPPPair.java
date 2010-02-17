package interpreter.objects.primproc;

import interpreter.objects.SchObject;
import interpreter.objects.primproc.SchOPrimProc;

public class SchOPPPair extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.car().ispair() ? SchObject.strue : SchObject.sfalse;
    }
}
