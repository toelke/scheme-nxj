package interpreter.objects.primproc;

import interpreter.objects.SchOPair;
import interpreter.objects.SchOString;
import interpreter.objects.SchOSymbol;
import interpreter.objects.SchObject;

public class SchOPPStringToSymbol extends SchOPrimProc {
    @Override
    public SchObject fn(SchObject args) {
        return SchOSymbol.makeSymbol(((SchOString)((SchOPair)args).car()).value);
    }
}
