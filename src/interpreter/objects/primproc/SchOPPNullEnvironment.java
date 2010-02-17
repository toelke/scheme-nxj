package interpreter.objects.primproc;

import interpreter.objects.SchObject;
import interpreter.utils.Environment;

public class SchOPPNullEnvironment extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return Environment.setup_environment();
    }
}
