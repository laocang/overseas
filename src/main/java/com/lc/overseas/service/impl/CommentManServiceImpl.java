package com.lc.overseas.service.impl;


import com.lc.overseas.dao.commentmanMapper;
import com.lc.overseas.dao.usersMapper;
import com.lc.overseas.pojo.commentman;
import com.lc.overseas.pojo.users;
import com.lc.overseas.service.ICommentManService;
import com.lc.overseas.service.IUserService;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.ibatis.annotations.Param;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "commentManServiceImpl")
public class CommentManServiceImpl implements ICommentManService {

    @Autowired
    private commentmanMapper commentmanDao;//这里可能会报错，但是并不会影响


    @Override
    public int addRecord(commentman record) {
        return commentmanDao.insert(record);
    }

    @Override
    public List<commentman> getAllCommentMan() {
        return commentmanDao.getAllCommentMan();
    }

    public List<commentman> getCommentMan(Map<String,Object> map) {
        return commentmanDao.getCommentMan(map);
    }

    public int updateHmd(commentman record){return commentmanDao.updateHmd(record);};

    public int requestUrl(int page){
        CloseableHttpClient client = HttpClients.createDefault();
        int result = 0;
        String url="http://cp.waishuitong.com/admin/assessor/index/index.html?page="+page;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
            Element tableEle = document.getElementsByTag("table").get(1);
            Elements trEles = tableEle.getElementsByTag("tr");
            result = trEles.size();
            for (Element trEle : trEles) {
                commentman cm = new commentman();
                Elements tdEles = trEle.getElementsByTag("td");
                String detailUrl = tdEles.get(1).getElementsByTag("a").attr("href");
                cm.setDetail(detailUrl);
                String fbid = tdEles.get(2).text();
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("fbId",fbid);
                List<commentman> cms = commentmanDao.getCommentMan(map);
                if(cms!=null&&cms.size()>0){
                    String gradeTemp = tdEles.get(12).text();
                    if("黑名单".equals(gradeTemp)){
                        commentman t2 = new commentman();
                        t2.setFbid(fbid);
                        t2.setGrade(gradeTemp);
                        commentmanDao.updateHmd(t2);
                    }
                }else{
                    cm.setFbid(fbid);
                    String country = tdEles.get(3).text();
                    cm.setCountry(country);
                    String paypal = tdEles.get(4).getElementsByTag("p").text();
                    cm.setPaypal(paypal);
                    String proLink = tdEles.get(5).getElementsByTag("a").attr("href");
                    cm.setProlink(proLink);
                    String proName = tdEles.get(6).text();
                    cm.setProname(proName);
                    String mainPageNum = tdEles.get(7).text();
                    cm.setMainpagenum(mainPageNum);
                    String countbuy = tdEles.get(8).text();
                    cm.setCountbuy(countbuy!=null?Integer.valueOf(countbuy):0);
                    String liuping = tdEles.get(9).text();
                    cm.setLiuping(liuping!=null?Integer.valueOf(liuping):0);
                    String shouhou = tdEles.get(10).text();
                    cm.setShouhou(shouhou!=null?Integer.valueOf(shouhou):0);
                    String remark = tdEles.get(11).text();
                    cm.setRemark(remark);
                    String grade = tdEles.get(15).text();
                    cm.setGrade(grade);
                    String earlyDate = tdEles.get(16).text();
                    cm.setEarlybuy(earlyDate!=null?sdf.parse(earlyDate):sdf.parse("1970-1-1"));
                    String nearDate = tdEles.get(17).text();
                    cm.setNearbuy(nearDate!=null?sdf.parse(nearDate):sdf.parse("1970-1-1"));
                    commentmanDao.insert(cm);
                }
            }
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
