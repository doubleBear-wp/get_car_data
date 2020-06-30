package cn.com.changjiu.callback;

/**
 * @author doubleBear
 * @create 2020 06 30 13:01
 */
public class CallBackTest {
    public static void main(String[] args) {

        Server server = new Server();

        Client client = new Client(server);

        client.sendMsg("Server,Hello~");
    }
}
