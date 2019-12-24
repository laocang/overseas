package com.lc.overseas.Controller;


import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lc.overseas.pojo.commentman;
import com.lc.overseas.pojo.commentman_order;
import com.lc.overseas.service.ICommentManService;
import com.lc.overseas.service.IOrderService;
import com.lc.overseas.service.IUserService;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("commentMan")
@Controller
public class ZhComController {

    @Autowired
    private ICommentManService commentManServiceImpl;
    @Autowired
    private IOrderService orderServiceImpl;
    ExecutorService pool = null;

    @RequestMapping("index")
    public void index() {
        String https = "http://cp.waishuitong.com/admin/assessor/index.html?page=1";
        long beginTimeAll = new Date().getTime();
        // 线程数
        int threadNum = 4;//预设线程数10，具体看运行服务的服务器cpu多任务能力
        // 总数据条数
        int dataSize = 98;
        // 每条线程处理的数据量
        int threadSize = 0;
        if(dataSize%threadNum == 0){
            threadSize = dataSize/threadNum;
        } else{
            threadSize = dataSize/(threadNum-1);
        }
        pool = Executors.newFixedThreadPool(threadNum);
        ThreadPoolDemo task = null;
        // 确定每条线程的数据
        for (int i = 0; i < threadNum; i++) {
            int start;
            int end;
            if (i == threadNum - 1) {
                start = threadSize * i+1;
                end = dataSize;
            } else {
                start = threadSize * i+1;
                end = threadSize * (i + 1);
            }
            task = new ThreadPoolDemo(start,end);
            pool.submit(task);
        }
        long endTimeAll = new Date().getTime();
        pool.shutdown();
       /* for(int i = 1;i<99999;i++){
            int result = requestUrl(i);
            System.out.print("成功获取"+result+"条!/n\n");
            if(result==0){
                break;
            }

        }
        System.out.print("获取结束");*/
    }


    @RequestMapping("getOrder")
    public void getOrder(){
        List<commentman> cms = commentManServiceImpl.getAllCommentMan();
        if(cms!=null&&cms.size()>0){
            for(commentman cmt:cms){
                CloseableHttpClient client = HttpClients.createDefault();
                int result = 0;
                String url = cmt.getDetail();
                if(url!=null&&!"".equals(url)){
                    String id = url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));
                    System.out.print("正在获取"+cmt.getFbid()+"的订单！\n");
                    if ("More Items".equals(cmt.getFbid())){
                        continue;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    HttpGet get=new HttpGet(url);
                    try {
                        BasicCookieStore cookieStore = new BasicCookieStore();
                        BasicClientCookie cookie = new BasicClientCookie("PHPSESSID", "t1st6ovujplmdp9l86tj7h0f5a");
                        cookie.setDomain(".cp.waishuitong.com");
                        cookie.setPath("/");
                        cookieStore.addCookie(cookie);
                        get.addHeader("Cookie", "PHPSESSID=t1st6ovujplmdp9l86tj7h0f5a");
                        get.addHeader("User-Agent","PostmanRuntime/7.18.0");
                        get.addHeader("Accept","*/*");
                        get.addHeader("Cache-Control","no-cache");
                        get.addHeader("Postman-Token","822b48b4-6e95-4e9e-9c19-6b63da74aaab");
                        get.addHeader("Host","cp.waishuitong.com");
                        get.addHeader("Accept-Encoding","gzip, deflate");
                        get.addHeader("Connection","keep-alive");
                        CloseableHttpResponse execute = client.execute(get);
                        HttpEntity entity = execute.getEntity();
                        InputStream in = entity.getContent();
                        StringBuilder builder=new StringBuilder();
                        BufferedReader bufreader =new BufferedReader(new InputStreamReader(in));
                        for (String temp=bufreader.readLine();temp!=null;temp= bufreader.readLine()) {
                            builder.append(temp);
                        }
                        Document document = Jsoup.parse(builder.toString());
                        if(document.getElementsByTag("table").size()>1){
                        Element tableEle = document.getElementsByTag("table").get(1);
                        Elements trEles = tableEle.getElementsByTag("tr");
                        System.out.print(cmt.getFbid()+"共有"+trEles.size()+"条订单！\n");
                        for(int i = 1;i<trEles.size();i++){
                            System.out.print("正在获取第"+i+"条的订单！\n");
                            commentman_order order = new commentman_order();
                            order.setId(id);
                            Elements tdEles = trEles.get(i).getElementsByTag("td");
                            String ordertime = tdEles.get(0).text();
                            order.setOrdertime(ordertime!=null?sdf.parse(ordertime):sdf.parse("1970-1-1 0:0:0"));
                            String zhuangyuan = tdEles.get(1).text();
                            order.setZhuanyuan(zhuangyuan);
                            String mainpage = tdEles.get(2).text();
                            order.setMainpage(mainpage);
                            String customno = tdEles.get(3).text();
                            order.setCustomnum(customno);
                            String product = tdEles.get(4).text();
                            order.setProduct(product);
                            String shop = tdEles.get(5).text();
                            order.setShop(shop);
                            String price = tdEles.get(6).text();
                            String orderno = tdEles.get(7).text();
                            order.setOrderno(orderno);
                            String orderremark = tdEles.get(8).text();
                            order.setOrderremark(orderremark);
                           /* String paypalaccount = tdEles.get(8).text();
                            order.setPaypalacount(paypalaccount);*/
                            String isliuping = tdEles.get(9).text();
                            order.setIsliuping(isliuping);
                            String blacklist = tdEles.get(15).text();
                            order.setBlacklist(blacklist);
                            orderServiceImpl.addRecord(order);
                            System.out.print("第"+i+"条的订单获取完成！\n");
                            }
                        }
                    } catch (ClientProtocolException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
