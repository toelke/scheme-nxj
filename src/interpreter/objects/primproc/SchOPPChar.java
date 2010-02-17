package interpreter.objects.primproc;

import interpreter.objects.SchObject;
import interpreter.objects.primproc.SchOPrimProc;

public class SchOPPChar extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.car().ischaracter() ? SchObject.strue : SchObject.sfalse;
    }
}
