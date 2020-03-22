import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


public class EchoClient {
    private static final String EXIT_MARK = "exit";
    private String hostname;
    private int port;

    EchoClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void start() throws IOException {
        // 打开一个套接字，向服务端发起链接
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress(hostname, port));

        Scanner sc = new Scanner(System.in);
        while(true) {
            // 输入信息
            System.out.println("请输入：");
            String msg = sc.nextLine();
            if(EXIT_MARK.equals(msg)) {
                EchoUtils.sendMsg(channel, "bye~");
                break;
            }
            EchoUtils.sendMsg(channel, msg);

            // 接收响应
            msg = EchoUtils.recvMsg(channel);
            System.out.println("服务端：");
            System.out.println(msg);
        }
        channel.close();
    }
}
