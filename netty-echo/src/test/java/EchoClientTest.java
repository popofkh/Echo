import org.junit.Test;

public class EchoClientTest {

    @Test
    public void test() throws InterruptedException {
        new EchoClient("localhost", 8888).start();
    }
}
