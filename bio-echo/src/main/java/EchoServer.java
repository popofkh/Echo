import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


public class EchoServer {
    private static final String EXIT_MARK = "exit";
    private int port;

    EchoServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        // 创建服务端套接字通信，监听端口，等待客户端链接
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(port));
        System.out.println("服务端已启动，正在监听" + port + "端口");
        SocketChannel channel = ssc.accept();
        System.out.println("接受来自" + channel.getRemoteAddress().toString() + "的请求");

        Scanner sc = new Scanner(System.in);
        while(true) {
            // 等待并接收客户端发送的信息
            String msg = EchoUtils.recvMsg(channel);
            System.out.println("客户端：");
            System.out.println(msg);

            // 输入信息
            System.out.println("请输入：");
            msg = sc.nextLine();
            if (EXIT_MARK.equals(msg)) {
                EchoUtils.sendMsg(channel, "bye~");
                break;
            }
            EchoUtils.sendMsg(channel, msg);
        }

        // 关闭通道
        channel.close();
        ssc.close();
    }
}
