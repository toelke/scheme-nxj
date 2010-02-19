package interpreter.objects;

import java.util.Hashtable;

@SuppressWarnings({"unchecked", "UseOfObsoleteCollectionType"})
public class SchOSymbol extends SchObject {
    public String value;
    private static Hashtable symbols;

    @SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
    public static SchOSymbol makeSymbol(String v) {
        if (SchOSymbol.symbols == null) SchOSymbol.symbols = new Hashtable();
        if (SchOSymbol.symbols.get(v) != null)
            return (SchOSymbol)SchOSymbol.symbols.get(v);
        return new SchOSymbol(v);
    }

    private SchOSymbol(String v) {
        value = v;
        type = SchOType.SYMBOL;
        SchOSymbol.symbols.put(v, this);
    }

}
