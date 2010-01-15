package utils;

import java.io.IOException;
import java.io.InputStream;

public class MyInputStream extends InputStream {
    private boolean valid;
    private int value;

    private InputStream s;

    public MyInputStream(InputStream s) {
        this.s = s;
    }

    @Override
    public int read() throws IOException {
        if (!valid)
            return s.read();
        else {
            valid = false;
            return value;
        }

    }

    public int peek() throws IOException {
        int c = read();
        unread(c);
        return c;
    }

    public void unread(int c) {
        value = c;
        valid = true;
    }
}
