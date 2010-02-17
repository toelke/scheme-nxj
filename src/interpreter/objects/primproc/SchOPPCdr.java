package interpreter.objects.primproc;

import interpreter.objects.SchObject;

public class SchOPPCdr extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.cdar();
    }
}
