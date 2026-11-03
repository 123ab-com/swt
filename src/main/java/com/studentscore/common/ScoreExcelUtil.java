package com.studentscore.common;

import com.studentscore.dto.ScoreImportRow;
import com.studentscore.vo.ScoreVO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 成绩 Excel 导入导出工具。
 * 导出列：学号 / 姓名 / 班级 / 课程 / 平时 / 期末 / 总评 / 学期
 * 导入列：学号 / 课程编号 / 学期 / 平时成绩 / 期末成绩
 */
public final class ScoreExcelUtil {

    private static final String[] EXPORT_HEADERS =
            {"学号", "姓名", "班级", "课程", "平时成绩", "期末成绩", "总评成绩", "学期"};

    private static final String[] IMPORT_HEADERS =
            {"学号", "课程编号", "学期", "平时成绩", "期末成绩"};

    private ScoreExcelUtil() {
    }

    public static void write(HttpServletResponse response, String fileName, List<ScoreVO> rows) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        try {
            Sheet sheet = workbook.createSheet("成绩");
            Row header = sheet.createRow(0);
            for (int i = 0; i < EXPORT_HEADERS.length; i++) {
                header.createCell(i).setCellValue(EXPORT_HEADERS[i]);
            }

            int rowIndex = 1;
            for (ScoreVO vo : rows) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(text(vo.getStudentNo()));
                row.createCell(1).setCellValue(text(vo.getStudentName()));
                row.createCell(2).setCellValue(text(vo.getClassName()));
                row.createCell(3).setCellValue(text(vo.getCourseName()));
                row.createCell(4).setCellValue(number(vo.getUsualScore()));
                row.createCell(5).setCellValue(number(vo.getExamScore()));
                row.createCell(6).setCellValue(number(vo.getTotalScore()));
                row.createCell(7).setCellValue(text(vo.getSemester()));
            }
            for (int i = 0; i < EXPORT_HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }
            writeOut(response, workbook, fileName);
        } finally {
            workbook.close();
        }
    }

    /** 导入模板：只有表头 */
    public static void writeTemplate(HttpServletResponse response, String fileName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        try {
            Sheet sheet = workbook.createSheet("成绩导入");
            Row header = sheet.createRow(0);
            for (int i = 0; i < IMPORT_HEADERS.length; i++) {
                header.createCell(i).setCellValue(IMPORT_HEADERS[i]);
            }
            Row sample = sheet.createRow(1);
            sample.createCell(0).setCellValue("2024001");
            sample.createCell(1).setCellValue("C001");
            sample.createCell(2).setCellValue(Constants.currentSemester());
            sample.createCell(3).setCellValue(85);
            sample.createCell(4).setCellValue(90);
            for (int i = 0; i < IMPORT_HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }
            writeOut(response, workbook, fileName);
        } finally {
            workbook.close();
        }
    }

    public static List<ScoreImportRow> read(InputStream inputStream) {
        List<ScoreImportRow> rows = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isBlank(row, formatter)) {
                    continue;
                }
                ScoreImportRow item = new ScoreImportRow();
                item.setStudentNo(cellText(row.getCell(0), formatter));
                item.setCourseNo(cellText(row.getCell(1), formatter));
                item.setSemester(cellText(row.getCell(2), formatter));
                item.setUsualScore(cellDecimal(row.getCell(3), formatter));
                item.setExamScore(cellDecimal(row.getCell(4), formatter));
                rows.add(item);
            }
        } catch (Exception e) {
            throw new BusinessException("Excel 解析失败，请使用导入模板填写：" + e.getMessage());
        }
        return rows;
    }

    private static void writeOut(HttpServletResponse response, Workbook workbook, String fileName)
            throws IOException {
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        workbook.write(response.getOutputStream());
    }

    private static String cellText(Cell cell, DataFormatter formatter) {
        if (cell == null) {
            return "";
        }
        return formatter.formatCellValue(cell).trim();
    }

    private static BigDecimal cellDecimal(Cell cell, DataFormatter formatter) {
        String value = cellText(cell, formatter);
        if (value.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new BusinessException("成绩必须是数字：" + value);
        }
    }

    private static boolean isBlank(Row row, DataFormatter formatter) {
        for (int i = 0; i < 5; i++) {
            if (!cellText(row.getCell(i), formatter).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static String text(String value) {
        return value == null ? "" : value;
    }

    private static double number(BigDecimal value) {
        return value == null ? 0D : value.doubleValue();
    }
}
