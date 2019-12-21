package com.example.loggenerate.service;

import com.example.loggenerate.dao.LogDao;
import com.example.loggenerate.entity.ism_data_transfer_log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.example.loggenerate.LoggenerateApplication.YMD;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class LogService {
    private static final Logger logger = LoggerFactory
            .getLogger(LogService.class);
    @Autowired
    private LogDao logDao;
    @Value("${recordNum}")
    private int recordNum;


    public void logCollecct() {
          logSave(10,"2019-05-09 00:00:00", "2019-05-10 00:00:00");
    }

    public void logSave(int recordNum, String startTime, String endTime) {
        try {
            List<ism_data_transfer_log> spList = logDao.getEntity(startTime, endTime,recordNum);
            BufferedWriter out=null;
            FileOutputStream outfile = new FileOutputStream("D:\\paper\\logs\\2019-11-17.log",true); // 相对路径，如果没有则要建立一个新的output。txt文件
            out = new BufferedWriter(new OutputStreamWriter(outfile, "utf-8"));
            if(spList!=null) {
                for (ism_data_transfer_log log : spList) {
                    String s="";
                    if(log.type_code.equals("success")) {
                        s=log.process_time+" "+"INFO 256668  --- [http-nio-8081-exec-9] "+log.process_api+"   : "+
                        log.process_api+" "+" "+log.process_log.split("#")[0]+" "+log.data_quantity+" "+log.create_by+" "+log.type_code;
                    }else if(log.type_code.equals("failure")){
                        s=log.process_time+" "+"ERROR 256668  --- [http-nio-8081-exec-9] "+log.process_api+"   : "+
                                log.process_api+" "+" "+log.process_log.split("#")[0]+" "+log.data_quantity+" "+log.create_by+" "+log.type_code;
                    }
                    out.write(s+"\r\n");
                }
            }else {
                out.write("无可用数据");
            }
        }catch (Exception ex){
            logger.info(""+ex.getCause());
        }
    }
    public static void readFileByLines(String fileName) {
        BufferedReader reader = null;BufferedWriter out=null;
        try {
            FileInputStream file = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(file, "gbk"));

            FileOutputStream outfile = new FileOutputStream("D:\\paper\\logs\\2019-12-09.log",true); // 相对路径，如果没有则要建立一个新的output。txt文件
            out = new BufferedWriter(new OutputStreamWriter(outfile, "utf-8"));

            String tempString = null;
            int line = 1;
            boolean flag = true;
            StringBuilder log = new StringBuilder();
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null&&line<10) {
                // 显示行号
                if(tempString.equals("")){continue;}
                String s = tempString.split(" ")[0];

                //log.append(tempString+"\n");
                if (!s.equals("") && s.matches("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$")) {
//                   if(log.toString().contains("物流信息中交易信息未备案，物流单号为")) {
                       out.write(log.toString() + "\r\n");
//                       System.out.println(log);
//                   }
                    //Thread.sleep(2);
                    log.setLength(0);
                    log.append(tempString);
                    flag = true;
                    line++;
                } else if (!s.equals("") && flag) {
                    //log.append(tempString.substring(0,tempString.lastIndexOf(':')));
                    flag = false;
                } else {

                }

            }
            System.out.println(line);
            logger.info("行数："+line);
            reader.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}
