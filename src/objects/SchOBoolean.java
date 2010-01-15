package objects;

public class SchOBoolean extends SchObject {
    public boolean value;

    public SchOBoolean(boolean v) {
        value = v;
        type = SchOType.BOOLEAN;
    }
}
