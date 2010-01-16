package objects;

public class SchOString extends SchObject {
    public String value;

    public SchOString(String v) {
        value = v;
        type = SchOType.STRING;
    }
}
