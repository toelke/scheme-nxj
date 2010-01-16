package objects;

public class SchObject {
    public static boolean isfixnum(SchObject obj) {
        return obj.type == SchOType.FIXNUM;
    }

    public static boolean isboolean(SchObject obj) {
        return obj.type == SchOType.BOOLEAN;
    }

    public static boolean ischaracter(SchObject obj) {
        return obj.type == SchOType.CHARACTER;
    }

    public static boolean isstring(SchObject obj) {
        return obj.type == SchOType.STRING;
    }

    public enum SchOType {
        BOOLEAN, CHARACTER, STRING, FIXNUM
    }

    public SchOType type;
}
