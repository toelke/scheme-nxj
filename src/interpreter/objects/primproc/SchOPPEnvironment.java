package interpreter.objects.primproc;

import interpreter.objects.SchObject;
import interpreter.utils.Environment;

public class SchOPPEnvironment extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return Environment.make_environment();
    }
}
