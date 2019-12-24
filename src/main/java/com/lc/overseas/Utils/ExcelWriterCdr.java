package com.lc.overseas.Utils;

import com.lc.overseas.pojo.ExcelDataCdr;
import com.lc.overseas.pojo.ExcelDataJm;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Description: 写入Excel内容
 */
public class ExcelWriterCdr {

    private static List<String> CELL_HEADS; //列头

    static{
        // 类装载时就载入指定好的列头信息，如有需要，可以考虑做成动态生成的列头
        CELL_HEADS = new ArrayList<>();

        CELL_HEADS.add("id");
        CELL_HEADS.add("calldate");
        CELL_HEADS.add("clid");
        CELL_HEADS.add("chargenbr");
        CELL_HEADS.add("src");
        CELL_HEADS.add("dst");
        CELL_HEADS.add("dcontext");
        CELL_HEADS.add("channel");
        CELL_HEADS.add("dstchannel");
        CELL_HEADS.add("lastapp");
        CELL_HEADS.add("lastdata");
        CELL_HEADS.add("duration");
        CELL_HEADS.add("billsec");
        CELL_HEADS.add("disposition");
        CELL_HEADS.add("amaflags");
        CELL_HEADS.add("accountcode");
        CELL_HEADS.add("uniqueid");
        CELL_HEADS.add("userfield");
        CELL_HEADS.add("center_num");
        CELL_HEADS.add("entp_exten");
        CELL_HEADS.add("service_type");
        CELL_HEADS.add("calltype");
        CELL_HEADS.add("dialext");
        CELL_HEADS.add("queuename");
        CELL_HEADS.add("agent");
        CELL_HEADS.add("outgoing_duration");
        CELL_HEADS.add("outgoing_ring");
        CELL_HEADS.add("outgoing_answ");
        CELL_HEADS.add("outgoing_end");
        CELL_HEADS.add("enterprise_id");
        CELL_HEADS.add("partition_id");
        CELL_HEADS.add("session_id");
        CELL_HEADS.add("billingno");


    }

    /**
     * 生成Excel并写入数据信息
     * @param dataList 数据列表
     * @return 写入数据后的工作簿对象
     */
    public static Workbook exportData(List<ExcelDataCdr> dataList){
        // 生成xlsx的Excel
        Workbook workbook = new SXSSFWorkbook();

        // 如需生成xls的Excel，请使用下面的工作簿对象，注意后续输出时文件后缀名也需更改为xls
        //Workbook workbook = new HSSFWorkbook();

        // 生成Sheet表，写入第一行的列头
        Sheet sheet = buildDataSheet(workbook);
        //构建每行的数据内容
        int rowNum = 1;
        for (Iterator<ExcelDataCdr> it = dataList.iterator(); it.hasNext(); ) {
            ExcelDataCdr data = it.next();
            if (data == null) {
                continue;
            }
            //输出行数据
            Row row = sheet.createRow(rowNum++);
            convertDataToRow(data, row);
        }
        return workbook;
    }

    /**
     * 生成sheet表，并写入第一行数据（列头）
     * @param workbook 工作簿对象
     * @return 已经写入列头的Sheet
     */
    private static Sheet buildDataSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet();
        // 设置列头宽度
        for (int i=0; i<CELL_HEADS.size(); i++) {
            sheet.setColumnWidth(i, 4000);
        }
        // 设置默认行高
        sheet.setDefaultRowHeight((short) 400);
        // 构建头单元格样式
        CellStyle cellStyle = buildHeadCellStyle(sheet.getWorkbook());
        // 写入第一行各列的数据
        Row head = sheet.createRow(0);
        for (int i = 0; i < CELL_HEADS.size(); i++) {
            Cell cell = head.createCell(i);
            cell.setCellValue(CELL_HEADS.get(i));
            cell.setCellStyle(cellStyle);
        }
        return sheet;
    }

    /**
     * 设置第一行列头的样式
     * @param workbook 工作簿对象
     * @return 单元格样式对象
     */
    private static CellStyle buildHeadCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        //对齐方式设置
        style.setAlignment(HorizontalAlignment.CENTER);
        //边框颜色和宽度设置
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 下边框
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边框
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex()); // 右边框
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上边框
        //设置背景颜色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //粗体字设置
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    /**
     * 将数据转换成行
     * @param data 源数据
     * @param row 行对象
     * @return
     */
    private static void convertDataToRow(ExcelDataCdr data, Row row){
        int cellNum = 0;
        Cell cell;
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getId() ? "" : data.getId());

        cell = row.createCell(cellNum++);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(data.getCalldate());
        cell.setCellValue(dateString);

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getClid() ? "" : data.getClid());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getChargenbr() ? "" : data.getChargenbr());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getSrc() ? "" : data.getSrc());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getDst() ? "" : data.getDst());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getDcontext() ? "" : data.getDcontext());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getChannel() ? "" : data.getChannel());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getDstchannel() ? "" : data.getDstchannel());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getLastapp() ? "" : data.getLastapp());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getLastdata() ? "" : data.getLastdata());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getDuration() ? "" : data.getDuration());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getBillsec() ? "" : data.getBillsec());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getDisposition() ? "" : data.getDisposition());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getAmaflags() ? "" : data.getAmaflags());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getAccountcode() ? "" : data.getAccountcode());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getUniqueid() ? "" : data.getUniqueid());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getUserfield() ? "" : data.getUserfield());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getCenter_num() ? "" : data.getCenter_num());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getEntp_exten() ? "" : data.getEntp_exten());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getService_type() ? "" : data.getService_type());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getCalltype() ? "" : data.getCalltype());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getDialext() ? "" : data.getDialext());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getQueuename() ? "" : data.getQueuename());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getAgent() ? "" : data.getAgent());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getOutgoing_duration() ? "" : data.getOutgoing_duration());

        cell = row.createCell(cellNum++);
        String dateString2 = formatter.format(data.getOutgoing_ring());
        cell.setCellValue(dateString2);

        cell = row.createCell(cellNum++);
        String dateString3= formatter.format(data.getOutgoing_answ());
        cell.setCellValue(dateString3);

        cell = row.createCell(cellNum++);
        String dateString4 = formatter.format(data.getOutgoing_end());
        cell.setCellValue( dateString4);

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getEnterprise_id() ? "" : data.getEnterprise_id());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getPartition_id() ? "" : data.getPartition_id());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getSession_id() ? "" : data.getSession_id());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getBillingno() ? "" : data.getBillingno());
    }
}