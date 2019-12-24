package com.lc.overseas.Utils;

import com.lc.overseas.pojo.ExcelDataCdr;
import com.lc.overseas.pojo.ExcelDataJm;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Description: 读取Excel内容
 */
public class ExcelReader {

    private static Logger logger = Logger.getLogger(ExcelReader.class.getName()); // 日志打印类

    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";

    /**
     * 根据文件后缀名类型获取对应的工作簿对象
     * @param inputStream 读取文件的输入流
     * @param fileType 文件后缀名类型（xls或xlsx）
     * @return 包含文件数据的工作簿对象
     * @throws IOException
     */
    public static Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
        Workbook workbook = null;
        if (fileType.equalsIgnoreCase(XLS)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (fileType.equalsIgnoreCase(XLSX)) {
            workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }

    /**
     * 读取Excel文件内容
     * @param fileName 要读取的Excel文件所在路径
     * @return 读取结果列表，读取失败时返回null
     */
    public static List<ExcelDataCdr> readExcelCdr(String fileName) {

        Workbook workbook = null;
        FileInputStream inputStream = null;

        try {
            // 获取Excel后缀名
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            // 获取Excel文件
            File excelFile = new File(fileName);
            if (!excelFile.exists()) {
                logger.warning("指定的Excel文件不存在！");
                return null;
            }

            // 获取Excel工作簿
            inputStream = new FileInputStream(excelFile);
            workbook = getWorkbook(inputStream, fileType);

            // 读取excel中的数据
            List<ExcelDataCdr> resultDataList = parseExcelCdr(workbook);

            return resultDataList;
        } catch (Exception e) {
            logger.warning("解析Excel失败，文件名：" + fileName + " 错误信息：" + e.getMessage());
            return null;
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.warning("关闭数据流出错！错误信息：" + e.getMessage());
                return null;
            }
        }
    }

    /**
     * 读取Excel文件内容
     * @param fileName 要读取的Excel文件所在路径
     * @return 读取结果列表，读取失败时返回null
     */
    public static List<ExcelDataJm> readExcelJm(String fileName) {

        Workbook workbook = null;
        FileInputStream inputStream = null;

        try {
            // 获取Excel后缀名
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            // 获取Excel文件
            File excelFile = new File(fileName);
            if (!excelFile.exists()) {
                logger.warning("指定的Excel文件不存在！");
                return null;
            }

            // 获取Excel工作簿
            inputStream = new FileInputStream(excelFile);
            workbook = getWorkbook(inputStream, fileType);

            // 读取excel中的数据
            List<ExcelDataJm> resultDataList = parseExcelJm(workbook);

            return resultDataList;
        } catch (Exception e) {
            logger.warning("解析Excel失败，文件名：" + fileName + " 错误信息：" + e.getMessage());
            return null;
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.warning("关闭数据流出错！错误信息：" + e.getMessage());
                return null;
            }
        }
    }

    /**
     * 解析Excel数据
     * @param workbook Excel工作簿对象
     * @return 解析结果
     */
    private static List<ExcelDataCdr> parseExcelCdr(Workbook workbook) throws ParseException {
        List<ExcelDataCdr> resultDataList = new ArrayList<>();
        // 解析sheet
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);

            // 校验sheet是否合法
            if (sheet == null) {
                continue;
            }

            // 获取第一行数据
            int firstRowNum = sheet.getFirstRowNum();
            Row firstRow = sheet.getRow(firstRowNum);
            if (null == firstRow) {
                logger.warning("解析Excel失败，在第一行没有读取到任何数据！");
            }

            // 解析每一行的数据，构造数据对象
            int rowStart = firstRowNum + 1;
            int rowEnd = sheet.getPhysicalNumberOfRows();
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row row = sheet.getRow(rowNum);

                if (null == row) {
                    continue;
                }

                ExcelDataCdr resultData = convertRowToDataCdr(row);
                if (null == resultData) {
                    logger.warning("第 " + row.getRowNum() + "行数据不合法，已忽略！");
                    continue;
                }
                resultDataList.add(resultData);
            }
        }

        return resultDataList;
    }


    /**
     * 解析Excel数据
     * @param workbook Excel工作簿对象
     * @return 解析结果
     */
    private static List<ExcelDataJm> parseExcelJm(Workbook workbook) throws ParseException {
        List<ExcelDataJm> resultDataList = new ArrayList<>();
        // 解析sheet
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);

            // 校验sheet是否合法
            if (sheet == null) {
                continue;
            }

            // 获取第一行数据
            int firstRowNum = sheet.getFirstRowNum();
            Row firstRow = sheet.getRow(firstRowNum);
            if (null == firstRow) {
                logger.warning("解析Excel失败，在第一行没有读取到任何数据！");
            }

            // 解析每一行的数据，构造数据对象
            int rowStart = firstRowNum + 1;
            int rowEnd = sheet.getPhysicalNumberOfRows();
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row row = sheet.getRow(rowNum);

                if (null == row) {
                    continue;
                }

                ExcelDataJm resultData = convertRowToDataJm(row);
                if (null == resultData) {
                    logger.warning("第 " + row.getRowNum() + "行数据不合法，已忽略！");
                    continue;
                }
                resultDataList.add(resultData);
            }
        }

        return resultDataList;
    }

    /**
     * 将单元格内容转换为字符串
     * @param cell
     * @return
     */
    private static String convertCellValueToString(Cell cell) {
        if(cell==null){
            return null;
        }
        String returnValue = null;
        switch (cell.getCellType()) {
            case NUMERIC:   //数字
                Double doubleValue = cell.getNumericCellValue();

                // 格式化科学计数法，取一位整数
                DecimalFormat df = new DecimalFormat("0");
                returnValue = df.format(doubleValue);
                break;
            case STRING:    //字符串
                returnValue = cell.getStringCellValue();
                break;
            case BOOLEAN:   //布尔
                Boolean booleanValue = cell.getBooleanCellValue();
                returnValue = booleanValue.toString();
                break;
            case BLANK:     // 空值
                break;
            case FORMULA:   // 公式
                returnValue = cell.getCellFormula();
                break;
            case ERROR:     // 故障
                break;
            default:
                break;
        }
        return returnValue;
    }

    /**
     * 提取每一行中需要的数据，构造成为一个结果数据对象
     *
     * 当该行中有单元格的数据为空或不合法时，忽略该行的数据
     *
     * @param row 行数据
     * @return 解析后的行数据对象，行数据错误时返回null
     */
    private static ExcelDataCdr convertRowToDataCdr(Row row) throws ParseException {
        ExcelDataCdr resultData = new ExcelDataCdr();
        DateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Cell cell;
        int cellNum = 0;
        cell = row.getCell(cellNum++);
        String id = convertCellValueToString(cell);
        resultData.setId(id);

        cell = row.getCell(cellNum++);
        String calldateStr = convertCellValueToString(cell);
        Date calldate = format1.parse(calldateStr);
        resultData.setCalldate(calldate);

        cell = row.getCell(cellNum++);
        String clid = convertCellValueToString(cell);
        resultData.setClid(clid);

        cell = row.getCell(cellNum++);
        String chargenbr = convertCellValueToString(cell);
        resultData.setChargenbr(chargenbr);

        cell = row.getCell(cellNum++);
        String src = convertCellValueToString(cell);
        resultData.setSrc(src);

        cell = row.getCell(cellNum++);
        String dst = convertCellValueToString(cell);
        resultData.setDst(dst);

        cell = row.getCell(cellNum++);
        String dcontext = convertCellValueToString(cell);
        resultData.setDcontext(dcontext);

        cell = row.getCell(cellNum++);
        String channel = convertCellValueToString(cell);
        resultData.setChannel(channel);

        cell = row.getCell(cellNum++);
        String dstchannel = convertCellValueToString(cell);
        resultData.setDstchannel(dstchannel);

        cell = row.getCell(cellNum++);
        String lastapp = convertCellValueToString(cell);
        resultData.setLastapp(lastapp);

        cell = row.getCell(cellNum++);
        String lastdata = convertCellValueToString(cell);
        resultData.setLastdata(lastdata);

        cell = row.getCell(cellNum++);
        String duration = convertCellValueToString(cell);
        resultData.setDuration(duration);

        cell = row.getCell(cellNum++);
        String billsec = convertCellValueToString(cell);
        resultData.setBillsec(billsec);

        cell = row.getCell(cellNum++);
        String disposition = convertCellValueToString(cell);
        resultData.setDisposition(disposition);

        cell = row.getCell(cellNum++);
        String amaflags = convertCellValueToString(cell);
        resultData.setAmaflags(amaflags);

        cell = row.getCell(cellNum++);
        String accountcode = convertCellValueToString(cell);
        resultData.setAccountcode(accountcode);

        cell = row.getCell(cellNum++);
        String uniqueid = convertCellValueToString(cell);
        resultData.setUniqueid(uniqueid);

        cell = row.getCell(cellNum++);
        String userfield = convertCellValueToString(cell);
        resultData.setUserfield(userfield);

        cell = row.getCell(cellNum++);
        String center_num = convertCellValueToString(cell);
        resultData.setCenter_num(center_num);

        cell = row.getCell(cellNum++);
        String entp_exten = convertCellValueToString(cell);
        resultData.setEntp_exten(entp_exten);

        cell = row.getCell(cellNum++);
        String service_type = convertCellValueToString(cell);
        resultData.setService_type(service_type);

        cell = row.getCell(cellNum++);
        String calltype = convertCellValueToString(cell);
        resultData.setCalltype(calltype);

        cell = row.getCell(cellNum++);
        String dialext = convertCellValueToString(cell);
        resultData.setDialext(dialext);

        cell = row.getCell(cellNum++);
        String queuename = convertCellValueToString(cell);
        resultData.setQueuename(queuename);

        cell = row.getCell(cellNum++);
        String agent = convertCellValueToString(cell);
        resultData.setAgent(agent);

        cell = row.getCell(cellNum++);
        String outgoing_duration = convertCellValueToString(cell);
        resultData.setOutgoing_duration(outgoing_duration);

        cell = row.getCell(cellNum++);
        String outgoing_ringStr = convertCellValueToString(cell);
        Date outgoing_ring = format1.parse(outgoing_ringStr);
        resultData.setOutgoing_ring(outgoing_ring);

        cell = row.getCell(cellNum++);
        String outgoing_answStr = convertCellValueToString(cell);
        Date outgoing_answ = format1.parse(outgoing_answStr);
        resultData.setOutgoing_answ(outgoing_answ);

        cell = row.getCell(cellNum++);
        String outgoing_endStr = convertCellValueToString(cell);
        Date outgoing_end = format1.parse(outgoing_endStr);
        resultData.setOutgoing_end(outgoing_end);

        cell = row.getCell(cellNum++);
        String enterprise_id = convertCellValueToString(cell);
        resultData.setEnterprise_id(enterprise_id);

        cell = row.getCell(cellNum++);
        String partition_id = convertCellValueToString(cell);
        resultData.setPartition_id(partition_id);

        cell = row.getCell(cellNum++);
        String session_id = convertCellValueToString(cell);
        resultData.setSession_id(session_id);

        cell = row.getCell(cellNum++);
        String billingno = convertCellValueToString(cell);
        resultData.setBillingno(billingno);


        return resultData;
    }

    /**
     * 提取每一行中需要的数据，构造成为一个结果数据对象
     *
     * 当该行中有单元格的数据为空或不合法时，忽略该行的数据
     *
     * @param row 行数据
     * @return 解析后的行数据对象，行数据错误时返回null
     */
    private static ExcelDataJm convertRowToDataJm(Row row) throws ParseException {
        ExcelDataJm resultData = new ExcelDataJm();
        DateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        DateFormat format2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Cell cell;
        int cellNum = 0;

        cell = row.getCell(cellNum++);
        String dept_info = convertCellValueToString(cell);
        resultData.setDept_info(dept_info);

        cell = row.getCell(cellNum++);
        String user_id = convertCellValueToString(cell);
        resultData.setUser_id(user_id);

        cell = row.getCell(cellNum++);
        String user_ext = convertCellValueToString(cell);
        resultData.setUser_ext(user_ext);


        cell = row.getCell(cellNum++);
        String start_dateStr = convertCellValueToString(cell).trim();
        Date start_date = format1.parse(start_dateStr);
        resultData.setStart_date(start_date);


        cell = row.getCell(cellNum++);
        String end_dateStr = convertCellValueToString(cell).trim();
        Date end_date = format1.parse(end_dateStr);
        resultData.setEnd_date(end_date);


        cell = row.getCell(cellNum++);
       /* String start_date_txtStr = convertCellValueToString(cell);
        Date start_date_txt = format2.parse(start_date_txtStr);
        resultData.setStart_date_txt(start_date_txt);
        */

        cell = row.getCell(cellNum++);
        /*String end_date_txtStr = convertCellValueToString(cell);
        Date end_date_txt = format2.parse(end_date_txtStr);
        resultData.setEnd_date_txt(end_date_txt);
        */

        cell = row.getCell(cellNum++);
        String caller_nbr = convertCellValueToString(cell);
        resultData.setCaller_nbr(caller_nbr);

        cell = row.getCell(cellNum++);
        String called_nbr = convertCellValueToString(cell);
        resultData.setCalled_nbr(called_nbr);

        cell = row.getCell(cellNum++);
        String call_direct = convertCellValueToString(cell);
        resultData.setCall_direct(call_direct);


        cell = row.getCell(cellNum++);
        String yzj_name = convertCellValueToString(cell);
        resultData.setYzj_name(yzj_name);

        cell = row.getCell(cellNum++);
        String yzj_nbr = convertCellValueToString(cell);
        resultData.setYzj_nbr(yzj_nbr);

        cell = row.getCell(cellNum++);
        String trans_nbr = convertCellValueToString(cell);
        resultData.setTrans_nbr(trans_nbr);

        cell = row.getCell(cellNum++);
        String durtaion = convertCellValueToString(cell);
        resultData.setDurtaion(durtaion);

        return resultData;
    }
}