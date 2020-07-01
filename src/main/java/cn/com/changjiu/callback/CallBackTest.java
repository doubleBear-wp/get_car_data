package cn.com.changjiu.callback;

/**
 * @author doubleBear
 * @create 2020 06 30 13:01
 */

/**
 * 回调函数的简单理解，首先客户端需要实现一个服务端要求实现的接口，然后客户端执行一个方法，该方法通过服务端实例调用服务端方法，
 * 服务端的这个接口需要客户端这个实例，这样服务端执行完对数据处理之后就可以通过传入的客户端实例调用回调方法（接口中定义的方法），
 * 从而将数据返回到客户端，这样做的好处就是客户端用一个子线程去调用服务端实例方法，则
 * 可以异步处理，客户端不用等待服务端，并且通过回调方法，客户端能够拿到服务端的处理数据
 * 当然，前提是客户端在执行自己的方法体时，不需要服务端的结果
 */
public class CallBackTest {
    public static void main(String[] args) {

        Server server = new Server();

        Client client = new Client(server);

        client.sendMsg("Server,Hello~");
    }
}
