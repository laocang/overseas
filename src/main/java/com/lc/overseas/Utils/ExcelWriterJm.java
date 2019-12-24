package com.lc.overseas.Utils;

import com.lc.overseas.pojo.ExcelDataCdr;
import com.lc.overseas.pojo.ExcelDataJm;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Description: 写入Excel内容
 */
public class ExcelWriterJm {

    private static List<String> CELL_HEADS; //列头

    static{
        // 类装载时就载入指定好的列头信息，如有需要，可以考虑做成动态生成的列头
        CELL_HEADS = new ArrayList<>();


        CELL_HEADS.add("dept_info");
        CELL_HEADS.add("user_id");
        CELL_HEADS.add("user_ext");
        CELL_HEADS.add("start_date");
        CELL_HEADS.add("end_date");
        CELL_HEADS.add("start_date_txt");
        CELL_HEADS.add("end_date_txt");
        CELL_HEADS.add("caller_nbr");
        CELL_HEADS.add("called_nbr");
        CELL_HEADS.add("call_direct");
        CELL_HEADS.add("yzj_name");
        CELL_HEADS.add("yzj_nbr");
        CELL_HEADS.add("trans_nbr");
        CELL_HEADS.add("durtaion");

    }

    /**
     * 生成Excel并写入数据信息
     * @param dataList 数据列表
     * @return 写入数据后的工作簿对象
     */
    public static Workbook exportData(List<ExcelDataJm> dataList){
        // 生成xlsx的Excel
        Workbook workbook = new SXSSFWorkbook();

        // 如需生成xls的Excel，请使用下面的工作簿对象，注意后续输出时文件后缀名也需更改为xls
        //Workbook workbook = new HSSFWorkbook();

        // 生成Sheet表，写入第一行的列头
        Sheet sheet = buildDataSheet(workbook);
        //构建每行的数据内容
        int rowNum = 1;
        for (Iterator<ExcelDataJm> it = dataList.iterator(); it.hasNext(); ) {
            ExcelDataJm data = it.next();
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
    private static void convertDataToRow(ExcelDataJm data, Row row){
        int cellNum = 0;
        Cell cell;

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getDept_info() ? "" : data.getDept_info());


        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getUser_id() ? "" : data.getUser_id());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getUser_ext() ? "" : data.getUser_ext());

        cell = row.createCell(cellNum++);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(data.getStart_date());
        cell.setCellValue(dateString);

        cell = row.createCell(cellNum++);
        String dateString2 = formatter.format(data.getEnd_date());
        cell.setCellValue(dateString2);

        cell = row.createCell(cellNum++);
        String dateString3 = formatter.format(data.getStart_date());
        cell.setCellValue(dateString3);

        cell = row.createCell(cellNum++);
        String dateString4 = formatter.format(data.getEnd_date());
        cell.setCellValue(dateString4);


        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getCaller_nbr() ? "" : data.getCaller_nbr());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getCalled_nbr() ? "" : data.getCalled_nbr());


        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getCall_direct() ? "" : data.getCall_direct());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getYzj_name() ? "" : data.getYzj_name());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getYzj_nbr() ? "" : data.getYzj_nbr());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getTrans_nbr() ? "" : data.getTrans_nbr());

        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getDurtaion() ? "" : data.getDurtaion());

    }
}