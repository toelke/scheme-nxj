package objects.primproc;

import objects.SchOFixNum;
import objects.SchOPair;
import objects.SchObject;

public class SchOPPMul extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        long result = 1;
        while (!args.istheemptylist()) {
            result *= ((SchOFixNum)((SchOPair)args).car()).value;
            //noinspection AssignmentToMethodParameter
            args = args.cdr();
        }
        return new SchOFixNum(result);
    }
}
