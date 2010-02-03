package objects.primproc;

import objects.SchOPair;
import objects.SchObject;

public class SchOPPCar extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.caar();
    }
}
