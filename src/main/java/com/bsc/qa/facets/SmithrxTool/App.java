package com.bsc.qa.facets.SmithrxTool;

import com.bsc.qa.facets.utils.SmithrxUtility;
import com.bsc.qa.facets.utils.db.connectionmanager.DbBootstrap;
import com.bsc.qa.facets.utils.excel.ExcelWriterUtil;
import com.bsc.qa.facets.utils.filemapper.NavitusFieldsConfig;
import com.bsc.qa.facets.utils.filemapper.NavitusFileReaderWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class App {
	public static void main(String[] args) {
		System.out.println("Smithrx Tool");
		Path appHome = SmithrxUtility.getAppHome();
		Path configFile = appHome.resolve("config").resolve("properties.txt");
		Properties p = new Properties();
		try {
			InputStream in = Files.newInputStream(configFile, new java.nio.file.OpenOption[0]);
			try {
				p.load(in);
				if (in != null)
					in.close();
			} catch (Throwable throwable) {
				if (in != null)
					try {
						in.close();
					} catch (Throwable throwable1) {
						throwable.addSuppressed(throwable1);
					}
				throw throwable;
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to load config:" + configFile.toAbsolutePath(), e);
		}

		try {
			SmithrxUtility.showConfigDialog(p, configFile);
		} catch (Exception e) {

			e.printStackTrace();
		}

		System.out.println("input file:" + p.getProperty("INPUT_FILE"));
		Path inputFile = appHome.resolve("inputfiles").resolve(p.getProperty("INPUT_FILE"));
		List<Map<String, String>> data = NavitusFileReaderWriter.readRecords(inputFile.toFile(),
				NavitusFieldsConfig.getFields());
		DbBootstrap.createDbConnection(p);

		String outputFileName = "Smithrx_" + (new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")).format(new Date()) + ".xlsx";
		Path outputDir = appHome.resolve("outputfiles");
		try {
			Files.createDirectories(outputDir, (FileAttribute<?>[]) new FileAttribute[0]);
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		String outputFilePath = outputDir.resolve(outputFileName).toString();
		List<String> headers = List.of(new String[] { "GRGR_ID", "SBSB_ID", "MEME_FIRST_NAME", "MEME_LAST_NAME",
				"MEME_SSN", "MEME_DOB", "MEME_SFX", "MEME_REL", "MEME_ID_NAME", "MATX_ACC_TYPE", "ACAC_ACC_NO",
				"ACCUM_AMOUNT", "ACCUM_YEAR", "FATX_ACC_IND", "SERVICE_DT", "PDPD_ACC_SFX", "MATX_BEN_BEG_DT",
				"PDPD_ID", "MATX_PERIOD_TYPE", "REC_IND", "MBR_OR_FAM_MCTR_RSN_CD", "MBR_OR_FAM_EXTR_ID" });

		List<String> theaders = List.of(new String[] { "GRGR_ID", "SBSB_ID", "MEME_FIRST_NAME", "MEME_LAST_NAME",
				"MEME_SSN", "MEME_DOB", "MEME_SFX", "MEME_REL", "MEME_ID_NAME", "MATX_ACC_TYPE", "ACAC_ACC_NO",
				"ACCUM_AMOUNT", "ACCUM_YEAR", "FATX_ACC_IND", "SERVICE_DT", "PDPD_ACC_SFX", "MATX_BEN_BEG_DT",
				"PDPD_ID", "MATX_PERIOD_TYPE", "REC_IND", "MATX_MCTR_RSN", "MATX_EXTERNAL_ID" });

		List<String> tfheaders = List.of(new String[] { "GRGR_ID", "SBSB_ID", "MEME_FIRST_NAME", "MEME_LAST_NAME",
				"MEME_SSN", "MEME_DOB", "MEME_SFX", "MEME_REL", "MEME_ID_NAME", "FATX_ACC_TYPE", "ACAC_ACC_NO",
				"ACCUM_AMOUNT", "ACCUM_YEAR", "FATX_ACC_IND", "SERVICE_DT", "PDPD_ACC_SFX", "FATX_BEN_BEG_DT",
				"PDPD_ID", "FATX_PERIOD_TYPE", "REC_IND", "FATX_MCTR_RSN", "FATX_EXTERNAL_ID" });

		String query = SmithrxUtility.dbQueryCreator(p);

		Map<String, String> prodRuleMap = SmithrxUtility.getProdRuleData(p);

		@SuppressWarnings("unchecked")
		List<String> r1BucketList = (List<String>) SmithrxUtility.fetchRuleData().get("R1").get("Buckets");

		@SuppressWarnings("unchecked")
		List<String> r2BucketList = (List<String>) SmithrxUtility.fetchRuleData().get("R2").get("Buckets");

		List<Map<String, Object>> matxData = new ArrayList<>();
		List<Map<String, Object>> fatxData = new ArrayList<>();
		for (Map<String, String> mapData : data) {

			List<Map<String, Object>> dbData = SmithrxUtility.dbUtility(query, mapData.get("CardHolderId"),
					((String) mapData.get("PersonCode")).substring(((String) mapData.get("PersonCode")).length() - 1),
					mapData.get("DateOfService"));
			System.out.println("Fetching data for cardholderid:" + (String) mapData.get("CardHolderId")
					+ "-person code:" + ((String) mapData.get("PersonCode"))
							.substring(((String) mapData.get("PersonCode")).length() - 1));
			for (Map<String, Object> dbd : dbData) {
				String prodId = (String) dbd.get("PDPD_ID");
				dbd.put("SERVICE_DT", mapData.get("DateOfService"));
				dbd.put("MATX_EXTERNAL_ID", mapData.get("TransmissionId"));
				dbd.put("FATX_EXTERNAL_ID", mapData.get("TransmissionId"));
				dbd.put("FATX_ACC_IND", "N");
				dbd.put("MATX_BEN_BEG_DT", "2026-01-01 00:00:00.0");
				dbd.put("FATX_BEN_BEG_DT", "2026-01-01 00:00:00.0");
				dbd.put("REC_IND", "M");
				dbd.put("MATX_MCTR_RSN", "1041");
				dbd.put("FATX_MCTR_RSN", "1041");
				dbd.put("MATX_PERIOD_TYPE", "C");
				dbd.put("FATX_PERIOD_TYPE", "C");
				String rule = prodRuleMap.get(prodId);
				List<String> bucketList = rule.equalsIgnoreCase("R1") ? r1BucketList : r2BucketList;

				for (String s : bucketList) {
					Map<String, Object> rowMapData = new HashMap<>(dbd);
					String[] bucketDetail = s.split("-");
					rowMapData.put("MATX_ACC_TYPE", bucketDetail[1]);
					rowMapData.put("FATX_ACC_TYPE", bucketDetail[1]);
					rowMapData.put("ACAC_ACC_NO", bucketDetail[2]);
					if (bucketDetail[1].equalsIgnoreCase("D")) {
						rowMapData.put("ACCUM_AMOUNT", Double.valueOf(
								Double.valueOf(mapData.get("DedAccumulatorAppliedAmount")).doubleValue() / 100.0D));
					} else if (bucketDetail[1].equalsIgnoreCase("L")) {
						if (bucketDetail[3].equalsIgnoreCase("DEDLMT")) {
							rowMapData.put("ACCUM_AMOUNT", Double.valueOf(
									Double.valueOf(mapData.get("OopAccumulatorAppliedAmount")).doubleValue() / 100.0D));
						} else if (bucketDetail[3].equalsIgnoreCase("DED")) {
							rowMapData.put("ACCUM_AMOUNT", Double.valueOf(
									Double.valueOf(mapData.get("DedAccumulatorAppliedAmount")).doubleValue() / 100.0D));
						} else if (bucketDetail[3].equalsIgnoreCase("LMT")) {
							rowMapData.put("ACCUM_AMOUNT", Double
									.valueOf((Double.valueOf(mapData.get("OopAccumulatorAppliedAmount")).doubleValue()
											- Double.valueOf(mapData.get("DedAccumulatorAppliedAmount")).doubleValue())
											/ 100.0D));
						}
					}

					if (bucketDetail[0].equals("M")) {
						matxData.add(rowMapData);
						continue;
					}
					if (bucketDetail[0].equals("F")) {
						fatxData.add(rowMapData);
					}
				}
			}
		}

		try {
			ExcelWriterUtil.writeExcel(outputFilePath, "MATX", theaders, matxData);
			ExcelWriterUtil.writeExcel(outputFilePath, "FATX", tfheaders, fatxData);
		} catch (IOException e) {

			e.printStackTrace();
		}

		p.remove("FACETS_USER");
		p.remove("FACETS_PASSWORD");
		try {
			OutputStream out = Files.newOutputStream(configFile, new java.nio.file.OpenOption[0]);
			try {
				p.store(out, (String) null);
				if (out != null)
					out.close();
			} catch (Throwable throwable) {
				if (out != null)
					try {
						out.close();
					} catch (Throwable throwable1) {
						throwable.addSuppressed(throwable1);
					}
				throw throwable;
			}
		} catch (IOException e1)

		{
			e1.printStackTrace();
		}

		System.out.println("Completed");
		System.out.println("Press ENTER to exit...");
		try {
			System.in.read();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}