package interpreter.objects.primproc;

import interpreter.objects.SchOFixNum;
import interpreter.objects.SchOPair;
import interpreter.objects.SchObject;

public class SchOPPSub extends SchOPrimProc {
    @SuppressWarnings({"FeatureEnvy"})
    @Override
    public SchObject fn(SchObject args) {
        long result = ((SchOFixNum)((SchOPair)args).car()).value;
        //noinspection AssignmentToMethodParameter
        while (!(args = args.cdr()).istheemptylist()) {
            result -= ((SchOFixNum)((SchOPair)args).car()).value;
        }
        return new SchOFixNum(result);
    }
}
