package objects;

public class SchOCompoundProc extends SchObject {
    public SchObject parameters;
    public SchObject body;
    public SchObject env;

    public SchOCompoundProc(SchObject parameters, SchObject body, SchObject env) {
        this.parameters = parameters;
        this.body = body;
        this.env = env;

        type = SchOType.COMPOUND_PROC;
    }
}
