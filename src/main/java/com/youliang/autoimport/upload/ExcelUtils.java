package com.youliang.autoimport.upload;

import com.jjccb.onlineLoan.common.component.ExcelParser;
import com.jjccb.onlineLoan.common.component.ExcelParser.ExcelFieldInfo;
import com.jjccb.onlineLoan.common.component.ExcelTitle;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version $Id: ExcelUtils.java, v 0.1 2018/9/21 15:02 Lyndon_Mi Exp $
 * @author: Lyndon_Mi
 */
public class ExcelUtils {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_TIME_FORMAT);
    private static Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    // 返回整型或者字符串
    public static <T> T getCellValue(Cell cell, Class<T> resultType) {
        if (cell == null) {
            return null;
        }
        CellType cellTypeEnum = cell.getCellTypeEnum();
        if (cellTypeEnum.equals(CellType.BLANK)) {
            return null;
        } else if (cellTypeEnum.equals(CellType.STRING)) {
            if (resultType == Integer.class) {
                return (T) Integer.valueOf(cell.getStringCellValue());
            } else if (resultType == Long.class) {
                return (T) Long.valueOf(new Double(cell.getNumericCellValue()).longValue());
            } else {
                return (T) cell.getStringCellValue();
            }
        } else if (cellTypeEnum.equals(CellType.NUMERIC)) {
            if (resultType == Integer.class) {
                return (T) Integer.valueOf(new Double(cell.getNumericCellValue()).intValue());
            } else if (resultType == Long.class) {
                return (T) Long.valueOf(new Double(cell.getNumericCellValue()).longValue());
            } else if (resultType == String.class) {
                return (T) Long.valueOf(new Double(cell.getNumericCellValue()).longValue()).toString();
            }
        }
        throw new RuntimeException("只能解析整数或字符串");
    }


    private static Workbook getWorkbook(File excelFile) {
        String fileType = excelFile.getName().substring(excelFile.getName().lastIndexOf(".") + 1);
        System.out.println("filetype: " + fileType);
        Workbook wb;
        FileInputStream fis = null;
        try {
            if ("xls".equals(fileType)) {
                fis = new FileInputStream(excelFile);
                wb = new HSSFWorkbook(fis);
            } else if ("xlsx".equals(fileType)) {
                wb = new XSSFWorkbook(excelFile);
            } else {
                throw new RuntimeException("文件类型错误");
            }
        } catch (Exception e) {
            throw new RuntimeException("读取文件失败", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return wb;
    }

    /**
     * @param startRow 开始行数(为null时从0开始)
     */
    public static List<List<String>> getData(File excelFile, int sheetIndex, Integer startRow) {
        Workbook wb = getWorkbook(excelFile);
        Sheet sheet = wb.getSheetAt(sheetIndex);
        List<List<String>> dataList = new ArrayList<>(sheet.getLastRowNum());
        if (startRow == null) {
            startRow = sheet.getFirstRowNum();
        }
        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<String> values = new ArrayList<>(row.getLastCellNum());
            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (cell.getCellTypeEnum() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    values.add(SIMPLE_DATE_FORMAT.format(date));
                    continue;
                }
                cell.setCellType(CellType.STRING);
                values.add(cell.getStringCellValue());
            }
            dataList.add(values);
        }
        return dataList;
    }


    /**
     * @param startRow 开始行数(默认0)
     */
    public static <T> List<T> getData(File excelFile, Integer startRow, Class<T> clazz) {
        List<List<String>> dataList = getData(excelFile, 0, startRow);
        log.info("excel-data-list: {}", dataList);
        List<T> objList = new ArrayList<>();
        if (dataList.size() == 0 || dataList.get(0) == null) {
            return objList;
        }
        List<ExcelFieldInfo> excelFieldList = ExcelParser.getExcelFieldList(clazz);
        if (excelFieldList.size() > dataList.get(0).size()) {
            throw new RuntimeException("fields数量超过excel文件列数");
        }
        try {
            for (List<String> data : dataList) {
                T obj = clazz.getConstructor().newInstance();
                for (int i = 0; i < data.size(); i++) {
                    String value = data.get(i);
                    ExcelFieldInfo excelFieldInfo = ExcelParser.getExcelField(excelFieldList, i + 1);
                    if (excelFieldInfo == null) {
                        continue;
                    }
                    setValue(excelFieldInfo, obj, value);
                }
                objList.add(obj);
            }
        } catch (Exception e) {
            throw new RuntimeException("映射数据结构失败", e);
        }
        return objList;
    }

    private static void setValue(ExcelFieldInfo excelFieldInfo, Object obj, String value) {
        try {
            String type = excelFieldInfo.getField().getType().getTypeName();
            Object v = value;
            switch (type) {
                case "int":
                case "java.lang.Integer":
                    v = Integer.valueOf(value);
                    break;
                case "long":
                case "java.lang.Long":
                    v = Long.valueOf(value);
                    break;
                case "java.math.BigDecimal":
                    v = new BigDecimal(value);
                    break;
                case "java.time.LocalDate":
                    try {
                        v = LocalDate.parse(value, DateTimeFormatter
                                .ofPattern(excelFieldInfo.getExcelField().dateFormat()));
                    } catch (Exception e) {
                        v = LocalDateTime.parse(value, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)).toLocalDate();
                    }
                    break;
                case "java.time.LocalDateTime":
                    try {
                        v = LocalDateTime.parse(value, DateTimeFormatter
                                .ofPattern(excelFieldInfo.getExcelField().dateFormat()));
                    } catch (Exception e) {
                        v = LocalDateTime.parse(value, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
                    }
                    break;
            }
            // 设置值
            excelFieldInfo.getField().setAccessible(true);
            excelFieldInfo.getField().set(obj, v);
        } catch (Exception e) {
            throw new RuntimeException("设置值失败", e);
        }
    }


    /**
     * 导出Excel
     */
    public static <T> void export(Class<T> clazz, List<T> dataList, OutputStream outputStream) {
        int startRow = 0;
        List<ExcelFieldInfo> excelFieldInfoList = ExcelParser.getExcelFieldList(clazz);
        int maxCell = excelFieldInfoList.size();
        Workbook wk = new XSSFWorkbook();
        Sheet sheet = wk.createSheet();
        ExcelTitle excelTitle = ExcelParser.getExcelTitle(clazz);
        if (excelTitle != null) {
            maxCell = excelTitle.value().length;
            Row title = sheet.createRow(startRow++);
            for (int i = 0; i < excelTitle.value().length; i++) {
                title.createCell(i).setCellValue(excelTitle.value()[i]);
            }
        }

        if (dataList == null) {
            return;
        }
        for (T data : dataList) {
            Row dataRow = sheet.createRow(startRow++);
            for (int i = 0; i < maxCell; i++) {
                ExcelFieldInfo fieldInfo = ExcelParser.getExcelField(excelFieldInfoList, i + 1);
                if (fieldInfo == null) {
                    continue;
                }
                fieldInfo.getField().setAccessible(true);
                Object value;
                try {
                    value = fieldInfo.getField().get(data);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("获取属性失败", e);
                }
                if (value instanceof Temporal) {
                    value = DateTimeFormatter.ofPattern(fieldInfo.getExcelField().dateFormat())
                            .format(((Temporal) value));
                }
                dataRow.createCell(i).setCellValue(value == null ? "" : value.toString());
            }
        }
        try {
            wk.write(outputStream);
        } catch (Exception e) {
            throw new RuntimeException("输出Excel文件失败", e);
        }
    }

//    public static void main(String[] args) throws IOException {
////        File excelFile = new File("C:\\Users\\john\\Desktop\\test2772009854110010218.xlsx");
////        List<ScfWhitelistExcelData> scfWhitelistList = getData(excelFile, 1, ScfWhitelistExcelData.class);
////        System.out.println(scfWhitelistList);
//
//        List<ScfWhitelistExcelData> dataList = Lists.newArrayList();
//        for (int i = 1; i <= 5; i++) {
//            ScfWhitelistExcelData data = new ScfWhitelistExcelData();
//            data.setIndex(i);
//            data.setCreditCode("code - " + i);
//            data.setCustCard("card - " + i);
//            data.setCustName("name - " + i);
//            data.setEnterpriseName("enterpriseName - " + i);
//            data.setExpireDate(LocalDate.now());
//            data.setQuota(BigDecimal.valueOf(12.33));
//            data.setRelationName("供应商");
//            dataList.add(data);
//        }
//        export(ScfWhitelistExcelData.class, dataList, new FileOutputStream(File.createTempFile("xxx", ".xlsx")));
//    }

}
