package objects.primproc;

import objects.SchOFixNum;
import objects.SchOPair;
import objects.SchObject;
import objects.primproc.SchOPrimProc;

public class SchOPPPlus extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        long result = 0;
        while (!args.istheemptylist()) {
            result += ((SchOFixNum)((SchOPair)args).car()).value;
            //noinspection AssignmentToMethodParameter
            args = args.cdr();
        }
        return new SchOFixNum(result);
    }
}
