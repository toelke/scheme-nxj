package objects.primproc;

import objects.SchObject;
import utils.Utils;

public class SchOPPApply extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        Utils.endWithError(1, "Apply should never be executed directly!");
        return null;
    }

    @Override
    public boolean is_apply() {
        return true;
    }
}
