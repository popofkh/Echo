import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


public class EchoServer implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    public EchoServer(int port) {
        try {

            selector = Selector.open();

            serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.configureBlocking(false);

            serverSocketChannel.bind(new InetSocketAddress(port));

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            System.exit(0);
        }
    }

    @Override
    public void run() {
        System.out.println("echo server started");
        while (true) {
            try {
                // 获取就绪的Channel
                selector.select();

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keysIter = selectedKeys.iterator();

                while (keysIter.hasNext()) {
                    SelectionKey key = keysIter.next();
                    // 读取完一个要移除
                    keysIter.remove();
                    // 处理连接就绪的channel，注意连接就绪的channel都是服务端的serversocketchannel
                    if (key.isAcceptable()) {
                        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);
                        System.out.println("echo server is connected");

                        sc.register(selector, SelectionKey.OP_READ);
                    }

                    else if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        StringBuilder strb = new StringBuilder(1024);
                        // 将channel中的数据写入ByteBuffer，position会逐渐向后移动
                        while (sc.read(byteBuffer) > 0) {
                            // flip操作重置position为0，才能开始读取，limit置为flip之前的position值，以限制读取的范围
                            byteBuffer.flip();
                            byte[] bytes = new byte[byteBuffer.remaining()];
                            byteBuffer.get(bytes);
                            // position置为0，limit置为容量，以便进行下一次读取
                            byteBuffer.clear();
                            String msg = new String(bytes);
                            System.out.println("echo server read msg:" + msg);
                            strb.append(msg);
                        }
                        // 将收到的消息重新封装成ByteBuffer
                        ByteBuffer writeBuffer = ByteBuffer.wrap(strb.toString().getBytes());
                        System.out.println(writeBuffer.position());
                        System.out.println(writeBuffer.limit());
                        // 写回channel
                        sc.write(writeBuffer);
                    }
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
