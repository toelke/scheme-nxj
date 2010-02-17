package objects.primproc;

import objects.SchOFixNum;
import objects.SchOPair;
import objects.SchObject;

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
