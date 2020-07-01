package cn.com.changjiu.service;



import com.alibaba.fastjson.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author doubleBear
 * @create 2020 06 29 13:49
 */
public class CarDataService {

    ByteArrayInputStream bis = null;
    InputStreamReader isr = null;
    BufferedReader br = null;
    FileWriter fw = null;
    BufferedWriter bw = null;

    public static String getCarData() throws Exception {
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
        boolean flag;//如果是true，表示继续往buffer1中拼接，即属于一个属性对象的所有信息
        List dataOfAttr = new ArrayList();//存放每一个属性的所有数据
        List data = new ArrayList();    //最终的数据
        String temp = null;
        for (Element tr : trs) {//遍历每一个tr
            String id = tr.attr("id");
            if (!"".equals(id)) {//id不为空的tr为表头
                flag = false;
                String text = tr.children().text();
                if(dataOfAttr != null && dataOfAttr.size() != 0){
                    Map mapOfAttr = new HashMap();
                    mapOfAttr.put(temp, dataOfAttr);
                    data.add(mapOfAttr);
                    dataOfAttr = new ArrayList();
                }
                if(!"".equals(text)){
                    temp = text;
                }

            } else {
                flag = true;
            }

            if (flag) {//属于同一个tr表头的所有详细信息
                int nodeSize = tr.children().size();
                for (int i = 0; i < nodeSize; i++) {
                    if (i == nodeSize-2 && "".equals(tr.child(i).text())) {//最后一个表格没有数据直接pass掉
                        break;
                    } else {
                        if (i % 2 != 0) {
                            Map p = new HashMap();
                            String content = tr.child(i).text();
                            content = "".equals(content) ? "无": content;
                            p.put(tr.child(i - 1).text(), content);
                            dataOfAttr.add(p);
                        }
                    }
                }
            }
        }

        //将最后一个属性结点加入data
        Map mapOfAttr = new HashMap();
        mapOfAttr.put(temp, dataOfAttr);
        data.add(mapOfAttr);
        System.out.println(JSON.toJSONString(data));
        return JSON.toJSONString(data);
    }


    public void jsonFile(String json){
        try {
            bis = new ByteArrayInputStream(json.getBytes());
            isr = new InputStreamReader(bis,"utf-8");
            
            br = new BufferedReader(isr);
            fw = new FileWriter("car.txt");
            bw = new BufferedWriter(fw);
            String str = "";
            while((str = br.readLine()) != null){
                bw.write(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bw != null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new CarDataService().jsonFile(getCarData());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
