import java.io.IOException;

public class EchoClientTest {
    public static void main(String[] args) throws IOException {
        new EchoClient("localhost", 8080).start();
    }
}
