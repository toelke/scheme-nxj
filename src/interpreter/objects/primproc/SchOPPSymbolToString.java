package interpreter.objects.primproc;

import interpreter.objects.SchOPair;
import interpreter.objects.SchOString;
import interpreter.objects.SchOSymbol;
import interpreter.objects.SchObject;

public class SchOPPSymbolToString extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return new SchOString(((SchOSymbol)((SchOPair)args).car()).value);
    }
}
