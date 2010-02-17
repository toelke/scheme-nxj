package interpreter.objects.primproc;

import interpreter.objects.SchObject;
import interpreter.utils.Environment;

public class SchOPPInteractiveEnvironment extends SchOPrimProc {
     @Override
    public SchObject fn(SchObject args) {
        return Environment.get_the_global_environment();
    }
}
