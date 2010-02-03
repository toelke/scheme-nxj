package objects.primproc;

import objects.SchOPair;
import objects.SchOString;
import objects.SchOSymbol;
import objects.SchObject;

public class SchOPPStringToSymbol extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return SchOSymbol.makeSymbol(((SchOString)((SchOPair)args).car()).value);
    }
}
