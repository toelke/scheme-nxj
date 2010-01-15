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

    public enum SchOType {
        BOOLEAN, CHARACTER, FIXNUM
    }

    public SchOType type;
}
