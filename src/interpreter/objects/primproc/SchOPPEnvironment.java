package interpreter.objects.primproc;

import interpreter.Repl;
import interpreter.objects.SchObject;

public class SchOPPEnvironment extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return Repl.make_environment();
    }
}
