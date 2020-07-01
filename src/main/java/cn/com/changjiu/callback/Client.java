package cn.com.changjiu.callback;

/**
 * @author doubleBear
 * @create 2020 06 30 13:00
 */
public class Client implements CSCallBack {
    private Server server;

    public Client(Server server) {

        this.server = server;

    }

    public void sendMsg(final String msg){//声明为final是为了让（局部）内部类可以使用，否则该方法执行完该变量生命结束，
                                        // 内部类所在线程开始执行的话，会导致内部类获取不到该值，用final修饰为常量
        System.out.println("客户端：发送的消息为：" + msg);


        //这种不通过子线程直接调用的方式为同步回调
//        server.getClientMsg(Client.this,msg);


        //以下方式为异步回调
        new Thread(new Runnable() {

            @Override

            public void run() {

                server.getClientMsg(Client.this,msg);

            }

        }).start();

        System.out.println("客户端：异步发送成功");

    }

    @Override

    public void process(String status) {

        System.out.println("客户端：服务端回调状态为：" + status);

    }
}
