package objects.primproc;

import objects.SchOPair;
import objects.SchObject;

public class SchOPPSetCdr extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        ((SchOPair)((SchOPair)args).car()).set_cdr(args.cadr());
        return SchObject.ok_symbol;
    }
}
