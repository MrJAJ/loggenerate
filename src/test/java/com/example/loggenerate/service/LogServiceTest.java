package com.example.loggenerate.service;

import com.example.loggenerate.dao.LogDao;
import com.example.loggenerate.entity.ism_data_transfer_log;
import com.example.loggenerate.spell.LCSMap;
import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.analysis.*;
import org.apdplat.word.segmentation.Segmentation;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;
import org.apdplat.word.util.AtomicFloat;
import org.apdplat.word.vector.Word2Vector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import sun.nio.cs.ext.MacArabic;
import sun.plugin.util.PluginSysUtil;

import javax.management.DescriptorRead;
import javax.swing.*;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.loggenerate.service.LogService.readFileByLines;
import static org.junit.Assert.*;
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class LogServiceTest {
    private static final Logger logger = LoggerFactory
            .getLogger(LogServiceTest.class);
    @Autowired
    private LogDao logDao;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    @Test
    public void logGenerateByDataBase() {
        BufferedWriter out=null;
        try {
            List<ism_data_transfer_log> spList = logDao.getAllEntity("2019-05-09 00:00:00", "2019-05-09 23:59:59");
            FileOutputStream outfile = new FileOutputStream("D:\\logs\\apilogs\\2019-05-09.log",true); // 相对路径，如果没有则要建立一个新的output。txt文件
            out = new BufferedWriter(new OutputStreamWriter(outfile, "utf-8"));
            if(spList!=null) {
                for (ism_data_transfer_log log : spList) {
//                    String s="";
//                    String detail=log.process_api+" "+" "+log.process_log.split("#")[0].replace("\r\n","").replace(" ","")+" "+log.data_quantity+" "+log.create_by+" "+log.type_code;
//                    String time=sdf.format(log.process_time);
//                    if(log.type_code.equals("success")) {
//                        s=time+" "+"INFO 256668  --- [http-nio-8081-exec-9] "+log.process_api+"   : "+detail;
//                    }else if(log.type_code.equals("failure")){
//                        s=time+" "+"ERROR 256668  --- [http-nio-8081-exec-9] "+log.process_api+"   : "+detail;
//                    }
                    out.write(log.process_log.split("#")[0].replace("\r\n","").replace(" ","")+"\r\n");
                }
            }else {
                out.write("无可用数据");
            }
        }catch (Exception ex){
            logger.info(""+ex.getCause());
        }
    }
    @Test
    public void logGenerateByFile(){
//        String basePath="D:\\\\logs\\\\logs\\\\";
//        String[] list=new File(basePath).list();
//        for(int i=0;i<list.length;i++) {
//            String filename="D:\\logs\\logs\\"+list[i];
//            readFileByLines(filename);
//            System.out.println("文件"+filename+"已读取完毕！");
//        }
        readFileByLines("D:\\logs\\apilogs\\ism.log.2019-06-08.0.log");
    }
    @Test
    public void t(){
            Jedis jedis = new Jedis("192.168.1.106", 6379);
            jedis.auth("61084");
//            Set<String> clusters=jedis.smembers("Clusters");
//            int sum=0;
//            for(String c:clusters){
//                jedis.hdel("ClusterNum",c);
//                jedis.hdel("ClusterCate",c);
//                Set<String> s=jedis.hkeys(c);
//                for(String t:s){
//                    jedis.hdel(c,t);
//                }
//            }

        Set<String> clusters=jedis.zrange("ClusterRank20190608",0,-1);
        for(String c:clusters){
            jedis.zincrby("ClusterRank:20190608",jedis.zscore("ClusterRank20190608",c),c);
        }
    }
    @Test
    public void testSpell(){
        LCSMap map = new LCSMap();
        FileInputStream fr;
        BufferedReader br;
        BufferedWriter out=null;
        String[] s=new String[]{"98809609262727168，","99558628322705408，","97638799831465984，","98365864436301824，"};

        try {
            fr = new FileInputStream("D:\\logs\\apilogs\\ism.log.2019-06-08.0.log");
            br = new BufferedReader(new InputStreamReader(fr, "gbk"));
            FileOutputStream outfile = new FileOutputStream("D:\\logs\\apilogs\\result-all.log", true); // 相对路径，如果没有则要建立一个新的output。txt文件
            out = new BufferedWriter(new OutputStreamWriter(outfile, "utf-8"));

            String line;
            while((line = br.readLine()) != null) {
                if (line.equals("") || line.indexOf(": ") == -1) continue;
                line=line.substring(line.indexOf(": ")+2);
                line = line.replaceAll("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}：[0-9]{2}：[0-9]{2}(：[0-9]{3})?", "");
                int n= (int) (Math.random()*4);
                line="主体UID为"+s[n]+line.trim();
                //line=line.trim();
                //System.out.println(line);
                map.insert(listToArray(WordSegmenter.segWithStopWords(line)));
            }
            out.write(map.toString());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Test Results:");
        System.out.println(map.toString());

    }
    @Test
    public void testSpell2(){
        LCSMap map = new LCSMap();
        String s1="第1条、交易信息中销售卖方主体未备案，交易单号为P4A08-11906072346142452未备案卖方主体代码为350206800091704";
        String s2="第1条、交易信息中销售卖方主体未备案，交易单号为P4A08-11906072345502451未备案卖方主体代码为350206800091704";
        String s3="第1条、交易信息中销售卖方主体未备案，交易单号为P4A10-11906080500432460未备案卖方主体代码为350206800091703";
        //String s4="GET /ism-web/api/relationController.do?getProductTree&traceablityCode=11877916&traceType=d HTTP/1.1";
       // String s5="GET /ism-web/api/relationController.do?getProductTree&traceablityCode=30009693777549594722304&traceType=d HTTP/1.1";
        //String s3="第1条、交易信息中销售卖方主体未备案，交易单号为P4A10-11906080500432460未备案卖方主体代码为350206800091704";
        String[] words1=listToArray(WordSegmenter.segWithStopWords(s1));
        String[] words2=listToArray(WordSegmenter.segWithStopWords(s2));
        String[] words3=listToArray(WordSegmenter.segWithStopWords(s3));
       //String[] words4=listToArray(WordSegmenter.segWithStopWords(s4));
        //String[] words5=listToArray(WordSegmenter.segWithStopWords(s5));
        System.out.println(words1);
        System.out.println(words2);
        //System.out.println(words4);
        //System.out.println(words5);
        map.insert(words1);
        map.insert(words2);
        map.insert(words3);
       // map.insert(words4);
        //map.insert(words5);
        System.out.println("Test Results:");
        System.out.println(map.toString());
    }
    @Test
    public void cutWords() {
        BufferedReader reader = null; BufferedWriter out=null;
        try {

            FileOutputStream outfile = new FileOutputStream("D:\\logs\\apilogs\\result-all.log", false); // 相对路径，如果没有则要建立一个新的output。txt文件
            out = new BufferedWriter(new OutputStreamWriter(outfile, "utf-8"));
            String basePath="D:\\\\logs\\\\logs\\\\";
            String[] listFile=new File(basePath).list();
                List<String> clu=new ArrayList<>();
                Map<String,Integer> cluCounts=new HashMap<>();
                EditDistanceTextSimilarity ed=new EditDistanceTextSimilarity();
                CosineTextSimilarity cos=new CosineTextSimilarity();
                Map<String,LCSMap> cluDetail=new HashMap<>();
               // Map<String,LCSMap> cluTem=new HashMap<>();
            WordSegmenter.segWithStopWords("");
                String tempString = null;
            for(int i=0;i<48;i++) {
                String filename="D:\\logs\\logs\\"+listFile[i];
                FileInputStream file = new FileInputStream(filename);
                reader = new BufferedReader(new InputStreamReader(file, "gbk"));
            long s1 = System.currentTimeMillis();

            long start = System.currentTimeMillis();
            double k=0.5;
            System.out.println("start:"+(start-s1)/1000);
                int count=0;
                while ((tempString = reader.readLine()) != null) {
                    if (tempString.equals("") || tempString.indexOf(" : ") == -1) continue;
                    count++;
                    String logClass = tempString.split(" : ")[0];
                    tempString = tempString.split(" : ")[1].replaceAll("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}：[0-9]{2}：[0-9]{2}(：[0-9]{3})?", "");
                    tempString = tempString.trim();
                    double score = 0;
                    boolean flag = false;
                    for (String c : clu) {
                        score = cos.similarScore(tempString, c);
                        if (score > k) {
                            cluCounts.put(c, cluCounts.getOrDefault(c, 0) + 1);
                            List<Word> list=WordSegmenter.segWithStopWords(tempString);
                            cluDetail.get(c).insert(listToArray(list));
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        clu.add(tempString);
                        cluCounts.put(tempString, cluCounts.getOrDefault(tempString, 0) + 1);
                        List<Word> list=WordSegmenter.segWithStopWords(tempString);
                        cluDetail.put(tempString,new LCSMap());
                        cluDetail.get(tempString).insert(listToArray(list));
                        flag = false;
                    }
                }
                long end = System.currentTimeMillis();
                System.out.println("总数："+count+"end:"+end);
                out.write("耗时：" + (end - start) / (1000) + "  阈值："+k+" " +"聚类数量：" + clu.size() + "\n");
                System.out.println("耗时：" + (end - start) / (1000) + "  阈值："+k+" " +"聚类数量：" + clu.size() + "\n");
                for (Map.Entry<String, Integer> entry : cluCounts.entrySet()) {
                    out.write(entry.getKey() + "\t" + entry.getValue() + "\n");
                    System.out.println(entry.getKey() + "\t" + entry.getValue());
                    String detail=cluDetail.get(entry.getKey()).toString();
                    out.write("\t" + detail+ "\n");
                    System.out.println("\t" +detail);

                }
                reader.close();
            }

                out.close();
            }catch(IOException e){
                e.printStackTrace();
            } finally{
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
    }

public String[] listToArray(List<Word> list){
    String[] words=new String[list.size()];
    for(int i=0;i<list.size();i++){
        words[i]=list.get(i).getText();
    }
    return words;
}

public static void main(String[] args){
    CosineTextSimilarity cos=new CosineTextSimilarity();
    String s1=" 主体UID为97638705270882304,第6条、生活必需品生产企业未备案，必需品名称为：白菜未备案主体代码000350206800091807";
    String s2=" 主体UID为99558628322705408,第1条、生活必需品生产企业未备案，必需品名称为：青花菜未备案主体代码000350206800091823";
//    String s3="2019-04-25 00：59：02保存交易明细信息成功！";
    System.out.println( WordSegmenter.segWithStopWords(s1));
    System.out.println( WordSegmenter.segWithStopWords(s2));
//    System.out.println( WordSegmenter.segWithStopWords(s3));
//    CosineTextSimilarity cos=new CosineTextSimilarity();
    System.out.println(cos.similarScore(s1,s2));
//    System.out.println(cos.similarScore(s1,s3));
//        String ss1="第1条、生活必需品生产企业未备案，必需品名称为：青花菜未备案主体代码000350206800091823";
//        String ss2="第1条、生活必需品生产企业未备案，必需品名称为：白菜未备案主体代码000350206800091807";
//        String ss3="保存交易明细信息成功！";
//        System.out.println( WordSegmenter.segWithStopWords(ss1));
//        System.out.println( WordSegmenter.segWithStopWords(ss2));
//        System.out.println( WordSegmenter.segWithStopWords(ss3));
//        System.out.println(cos.similarScore(ss1,ss2));
        System.out.println("第 1条 生活 必需品 生产 企业 未 备案 必需品 名称 为 * 未备案 主体 代码 *".replaceAll(" ",""));
    //System.out.println(cos.similarScore(ss1,ss3));
}
    @Test
    public void LCS() {
        BufferedReader reader = null; BufferedWriter out=null;
        try {

            FileOutputStream outfile = new FileOutputStream("D:\\logs\\apilogs\\result-cos.log", true); // 相对路径，如果没有则要建立一个新的output。txt文件
            out = new BufferedWriter(new OutputStreamWriter(outfile, "utf-8"));
            //for (double k = 0.95; k <= 1.0; k+=0.01) {
            FileInputStream file = new FileInputStream("D:\\logs\\apilogs\\Hadoop_2k.log");
            reader = new BufferedReader(new InputStreamReader(file, "utf-8"));
        }catch(IOException e){
            e.printStackTrace();
        } finally{
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}
