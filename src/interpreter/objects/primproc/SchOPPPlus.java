package interpreter.objects.primproc;

import interpreter.objects.SchOFixNum;
import interpreter.objects.SchOPair;
import interpreter.objects.SchObject;
import interpreter.objects.primproc.SchOPrimProc;

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
