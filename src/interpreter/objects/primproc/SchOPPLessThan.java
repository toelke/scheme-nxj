package interpreter.objects.primproc;

import interpreter.objects.SchOFixNum;
import interpreter.objects.SchOPair;
import interpreter.objects.SchObject;

public class SchOPPLessThan extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        long next, previous = ((SchOFixNum)((SchOPair)args).car()).value;
        //noinspection AssignmentToMethodParameter
        while (!(args = args.cdr()).istheemptylist()) {
            next = ((SchOFixNum)((SchOPair)args).car()).value;
            if (previous < next) previous = next;
            else return SchObject.sfalse;
        }
        return SchObject.strue;
    }
}
