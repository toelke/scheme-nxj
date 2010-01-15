package objects;

public class SchObject {
    public static boolean isfixnum(SchObject obj) {
        return obj.type == SchOType.FIXNUM;
    }

    public static boolean isboolean(SchObject obj) {
        return obj.type == SchOType.BOOLEAN;
    }

    public enum SchOType {
        BOOLEAN, FIXNUM
    }

    public SchOType type;
}
