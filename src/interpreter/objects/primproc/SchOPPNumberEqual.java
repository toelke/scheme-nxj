package interpreter.objects.primproc;

import interpreter.objects.SchOFixNum;
import interpreter.objects.SchOPair;
import interpreter.objects.SchObject;

public class SchOPPNumberEqual extends SchOPrimProc {
    @SuppressWarnings({"FeatureEnvy"})
    @Override
    public SchObject fn(SchObject args) {
        long value = ((SchOFixNum)((SchOPair)args).car()).value;
        //noinspection AssignmentToMethodParameter
        while (!(args = args.cdr()).istheemptylist()) {
            if (value != ((SchOFixNum)((SchOPair)args).car()).value) return SchObject.sfalse;
        }
        return SchObject.strue;
    }
}
