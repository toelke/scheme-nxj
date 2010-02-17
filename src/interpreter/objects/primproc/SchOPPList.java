package interpreter.objects.primproc;

import interpreter.objects.SchObject;

public class SchOPPList extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args;
    }
}
