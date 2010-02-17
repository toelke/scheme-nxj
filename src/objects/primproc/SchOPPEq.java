package objects.primproc;

import objects.*;

public class SchOPPEq extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        SchObject obj1, obj2;
        obj1 = args.car();
        obj2 = args.cadr();

        if (obj1.type != obj2.type) return SchObject.sfalse;
        switch (obj1.type) {
            case FIXNUM:
                return ((SchOFixNum)obj1).value == ((SchOFixNum)obj2).value ? SchObject.strue : SchObject.sfalse;
            case CHARACTER:
                return ((SchOCharacter)obj1).value == ((SchOCharacter)obj2).value ? SchObject.strue : SchObject.sfalse;
            case STRING:
                return ((SchOString)obj1).value.equals(((SchOString)obj2).value) ? SchObject.strue : SchObject.sfalse;
            default:
                return obj1 == obj2 ? SchObject.strue : SchObject.sfalse;
        }
    }
}
