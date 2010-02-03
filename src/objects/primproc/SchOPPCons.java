package objects.primproc;

import objects.SchOPair;
import objects.SchObject;

public class SchOPPCons extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return ((SchOPair)args).car().cons(args.cadr());
    }
}
