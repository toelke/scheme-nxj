package interpreter.objects.primproc;

import interpreter.objects.SchObject;
import interpreter.objects.primproc.SchOPrimProc;

public class SchOPPNull extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.car().istheemptylist() ? SchObject.strue : SchObject.sfalse;
    }
}
