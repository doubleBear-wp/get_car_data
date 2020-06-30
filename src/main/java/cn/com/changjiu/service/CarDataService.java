package cn.com.changjiu.service;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author doubleBear
 * @create 2020 06 29 13:49
 */
public class CarDataService {
    public static void getCarData() throws Exception {
        String url = "http://www.haicj.com/carinfo.jsp?clxh=HMC7168E5S0&typeid=2";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();//默认是get请求
        String result = client.newCall(request).execute().body().string();
        System.out.println("http get rsp:" + result);

        Document document = Jsoup.parse(result);//获取html文档
        String imageUUU = document.getElementById("carInfoBox").html();//获取元素节点等
        System.out.println("https:" + imageUUU);

        Document carinfo = Jsoup.parse(imageUUU);
        Elements trs = carinfo.getElementsByTag("tr");//获取所有的tr
        StringBuffer buffer = new StringBuffer();//最终的json字符串
        buffer.append("{");
        boolean flag;//如果是true，表示继续往buffer1中拼接，即属于一个属性对象的所有信息
        StringBuffer buffer1 = new StringBuffer();//用来保存每个属性对象信息
        for (Element tr : trs) {//遍历每一个tr
            String id = tr.attr("id");
            if (!"".equals(id)) {//id不为空的tr为表头
                flag = false;
                if (buffer1 != null && buffer1.length() != 0) {
                    buffer1.deleteCharAt(buffer1.length() - 1);
                    buffer1.append("}");
                    buffer1.insert(0, "{");
                    buffer.append(buffer1.toString() + ",");
                    buffer1 = new StringBuffer();
                }
                String text = tr.children().text();
                buffer.append(text);
                buffer.append(":");
            } else {
                flag = true;
            }

            if (flag) {//属于同一个tr表头的所有详细信息
                for (int i = 0; i < tr.children().size(); i++) {
                    if(i == 2 && "".equals(tr.child(i).text())){//最后一个表格没有数据直接pass掉
                        break;
                    }else{
                        String text = tr.child(i).text();
                        if ("".equals(text)) {
                            buffer1.append("无");
                        } else {
                            buffer1.append(text);
                        }
                        String id1 = tr.child(i).attr("id");
                        if ("".equals(id1)) {
                            buffer1.append(":");
                        } else {
                            buffer1.append(",");
                        }
                    }
                }
            }
        }
        buffer.append("{");
        buffer.append(buffer1.toString());
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append("}}");

        System.out.println(buffer.toString());
    }

    public static void main(String[] args) {
        try {
            getCarData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
