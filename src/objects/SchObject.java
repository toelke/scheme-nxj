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
    public boolean istheemptylist() {
        return type == SchOType.THEEMPTYLIST;
    }

    public enum SchOType {
        BOOLEAN, CHARACTER, STRING, THEEMPTYLIST, FIXNUM
    }

    public SchOType type;
}
