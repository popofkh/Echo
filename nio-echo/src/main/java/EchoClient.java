import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


public class EchoClient implements Runnable {

    private Selector selector;

    private SocketChannel socketChannel;

    private ByteBuffer byteBuffer;

    public EchoClient(String ip, int port) {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(ip, port));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    @Override
    public void run() {
        System.out.println("echo client started");
        while (true) {
            try {
                selector.select();
                Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
                while (ite.hasNext()) {
                    SelectionKey key = (SelectionKey) ite.next();
                    ite.remove();
                    // 处理可连接的channel
                    if (key.isConnectable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        // 如果正在连接，则完成连接
                        if (channel.isConnectionPending()) {
                            channel.finishConnect();
                        }
                        System.out.println("echo client is connected");
                        // 设置成非阻塞
                        channel.configureBlocking(false);
                        channel.write(ByteBuffer.wrap("this echo client msg,over".getBytes()));
                        channel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {

                        SocketChannel sc = (SocketChannel) key.channel();
                        StringBuilder strb = new StringBuilder(1024);
                        while (sc.read(byteBuffer) > 0) {

                            byteBuffer.flip();
                            byte[] bytes = new byte[byteBuffer.remaining()];
                            byteBuffer.get(bytes);
                            byteBuffer.clear();
                            String msg = new String(bytes);
                            strb.append(msg);
                        }
                        System.out.println("echo client read msg:" + strb.toString());
                        break;
                    }
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
        }
    }
}

