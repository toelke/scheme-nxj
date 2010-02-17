package interpreter.objects.primproc;

import interpreter.objects.SchObject;
import interpreter.utils.Utils;

public class SchOPPEval extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        Utils.endWithError(1, "Eval should never be executed directly!");
        return null;
    }

    @Override
    public boolean is_eval() {
        return true;
    }
}
