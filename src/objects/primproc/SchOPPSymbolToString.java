package objects.primproc;

import objects.SchOPair;
import objects.SchOString;
import objects.SchOSymbol;
import objects.SchObject;

public class SchOPPSymbolToString extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return new SchOString(((SchOSymbol)((SchOPair)args).car()).value);
    }
}
