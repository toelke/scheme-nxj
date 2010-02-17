package interpreter.objects;

import java.util.HashMap;

public class SchOSymbol extends SchObject {
    public String value;
    private static HashMap<String, SchOSymbol> symbols = new HashMap<String,  SchOSymbol>();

    @SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
    public static SchOSymbol makeSymbol(String v) {
        if (SchOSymbol.symbols.containsKey(v))
            return SchOSymbol.symbols.get(v);
        return new SchOSymbol(v);
    }

    private SchOSymbol(String v) {
        value = v;
        type = SchOType.SYMBOL;
        SchOSymbol.symbols.put(v, this);
    }

}
