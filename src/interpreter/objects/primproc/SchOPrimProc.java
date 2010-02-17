package interpreter.objects.primproc;

import interpreter.objects.SchObject;

public abstract class SchOPrimProc extends SchObject {
    public SchOPrimProc() {
        type = SchOType.PRIMITIVE_PROC;
    }

    public boolean is_apply() {
        return false;
    }

    public boolean is_eval() {
        return false;
    }

    public abstract SchObject fn(SchObject args);
}
