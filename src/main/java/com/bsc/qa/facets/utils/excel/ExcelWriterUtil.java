/*    */ package com.bsc.qa.facets.utils.excel;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.apache.poi.ss.usermodel.Cell;
/*    */ import org.apache.poi.ss.usermodel.Row;
/*    */ import org.apache.poi.ss.usermodel.Sheet;
/*    */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExcelWriterUtil
/*    */ {
/*    */   public static void writeExcel(String filePath, String sheetName, List<String> headers, List<Map<String, Object>> data) throws IOException {
/*    */     XSSFWorkbook xSSFWorkbook;
/*    */     Sheet sheet;
/*    */     int rowNum;
/* 26 */     File file = new File(filePath);
/* 27 */     if (file.exists()) {
/* 28 */       FileInputStream fis = new FileInputStream(file); 
/* 29 */       try { xSSFWorkbook = new XSSFWorkbook(fis);
/* 30 */         fis.close(); } catch (Throwable throwable) { try { fis.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 31 */        sheet = xSSFWorkbook.getSheet(sheetName);
/* 32 */       if (sheet == null) {
/* 33 */         sheet = xSSFWorkbook.createSheet(sheetName);
/*    */       }
/*    */     } else {
/* 36 */       xSSFWorkbook = new XSSFWorkbook();
/* 37 */       sheet = xSSFWorkbook.createSheet(sheetName);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 43 */     if (sheet.getPhysicalNumberOfRows() == 0) {
/* 44 */       Row headerRow = sheet.createRow(0);
/* 45 */       for (int j = 0; j < headers.size(); j++) {
/* 46 */         headerRow.createCell(j).setCellValue(headers.get(j));
/*    */       }
/* 48 */       rowNum = 1;
/*    */     } else {
/* 50 */       rowNum = sheet.getLastRowNum() + 1;
/*    */     } 
/*    */ 
/*    */     
/* 54 */     for (Map<String, Object> rowData : data) {
/* 55 */       Row row = sheet.createRow(rowNum++);
/* 56 */       for (int col = 0; col < headers.size(); col++) {
/* 57 */         String header = headers.get(col);
/* 58 */         Cell cell = row.createCell(col);
/* 59 */         setCellValue(cell, rowData.get(header));
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 64 */     for (int i = 0; i < headers.size(); i++) {
/* 65 */       sheet.autoSizeColumn(i);
/*    */     }
/*    */     
/* 68 */     FileOutputStream fos = new FileOutputStream(filePath); 
/* 69 */     try { xSSFWorkbook.write(fos);
/* 70 */       fos.close(); } catch (Throwable throwable) { try { fos.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*    */        throw throwable; }
/* 72 */      xSSFWorkbook.close();
/*    */   }
/*    */   
/*    */   private static void setCellValue(Cell cell, Object value) {
/* 76 */     if (value == null) {
/* 77 */       cell.setBlank();
/* 78 */     } else if (value instanceof Number) {
/* 79 */       cell.setCellValue(((Number)value).doubleValue());
/* 80 */     } else if (value instanceof Boolean) {
/* 81 */       cell.setCellValue(((Boolean)value).booleanValue());
/*    */     } else {
/* 83 */       cell.setCellValue(value.toString());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\praj04\Music\SmithRxTool\app\SmithrxTool-0.0.1.jar!\com\bsc\qa\facet\\utils\excel\ExcelWriterUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */