package interpreter.objects.primproc;

import interpreter.objects.SchObject;
import interpreter.objects.primproc.SchOPrimProc;

public class SchOPPProcedure extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        SchObject obj = args.car();
        return obj.isprimproc() || obj.iscompproc() ? SchObject.strue : SchObject.sfalse;
    }
}
