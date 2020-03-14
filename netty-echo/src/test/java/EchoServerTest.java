import org.junit.Test;

public class EchoServerTest {

    @Test
    public void test() throws InterruptedException {
        new EchoServer(8888).start();
    }
}
