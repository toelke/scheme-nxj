package interpreter.objects.primproc;

import interpreter.Repl;
import interpreter.objects.SchObject;

public class SchOPPInteractiveEnvironment extends SchOPrimProc {
     @Override
    public SchObject fn(SchObject args) {
        return Repl.get_the_global_environment();
    }
}
