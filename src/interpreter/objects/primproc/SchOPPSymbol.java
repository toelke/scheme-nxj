package interpreter.objects.primproc;

import interpreter.objects.SchObject;
import interpreter.objects.primproc.SchOPrimProc;

public class SchOPPSymbol extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.car().issymbol() ? SchObject.strue : SchObject.sfalse;
    }
}
