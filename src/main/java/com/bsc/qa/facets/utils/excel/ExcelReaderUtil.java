package com.bsc.qa.facets.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtil {
	private final Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
	private final List<String> headers = new ArrayList<String>();

	public ExcelReaderUtil(String excelFilePath) throws IOException {
		this(excelFilePath, "sheet1");
	}

	@SuppressWarnings("resource")
	public ExcelReaderUtil(String excelFilePath, String sheetName) throws IOException {
		FileInputStream fis = null;
		Workbook workbook = null;

		try {
			fis = new FileInputStream(new File(excelFilePath));

			// Choose workbook type based on file extension
			if (excelFilePath.toLowerCase().endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fis);
			} else if (excelFilePath.toLowerCase().endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			} else {
				throw new IllegalArgumentException("Invalid file format. Only .xls or .xlsx supported.");
			}

			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new IllegalArgumentException("Excel sheet is empty");
			}

			// Read header row
			Row headerRow = sheet.getRow(0);
			if (headerRow == null) {
				throw new IllegalArgumentException("Header row missing");
			}

			for (Cell cell : headerRow) {
				headers.add(getCellValueAsString(cell));
			}

			// Read data rows
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				Map<String, String> rowData = new HashMap<String, String>();
				String testMethodName = null;

				for (int j = 0; j < headers.size(); j++) {
					Cell cell = row.getCell(j);
					String cellValue = getCellValueAsString(cell);
					rowData.put(headers.get(j), cellValue);

					if (j == 0) { // first column assumed to be method name
						testMethodName = cellValue;
					}
				}

				if (testMethodName != null && !testMethodName.isEmpty()) {
					dataMap.put(testMethodName, rowData);
				}
			}

		} finally

		{

			if (fis != null) {
				fis.close();
			}
		}
	}

	public Map<String, Map<String, String>> getDataMap() {
		return dataMap;
	}

	public List<String> getHeaders() {
		return headers;
	}

	@SuppressWarnings("resource")
	public static List<Map<String, String>> fetchExcelData(String excelFilePath, String sheetName) {
		FileInputStream fis = null;
		Workbook workbook = null;
		List<Map<String, String>> dataList = new ArrayList<>();
		List<String> headers = new ArrayList<String>();

		try {
			fis = new FileInputStream(new File(excelFilePath));

			// Choose workbook type based on file extension
			if (excelFilePath.toLowerCase().endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fis);
			} else if (excelFilePath.toLowerCase().endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			} else {
				throw new IllegalArgumentException("Invalid file format. Only .xls or .xlsx supported.");
			}

			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new IllegalArgumentException("Excel sheet is empty");
			}

			// Read header row
			Row headerRow = sheet.getRow(0);
			if (headerRow == null) {
				throw new IllegalArgumentException("Header row missing");
			}

			for (Cell cell : headerRow) {
				headers.add(new DataFormatter().formatCellValue(cell));
			}

			// Read data rows
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (isRowEmpty(row))
					continue;
				if (row == null) {
					continue;
				}

				Map<String, String> rowData = new HashMap<String, String>();

				for (int j = 0; j < headers.size(); j++) {
					Cell cell = row.getCell(j);
					String cellValue = new DataFormatter().formatCellValue(cell);
					rowData.put(headers.get(j), cellValue);
				}

				dataList.add(rowData);
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally

		{

			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return dataList;
	}

	private static boolean isRowEmpty(Row row) {
		if (row == null)
			return true;
		for (Cell cell : row) {
			if (cell != null) {
				String value = cell.toString().trim();
				if (!value.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}


	/** Convert cell value to String (POI 3.9 safe). */
	private static String getCellValueAsString(Cell cell) {
		if (cell == null)
			return "";

		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
			} else {
				return String.valueOf(cell.getNumericCellValue());
			}
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		case BLANK:
		case ERROR:
		default:
			return "";
		}
	}

}
