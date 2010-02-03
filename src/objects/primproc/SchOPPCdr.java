package objects.primproc;

import objects.SchObject;

public class SchOPPCdr extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return args.cdar();
    }
}
