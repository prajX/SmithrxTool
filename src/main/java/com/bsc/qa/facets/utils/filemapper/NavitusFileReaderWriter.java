package com.bsc.qa.facets.utils.filemapper;

import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NavitusFileReaderWriter {
	public static List<String> validateRecords(List<Map<String, String>> records, List<Field> fields, String mode) {
		List<String> errors = new ArrayList<>();

		for (int recordIndex = 0; recordIndex < records.size(); recordIndex++) {
			Map<String, String> record = records.get(recordIndex);

			for (Field field : fields) {

				boolean isOptional = mode.equalsIgnoreCase("gold") ? field.isGoldStandardOptional()
						: field.isNavitusOptional();

				String value = record.get(field.getName());

				if ((value == null || value.trim().isEmpty()) && !isOptional) {
					errors.add(String.format("Missing mandatory field '%s' in record %d (mode: %s)",
							new Object[] { field.getName(), /* 43 */ Integer.valueOf(recordIndex + 1), mode }));

					continue;
				}
				if (value != null) {
					value = value.trim();

					if (value.length() > field.getLength()) {
						errors.add(String.format("Field '%s' in record %d exceeds max length (%d)",
								new Object[] { field.getName(), /* 53 */ Integer.valueOf(recordIndex + 1),
										Integer.valueOf(field.getLength()) }));
					}
				}
			}
		}

		return errors;
	}

	public static void writeRecords(String fileName, List<Map<String, String>> records, List<Field> fields) {
		File fileDir = new File(System.getenv("OUTPUT_FILE_PATH"));
		File file = new File(fileDir, fileName);

		int totalLineLength = fields.stream().mapToInt(Field::getEndPos).max().orElse(0);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			try {
				for (Map<String, String> record : records) {

					char[] line = new char[totalLineLength];
					Arrays.fill(line, ' ');

					for (Field field : fields) {
						String value = record.getOrDefault(field.getName(), "");
						value = value.trim();

						if (value.length() > field.getLength()) {
							value = value.substring(0, field.getLength());
						} else if (value.length() < field.getLength()) {
							value = String.format("%-" + field.getLength() + "s", new Object[] { value });
						}

						int startIndex = field.getStartPos() - 1;
						int endIndex = Math.min(field.getEndPos(), totalLineLength);

						for (int i = 0; i < value.length() && startIndex + i < endIndex; i++) {
							line[startIndex + i] = value.charAt(i);
						}
					}

					writer.write(new String(line));
					writer.newLine();
				}

				writer.close();
			} catch (Throwable throwable) {
				try {
					writer.close();
				} catch (Throwable throwable1) {
					throwable.addSuppressed(throwable1);
				}
				throw throwable;
			}
		} catch (IOException e) {
			System.err.println("Error writing file: " + e);
		}

	}

	public static List<Map<String, String>> readRecords(File file, List<Field> fields) {
		List<Map<String, String>> records = new ArrayList<>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					Map<String, String> record = new LinkedHashMap<>();
					for (Field field : fields) {
						int startIndex = Math.max(0, field.getStartPos() - 1);
						int endIndex = Math.min(line.length(), field.getEndPos());
						String rawValue = "";
						if (startIndex < line.length()) {
							rawValue = line.substring(startIndex, endIndex);
						}
						record.put(field.getName(), rawValue.trim());
					}
					records.add(record);
				}

				reader.close();
			} catch (Throwable throwable) {
				try {
					reader.close();
				} catch (Throwable throwable1) {
					throwable.addSuppressed(throwable1);
				}
				throw throwable;
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e);
		}

		return records;
	}

	public static void updateEntireFileWithLineUpdates(String inputFileName, List<Field> fields,
			List<String> updateStrings) throws IOException {
		File fileDir = new File(System.getenv("OUTPUT_FILE_PATH"));
		File inputFile = new File(fileDir, inputFileName);

		File tempFile = new File(fileDir, "temp.txt");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			try {
				int index = 0;
				String line;
				while ((line = reader.readLine()) != null) {

					String updateStr = (index < updateStrings.size()) ? updateStrings.get(index) : "";

					Map<String, String> updates = parseUpdateString(updateStr);

					String updatedLine = updateLine(line, fields, updates);

					writer.write(updatedLine);
					writer.newLine();

					index++;
				}
				writer.close();
			} catch (Throwable throwable) {
				try {
					writer.close();
				} catch (Throwable throwable1) {
					throwable.addSuppressed(throwable1);
				}
				throw throwable;
			}
			reader.close();
		} catch (Throwable throwable) {
			try {
				reader.close();
			} catch (Throwable throwable1) {
				throwable.addSuppressed(throwable1);
			}
			throw throwable;
		}
		Files.move(tempFile, inputFile);
	}

	private static Map<String, String> parseUpdateString(String update) {
		Map<String, String> map = new HashMap<>();

		if (update == null || update.trim().isEmpty()) {
			return map;
		}

		String[] parts = update.split(",");

		for (String part : parts) {
			if (part.contains("=")) {

				String[] kv = part.split("=", 2);
				String key = kv[0].trim();
				String value = (kv.length > 1) ? kv[1].trim() : "";

				map.put(key, value);
			}
		}
		return map;
	}

	private static String updateLine(String line, List<Field> fields, Map<String, String> updates) {
		char[] updated = line.toCharArray();

		for (Field field : fields) {

			if (!updates.containsKey(field.getName())) {
				continue;
			}

			String value = updates.get(field.getName());

			int start = field.getStartPos() - 1;
			int end = Math.min(field.getEndPos(), updated.length);
			int len = field.getLength();

			if (value == null || value.isEmpty()) {
				for (int j = start; j < end; j++) {
					updated[j] = ' ';
				}

				continue;
			}

			value = value.trim();

			if (value.length() > len) {
				value = value.substring(0, len);
			} else if (value.length() < len) {
				value = String.format("%-" + len + "s", new Object[] { value });
			}

			for (int i = 0; i < value.length() && start + i < end; i++) {
				updated[start + i] = value.charAt(i);
			}
		}

		return new String(updated);
	}
}