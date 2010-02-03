package objects.primproc;

import objects.SchObject;

public abstract class SchOPrimProc extends SchObject {
    public SchOPrimProc() {
        type = SchOType.PRIMITIVE_PROC;
    }

    public abstract SchObject fn(SchObject args);
}
