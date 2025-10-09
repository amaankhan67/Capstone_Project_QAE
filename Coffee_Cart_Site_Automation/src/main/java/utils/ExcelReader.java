package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	private static final String DATA_FILE_PATH = ConfigReader.getProperty("excelpath");

	public static Object[][] getTestData(String sheetName){
		Object[][] data = null;
		Workbook workbook = null;
		FileInputStream input = null;

		try {
			File file = new File(DATA_FILE_PATH);
			if(!file.exists()) {
				throw new IOException("Excel File not found");
			}

			input = new FileInputStream(file);
			workbook = new XSSFWorkbook(input);
			Sheet sheet = workbook.getSheet(sheetName);
			if(sheet == null) {
				throw new IllegalArgumentException("Sheet " + sheetName + " not found in workbook");
			}

			int rowCount = sheet.getLastRowNum() + 1;
			int colCount = sheet.getRow(0).getLastCellNum();
			data = new Object[rowCount][colCount];

			for(int i=0; i<rowCount; i++) {
				Row row = sheet.getRow(i);
				if(row == null)
					continue;
				for(int j=0; j<colCount; j++) {
					Cell cell = row.getCell(j);
					data[i][j] = cell.toString();
				}
			}
		} catch(Exception e) {
			throw new RuntimeException("Failed to read Excel ", e);
		} finally {
			try {
				if (workbook != null) workbook.close();
				if (input != null) input.close();
			}catch(Exception e) {
			}
		}
		return data;
	}

	public static Map<String,String> getAllCoffeeItemsWithPrices(String sheetName){
		Map<String, String> coffeeNamesWithPrices = null;
		FileInputStream input = null;
		Workbook workbook = null;

		try {
			File file = new File(DATA_FILE_PATH);
			if(!file.exists()) {
				throw new IOException("Excel File not Found");
			}
			input = new FileInputStream(file);
			workbook = new XSSFWorkbook(input);
			Sheet sheet = workbook.getSheet(sheetName);
			if(sheet == null) {
				throw new IllegalArgumentException("Sheet " + sheetName + " not found in workbook");
			}

			int rowCount = sheet.getLastRowNum() + 1;	//getLastRowNum returns 0-based count
			coffeeNamesWithPrices = new HashMap<>();

			for(int i=0; i<rowCount; i++) {
				Row row = sheet.getRow(i);
				if(row == null)
					continue;
				String coffeeName = row.getCell(0).toString();
				String coffeePrice = row.getCell(1).toString();
				if(!coffeeName.isEmpty() && !coffeePrice.isEmpty()) {
					coffeeNamesWithPrices.put(coffeeName, coffeePrice);
				}	
			}
		} catch(Exception e) {
			throw new RuntimeException("Failed to read Excel ", e);
		} finally {
			try {
				if (workbook != null) workbook.close();
				if (input != null) input.close();
			}catch(Exception e) {
			}
		}
		return coffeeNamesWithPrices;
	}
}
