import java.io.IOException;

public class EchoServerTest {
    public static void main(String[] args) throws IOException {
        new EchoServer(8080).start();
    }
}
