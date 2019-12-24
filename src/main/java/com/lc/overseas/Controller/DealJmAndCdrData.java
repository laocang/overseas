package com.lc.overseas.Controller;

import com.lc.overseas.Utils.ExcelReader;
import com.lc.overseas.Utils.ExcelWriterCdr;
import com.lc.overseas.Utils.ExcelWriterJm;
import com.lc.overseas.Utils.ExcelWriterMap;
import com.lc.overseas.pojo.ExcelDataCdr;
import com.lc.overseas.pojo.ExcelDataJm;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RequestMapping("DealJmAndCdrData")
@Controller
public class DealJmAndCdrData {


    @ResponseBody
    @RequestMapping("index")
    public String index() throws ParseException {
        //String excelFileName = "C:/Users/lc/Desktop/电信能力开放平台/招行呼叫统计/20191106/仅出局数据/Cdr19-30.xlsx";
        String excelFileName = "C:/Users/lc/Desktop/电信能力开放平台/招行呼叫统计/20191126/cdr20191118-20191125.xlsx";

        // 读取Excel文件内容
        List<ExcelDataCdr> readResult = ExcelReader.readExcelCdr(excelFileName);
        //List<ExcelDataJm> readResult2 = ExcelReader.readExcelJm("C:/Users/lc/Desktop/电信能力开放平台/招行呼叫统计/20191106/仅出局数据/Jm19-30.xlsx");
        List<ExcelDataJm> readResult2 = ExcelReader.readExcelJm("C:/Users/lc/Desktop/电信能力开放平台/招行呼叫统计/20191126/jm20191118-20191125.xlsx");
        Calendar calendar = Calendar.getInstance();
        Map<String,List<ExcelDataCdr>> cdrMap =  new HashMap<String,List<ExcelDataCdr>>();
        Map<String,List<ExcelDataJm>> jmMap =  new HashMap<String,List<ExcelDataJm>>();
        for(ExcelDataCdr edc : readResult){
            calendar.setTime(edc.getCalldate());
            String tempDate = calendar.get(Calendar.MONTH)+1+""+calendar.get(Calendar.DATE);
            if(cdrMap.containsKey(tempDate)){
                List<ExcelDataCdr> tempList = cdrMap.get(tempDate);
                tempList.add(edc);
                cdrMap.put(tempDate,tempList);
            }else{
                List<ExcelDataCdr> edclist = new ArrayList<ExcelDataCdr>();
                edclist.add(edc);
                cdrMap.put(tempDate,edclist);
            }
        }
        for(ExcelDataJm edj : readResult2){
            calendar.setTime(edj.getStart_date());
            String tempDate = calendar.get(Calendar.MONTH)+1+""+calendar.get(Calendar.DATE);
            if(jmMap.containsKey(tempDate)){
                List<ExcelDataJm> tempList = jmMap.get(tempDate);
                tempList.add(edj);
                jmMap.put(tempDate,tempList);
            }else{
                List<ExcelDataJm> edjlist = new ArrayList<ExcelDataJm>();
                edjlist.add(edj);
                jmMap.put(tempDate,edjlist);
            }
        }
        //导出cdr表中相对于jm表多的数据
   /*  List<ExcelDataCdr> finalResult = new ArrayList<ExcelDataCdr>();
        for(String key : cdrMap.keySet()){
            List<ExcelDataCdr> re1 = cdrCompareJm(cdrMap.get(key),jmMap.get(key));
            finalResult.addAll(re1);
        }

            // 写入数据到工作簿对象内
            Workbook workbook = ExcelWriterCdr.exportData(finalResult);

            // 以文件的形式输出工作簿对象
            FileOutputStream fileOut = null;
            try {
                String exportFilePath = "C:/Users/lc/Desktop/电信能力开放平台/招行呼叫统计/20191126/cdr多出的1118-1125.xlsx";
                //String exportFilePath = "C:/Users/lc/Desktop/电信能力开放平台/招行呼叫统计/20191106/仅出局数据/cdr多出的9.19-9.302.xlsx";
                File exportFile = new File(exportFilePath);
                if (!exportFile.exists()) {
                    exportFile.createNewFile();
                }

                fileOut = new FileOutputStream(exportFilePath);
                workbook.write(fileOut);
                fileOut.flush();
            } catch (Exception e) {
            } finally {
                try {
                    if (null != fileOut) {
                        fileOut.close();
                    }
                    if (null != workbook) {
                        workbook.close();
                    }
                } catch (IOException e) {
                }
            }*/
    //导出jm表中相对于cdr表中多的数据
    List<ExcelDataJm> finalResult2 = new ArrayList<ExcelDataJm>();
        for(String key : jmMap.keySet()){
            List<ExcelDataJm> re1 = jmCompareCdr(jmMap.get(key),cdrMap.get(key));
            finalResult2.addAll(re1);
        }
        // 写入数据到工作簿对象内
        Workbook workbook = ExcelWriterJm.exportData(finalResult2);

        // 以文件的形式输出工作簿对象
        FileOutputStream fileOut = null;
        try {
            String exportFilePath = "C:/Users/lc/Desktop/电信能力开放平台/招行呼叫统计/20191126/Jm多出的1118-1125.xlsx";
            //String exportFilePath = "C:/Users/lc/Desktop/电信能力开放平台/招行呼叫统计/20191106/仅出局数据/Jm多出的9.19-9.302.xlsx";
            File exportFile = new File(exportFilePath);
            if (!exportFile.exists()) {
                exportFile.createNewFile();
            }

            fileOut = new FileOutputStream(exportFilePath);
            workbook.write(fileOut);
            fileOut.flush();
        } catch (Exception e) {
        } finally {
            try {
                if (null != fileOut) {
                    fileOut.close();
                }
                if (null != workbook) {
                    workbook.close();
                }
            } catch (IOException e) {
            }
        }
        System.out.print("1111");
        return "导出成功";
    }



    @ResponseBody
    @RequestMapping("fiveSecond")
    public String fiveSecond(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        String excelFileName = "C:/Users/lc/Desktop/电信能力开放平台/招行呼叫统计/20191106/仅出局数据/Cdr19-30.xlsx";
        //String excelFileName = "C:/Users/lc/Desktop/电信能力开放平台/招行呼叫统计/20191107/仅出局数据/Cdr1001-1031.xlsx";

        // 读取Excel文件内容
        List<ExcelDataCdr> readResult = ExcelReader.readExcelCdr(excelFileName);
        List<ExcelDataJm> readResult2 = ExcelReader.readExcelJm("C:/Users/lc/Desktop/电信能力开放平台/招行呼叫统计/20191106/仅出局数据/Jm19-30.xlsx");
        //List<ExcelDataJm> readResult2 = ExcelReader.readExcelJm("C:/Users/lc/Desktop/电信能力开放平台/招行呼叫统计/20191107/仅出局数据/Jm1001-1031.xlsx");
        Calendar calendar = Calendar.getInstance();
        Map<String,List<ExcelDataCdr>> cdrMap =  new HashMap<String,List<ExcelDataCdr>>();
        Map<String,List<ExcelDataJm>> jmMap =  new HashMap<String,List<ExcelDataJm>>();
        for(ExcelDataCdr edc : readResult){
            calendar.setTime(edc.getCalldate());
            String tempDate = calendar.get(Calendar.MONTH)+1+""+calendar.get(Calendar.DATE);
            if(cdrMap.containsKey(tempDate)){
                List<ExcelDataCdr> tempList = cdrMap.get(tempDate);
                tempList.add(edc);
                cdrMap.put(tempDate,tempList);
            }else{
                List<ExcelDataCdr> edclist = new ArrayList<ExcelDataCdr>();
                edclist.add(edc);
                cdrMap.put(tempDate,edclist);
            }
        }
        for(ExcelDataJm edj : readResult2){
            calendar.setTime(edj.getStart_date());
            String tempDate = calendar.get(Calendar.MONTH)+1+""+calendar.get(Calendar.DATE);
            if(jmMap.containsKey(tempDate)){
                List<ExcelDataJm> tempList = jmMap.get(tempDate);
                tempList.add(edj);
                jmMap.put(tempDate,tempList);
            }else{
                List<ExcelDataJm> edjlist = new ArrayList<ExcelDataJm>();
                edjlist.add(edj);
                jmMap.put(tempDate,edjlist);
            }
        }
        //导出jm表中相对于cdr表中多的数据
        List<Map<String,Object>> finalResult2 = new ArrayList<Map<String,Object>>();
        for(String key : jmMap.keySet()){
            List<Map<String,Object>> re1 = jmCompareCdrSame(jmMap.get(key),cdrMap.get(key));
            finalResult2.addAll(re1);
        }
        // 写入数据到工作簿对象内
        Workbook workbook = ExcelWriterMap.exportData(finalResult2);

        // 以文件的形式输出工作簿对象
        FileOutputStream fileOut = null;
        try {
             //String exportFilePath = "C:/Users/lc/Desktop/电信能力开放平台/招行呼叫统计/20191113/多五秒的数据/1001-1031通话持续时间相差过五秒的账单.xlsx";
            String exportFilePath = "C:/Users/lc/Desktop/电信能力开放平台/招行呼叫统计/20191113/多五秒的数据/9.19-9.30通话持续时间相差过五秒的账单.xlsx";
            File exportFile = new File(exportFilePath);
            if (!exportFile.exists()) {
                exportFile.createNewFile();
            }

            fileOut = new FileOutputStream(exportFilePath);
            workbook.write(fileOut);
            fileOut.flush();
        } catch (Exception e) {
        } finally {
            try {
                if (null != fileOut) {
                    fileOut.close();
                }
                if (null != workbook) {
                    workbook.close();
                }
            } catch (IOException e) {
            }
        }
        return "导出成功";
    }


    public static List<ExcelDataCdr> cdrCompareJm(List<ExcelDataCdr> edc, List<ExcelDataJm> edj){
            if(edj==null){
                edj = new ArrayList<ExcelDataJm>();
            }
            if(edc==null){
                edc = new ArrayList<ExcelDataCdr>();
            }
            List<ExcelDataCdr> result = new ArrayList<ExcelDataCdr>();
            for(ExcelDataCdr tempEdc:edc){
                boolean flag = false;
                for(ExcelDataJm tempEdj:edj){
                    if(tempEdc.getDst().equals(tempEdj.getCalled_nbr())&&dealJmAndCdr(tempEdc,tempEdj)){//&&dealJmAndCdr(tempEdc,tempEdj)
                            tempEdj.setCalled_nbr(tempEdj.getCalled_nbr()+"xxxx");
                            flag = true;
                            break;
                    }
                }
                if(!flag){
                    result.add(tempEdc);
                }else {
                    flag = false;
                }
            }
            if(result!=null&&result.size()>0){
                System.out.print(result.get(0).getPartition_id()+"-----"+result.size()+"\n");
            }
            return result;
    }


    public static List<ExcelDataJm> jmCompareCdr(List<ExcelDataJm> edj, List<ExcelDataCdr> edc) throws ParseException {
        if(edj==null){
            edj = new ArrayList<ExcelDataJm>();
        }
        if(edc==null){
            edc = new ArrayList<ExcelDataCdr>();
        }
        Calendar calendar = Calendar.getInstance();
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ExcelDataJm> result = new ArrayList<ExcelDataJm>();
        for(ExcelDataJm tempEdj:edj){
            boolean flag = false;
            for(ExcelDataCdr tempEdc:edc){
                if((tempEdc.getDst().equals(tempEdj.getCalled_nbr()))&&dealJmAndCdr(tempEdc,tempEdj)){//&&(Math.abs(tempEdc.getOutgoing_answ().getTime()-tempEdj.getStart_date().getTime())<10000)
                        tempEdc.setDst(tempEdc.getDst()+"xxxx");
                        flag = true;
                        break;
                }
            }
            /*boolean flag = false;
            for(ExcelDataCdr tempEdc:edc){
                if(tempEdc.getDst().equals(tempEdj.getCalled_nbr())) {//如果jm这条记录有和cdr中相同的记录
                            long l2 = 0;
                            int count2 = 0;
                            for(ExcelDataJm tempEdj2:edj){//去查找这条cdr与jm记录中相差时间最短的
                                if(tempEdc.getDst().equals(tempEdj2.getCalled_nbr())) {
                                    long templ2 = Math.abs(tempEdj2.getStart_date().getTime() - tempEdc.getCalldate().getTime());
                                    if (count2 == 0) {
                                        l2 = templ2;
                                    } else {
                                        if (l2 > templ2) {
                                            l2 = templ2;
                                        }
                                    }
                                    count2++;
                                }
                            }
                            long l = 0;
                            int count = 0;
                            for(ExcelDataCdr tempEdc2:edc){//去查找这条jm与cdr中时间相差最短的
                                if(tempEdc2.getDst().equals(tempEdj.getCalled_nbr())) {
                                    long templ3 = Math.abs(tempEdj.getStart_date().getTime() - tempEdc2.getCalldate().getTime());
                                    if (count == 0) {
                                        l = templ3;
                                    } else {
                                        if (l > templ3) {
                                            l = templ3;
                                        }
                                    }
                                    count++;
                                }
                            }
                            if((l==l2)&&(Math.abs(tempEdj.getStart_date().getTime()-tempEdc.getCalldate().getTime())<180000)){//&&(Math.abs(tempEdj.getStart_date().getTime()-tempEdc.getCalldate().getTime())<100000)
                                    tempEdc.setDst(tempEdc.getDst()+"xxxx");
                                    flag = true;
                                    break;
                            }
                }
            }*/
            if(!flag){
                result.add(tempEdj);
            }else {
                flag = false;
            }
        }
        if(result!=null&&result.size()>0){
            System.out.print(result.get(0).getStart_date()+"-----"+result.size()+"\n");
        }
        return result;
    }


    public static List<Map<String,Object>> jmCompareCdrSame(List<ExcelDataJm> edj, List<ExcelDataCdr> edc) throws ParseException {
        if(edj==null){
            edj = new ArrayList<ExcelDataJm>();
        }
        if(edc==null){
            edc = new ArrayList<ExcelDataCdr>();
        }
        Calendar calendar = Calendar.getInstance();
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        for(ExcelDataJm tempEdj:edj){
            boolean flag = false;
            for(ExcelDataCdr tempEdc:edc){
                if((tempEdc.getDst().equals(tempEdj.getCalled_nbr()))&&dealJmAndCdr(tempEdc,tempEdj)){//&&(Math.abs(tempEdc.getOutgoing_answ().getTime()-tempEdj.getStart_date().getTime())<10000)
                    String[] arr = tempEdj.getDurtaion().split(":");
                    int jmm = Integer.valueOf(arr[0])*3600+Integer.valueOf(arr[1])*60+Integer.valueOf(arr[2]);
                    if(Math.abs(jmm-Integer.valueOf(tempEdc.getOutgoing_duration()))>5){
                        Map<String,Object> map = new HashMap<String,Object>();
                        Map<String,Object> mapcdr = new HashMap<String,Object>();
                        Map<String,Object> mapjm = new HashMap<String,Object>();
                        mapcdr.put("catalog","cdr");
                        mapcdr.put("caller_nbr",tempEdc.getSrc());
                        mapcdr.put("called_nbr",tempEdc.getDst());
                        mapcdr.put("starttime",tempEdc.getOutgoing_answ());
                        mapcdr.put("endtime",tempEdc.getOutgoing_end());
                        mapcdr.put("duration",tempEdc.getOutgoing_duration());
                        map.put("cdr",mapcdr);
                        mapjm.put("catalog","jm");
                        mapjm.put("caller_nbr",tempEdj.getCaller_nbr());
                        mapjm.put("called_nbr",tempEdj.getCalled_nbr());
                        mapjm.put("starttime",tempEdj.getStart_date());
                        mapjm.put("endtime",tempEdj.getEnd_date());
                        mapjm.put("duration",tempEdj.getDurtaion());
                        map.put("jm",mapjm);
                        result.add(map);
                    }
                    tempEdc.setDst(tempEdc.getDst()+"xxxx");
                    flag = true;
                    break;
                }
            }
            if(!flag){
            }else {
                flag = false;
            }
        }
        return result;
    }

    /**
     * 用于处理正确的时间匹配逻辑
     */
    public static boolean dealJmAndCdr(ExcelDataCdr cdr, ExcelDataJm jm){
        boolean result = false;
        if(cdr.getCalldate().getTime()>jm.getStart_date().getTime()){
            if(jm.getEnd_date().getTime()>=cdr.getCalldate().getTime()){
                result = true;
            }
        }else if (cdr.getCalldate().getTime()>jm.getStart_date().getTime()){
            result = true;
        }else{
            if(cdr.getOutgoing_end().getTime()>=jm.getStart_date().getTime()){
                result = true;
            }
        }
        return result;
    }
}
