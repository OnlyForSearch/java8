import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Java8Stream {


    @Test
    public void testIterator() throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get("Java8Stream.java")), StandardCharsets.UTF_8);

    }

}
