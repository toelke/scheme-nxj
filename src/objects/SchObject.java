package objects;

public class SchObject {
    public boolean isfixnum() {
        return type == SchOType.FIXNUM;
    }

    public boolean isboolean() {
        return type == SchOType.BOOLEAN;
    }

    public boolean ischaracter() {
        return type == SchOType.CHARACTER;
    }

    public boolean isstring() {
        return type == SchOType.STRING;
    }

    public enum SchOType {
        BOOLEAN, CHARACTER, STRING, FIXNUM
    }

    public SchOType type;
}
