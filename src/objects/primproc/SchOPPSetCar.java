package objects.primproc;

import objects.SchOPair;
import objects.SchObject;

public class SchOPPSetCar extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        ((SchOPair)((SchOPair)args).car()).set_car(args.cadr());
        return SchObject.ok_symbol;
    }
}
