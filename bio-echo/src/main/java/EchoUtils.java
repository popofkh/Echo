import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class EchoUtils {
    private static final int BUFFER_SIZE = 128;

    public static void sendMsg(SocketChannel channel, String msg) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        // 向buffer写数据
        buffer.put(msg.getBytes());

        buffer.flip();

        //从buffer向channel写数据
        channel.write(buffer);
    }

    public static String recvMsg(SocketChannel channel) throws IOException {
        // 创建buffer
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        // channel向buffer写数据
        channel.read(buffer);

        buffer.flip();

        byte[] bytes = new byte[buffer.limit()];
        //从buffer中读取数据
        buffer.get(bytes);

        return new String(bytes);
    }
}
