package interpreter;

import interpreter.utils.Environment;

import java.io.IOException;

@SuppressWarnings({"UtilityClass", "FeatureEnvy"})
public class PCMain {
    private PCMain() {
    }

    public static void main(String... args) {
        Repl r = new Repl(System.in, System.out);

        try {
            //noinspection InfiniteLoopStatement
            for(;;) {
                System.out.print("> ");
                r.write(r.eval(r.read(), Environment.the_global_environment));
                System.out.print("\n");
            }
        } catch (IOException ignored) {}
    }
}
