package interpreter.objects.primproc;

import interpreter.objects.SchObject;

public class SchOPPCons extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.car().cons(args.cadr());
    }
}
