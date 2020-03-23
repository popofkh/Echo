public class EchoClientTest {
    public static void main(String[] args) {
        EchoClient echoClient = new EchoClient("localhost", 8888);
        echoClient.run();
    }
}
