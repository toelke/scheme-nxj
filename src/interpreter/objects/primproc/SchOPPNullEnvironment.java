package interpreter.objects.primproc;

import interpreter.Repl;
import interpreter.objects.SchObject;

public class SchOPPNullEnvironment extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return Repl.setup_environment();
    }
}
