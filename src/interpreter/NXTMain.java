package interpreter;

import interpreter.utils.Environment;
import interpreter.utils.Utils;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;

@SuppressWarnings({"UtilityClass"})
public class NXTMain {
    private NXTMain() {
    }

    public static void main(String... args) {
        NXTConnection c = USB.waitForConnection();
        InputStream in = c.openInputStream();
        PrintStream out = new PrintStream(c.openOutputStream());
        Repl r = new Repl(in, out);
        Utils.setErr(out);

        try {
            //noinspection InfiniteLoopStatement
            for(;;) {
                out.print("> ");
                r.write(r.eval(r.read(), Environment.the_global_environment));
                out.print("\n");
            }
        } catch (IOException ignored) {}
    }
}