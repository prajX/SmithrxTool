package com.bsc.qa.facets.utils;

import com.bsc.qa.facets.SmithrxTool.App;
import com.bsc.qa.facets.utils.db.QueryExecutor;
import com.bsc.qa.facets.utils.db.connectionmanager.DbBootstrap;
import com.bsc.qa.facets.utils.db.connectionmanager.DbType;
import com.bsc.qa.facets.utils.db.factory.QueryExecutorFactory;
import java.awt.GridLayout;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SmithrxUtility {
	public static Path getAppHome() {
		try {
			return Paths.get(
					(new File(App.class.getProtectionDomain().getCodeSource().getLocation().toURI())).getParent(),
					new String[0]);
		} catch (Exception e) {
			throw new RuntimeException("Unable to determine App home", e);
		}
	}

	public static Map<String, Map<String, Object>> fetchRuleData() {
		Map<String, Map<String, Object>> ruleData = new HashMap<>();
		ruleData.put("R2",
				Map.of("VendorBSDL", "SMRX", "INN/OON", "INN", "ProdType", "PPO", "TransType", "IB", "MCTRCode", "1041",
						"Buckets",

						List.of(new String[] { "M-D-2-DED", "M-D-3-DED", "M-D-6-DED", "M-D-16-DED", "F-D-2-DED",
								"F-D-3-DED", "F-D-6-DED", "F-D-16-DED", "M-L-10-DED", "M-L-11-DEDLMT", "M-L-12-DEDLMT",
								"M-L-16-DEDLMT", "M-L-26-DEDLMT", "F-L-7-DED", "F-L-8-DEDLMT", "F-L-9-DEDLMT",
								"F-L-17-DEDLMT", "F-L-27-DEDLMT", "M-L-3-LMT", "M-L-5-LMT", "M-L-21-LMT", "M-L-24-LMT",
								"M-L-44-LMT", "F-L-4-LMT", "F-L-6-LMT", "F-L-25-LMT", "F-L-28-LMT" })));

		ruleData.put("R1", Map.of("VendorBSDL", "SMRX", "INN/OON", "INN", "ProdType", "PPO", "TransType", "IB",
				"MCTRCode", "1041", "Buckets",

				List.of(new String[] { "M-D-6-DED", "M-D-16-DED", "F-D-6-DED", "F-D-16-DED", "M-L-10-DED",
						"M-L-11-DEDLMT", "M-L-12-DEDLMT", "M-L-16-DEDLMT", "M-L-26-DEDLMT", "F-L-7-DED", "F-L-8-DEDLMT",
						"F-L-9-DEDLMT", "F-L-17-DEDLMT", "F-L-27-DEDLMT", "M-L-3-LMT", "M-L-5-LMT", "M-L-21-LMT",
						"M-L-24-LMT", "M-L-44-LMT", "F-L-4-LMT", "F-L-6-LMT", "F-L-25-LMT", "F-L-28-LMT" })));

		return ruleData;
	}

	public static String dbQueryCreator(Properties p) {
		List<String> prodList = getProdIds(p);
		String queryPart = " AND MEPE.PDPD_ID IN (";
		for (String s : prodList) {
			queryPart = queryPart + "'" + s + "',";
		}
		queryPart = queryPart.substring(0, queryPart.length() - 1);
		queryPart = queryPart + ")";

		String query = "SELECT \r\n" + "    GRGR.GRGR_ID AS GRGR_ID,\r\n" + "    SBSB.SBSB_ID AS SBSB_ID,\r\n"
				+ "    MEME.MEME_FIRST_NAME AS MEME_FIRST_NAME,\r\n" + "    MEME.MEME_LAST_NAME AS MEME_LAST_NAME,\r\n"
				+ "    MEME.MEME_SSN AS MEME_SSN,\r\n" + "    MEME.MEME_BIRTH_DT AS MEME_DOB,\r\n"
				+ "    MEME.MEME_SFX AS MEME_SFX,\r\n" + "    MEME.MEME_REL AS MEME_REL,\r\n"
				+ "    MEME.MEME_ID_NAME AS MEME_ID_NAME,\r\n" + "    MEPE.MEPE_EFF_DT AS ACCUM_YEAR,\r\n"
				+ "    MEPE.PDPD_ID AS PDPD_ID,\r\n" + "    PDPD.PDPD_ACC_SFX AS PDPD_ACC_SFX,\r\n"
				+ "    MEPE.MEPE_EFF_DT ,\r\n" + "    MEPE.MEPE_TERM_DT \r\n" + "FROM FC_CMC_MEME_MEMBER MEME\r\n"
				+ "JOIN FC_CMC_SBSB_SUBSC SBSB\r\n" + "    ON SBSB.SBSB_CK = MEME.SBSB_CK\r\n"
				+ "JOIN FC_CMC_GRGR_GROUP GRGR\r\n" + "    ON SBSB.GRGR_CK = GRGR.GRGR_CK\r\n"
				+ "JOIN FC_CMC_MEPE_PRCS_ELIG MEPE\r\n" + "    ON MEPE.MEME_CK = MEME.MEME_CK\r\n"
				+ "JOIN FC_CMC_PDPD_PRODUCT PDPD\r\n" + "    ON MEPE.PDPD_ID = PDPD.PDPD_ID\r\n"
				+ "WHERE SBSB.SBSB_ID = ?\r\n" + "  AND MEME.MEME_SFX = ?\r\n"
				+ "  AND MEPE.MEPE_EFF_DT <=TO_TIMESTAMP(?,'YYYYMMDD') \r\n"
				+ "  AND MEPE.MEPE_TERM_DT >=TO_TIMESTAMP(?,'YYYYMMDD')\r\n" + "  AND MEPE.MEPE_ELIG_IND = 'Y'";
		query = query + queryPart;
		return query;
	}

	public static List<Map<String, Object>> dbUtility(String query, String sbid, String sfx, String serviceDate) {
		Connection facetsConnection = DbBootstrap.getConnection(DbType.FACETS);
		QueryExecutor facetsQueryExecutor = QueryExecutorFactory.getExecutor(DbType.FACETS, facetsConnection);

		if (facetsConnection != null) {

			try {
				return facetsQueryExecutor.fetchMultipleRowsPrepared(query,
						new Object[] { sbid, sfx, serviceDate, serviceDate });
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		return null;
	}

	public static List<String> getProdIds(Properties p) {
		List<String> prodIdList = new ArrayList<>();
		String[] r1Data = p.getProperty("R1_PRODID").split(",");
		String[] r2Data = p.getProperty("R2_PRODID").split(",");
		for (String s : r1Data) {
			if (!s.isBlank()) {
				prodIdList.add(s);
			}
		}

		for (String s : r2Data) {
			if (!s.isBlank()) {
				prodIdList.add(s);
			}
		}

		return prodIdList;
	}

	public static Map<String, String> getProdRuleData(Properties p) {
		String[] r1Data = p.getProperty("R1_PRODID").split(",");
		String[] r2Data = p.getProperty("R2_PRODID").split(",");
		Map<String, String> prodRuleMap = new HashMap<>();
		for (String s : r1Data) {
			if (!s.isBlank()) {
				prodRuleMap.put(s, "R1");
			}
		}
		for (String s : r2Data) {
			if (!s.isBlank()) {
				prodRuleMap.put(s, "R2");
			}
		}
		return prodRuleMap;
	}

	public static void showConfigDialog(Properties props, Path configFile) throws Exception {
		JTextField fileField = new JTextField(20);
		fileField.setEditable(false);
		JButton browse = new JButton("Browse...");
		browse.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();

			chooser.setFileSelectionMode(0);
			int result = chooser.showOpenDialog(null);
			if (result == 0) {
				File selected = chooser.getSelectedFile();
				fileField.setText(selected.getAbsolutePath());
			}
		});
		JTextField facetsEnv = new JTextField(props.getProperty("FACETS_ENV", ""), 20);
		JTextField facetsHost = new JTextField(props.getProperty("FACETS_HOST", ""), 20);
		JTextField port = new JTextField(props.getProperty("FACETS_PORT", ""), 20);
		JTextField user = new JTextField();
		JPasswordField pass = new JPasswordField();
		JTextField rule1 = new JTextField(props.getProperty("R1_PRODID", ""), 40);
		JTextField rule2 = new JTextField(props.getProperty("R2_PRODID", ""), 40);

		JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
		panel.add(new JLabel("Input File:"));
		panel.add(fileField);
		panel.add(new JLabel(""));
		panel.add(browse);
		panel.add(new JLabel("FACETS ENV:"));
		panel.add(facetsEnv);
		panel.add(new JLabel("FACETS HOST:"));
		panel.add(facetsHost);
		panel.add(new JLabel("FACETS PORT:"));
		panel.add(port);
		panel.add(new JLabel("FACETS USERNAME:"));
		panel.add(user);
		panel.add(new JLabel("FACETS PASSWORD:"));
		panel.add(pass);
		panel.add(new JLabel("Rule1 Product Ids (Comma separated):"));
		panel.add(rule1);
		panel.add(new JLabel("Rule2 Product Ids (Comma separated):"));
		panel.add(rule2);

		int result = JOptionPane.showConfirmDialog(null, panel, "Application Configuration", 2, -1);

		if (result != 0) {
			System.exit(0);
		}
		Path appHome = getAppHome();
		Path dataDir = appHome.resolve("inputfiles");
		Files.createDirectories(dataDir, (FileAttribute<?>[]) new FileAttribute[0]);
		Path source = Paths.get(fileField.getText(), new String[0]);
		Path target = dataDir.resolve(source.getFileName());
		Files.copy(source, target, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
		props.setProperty("INPUT_FILE", target.toString());
		props.setProperty("FACETS_ENV", facetsEnv.getText().trim());
		props.setProperty("FACETS_HOST", facetsHost.getText().trim());
		props.setProperty("FACETS_PORT", port.getText().trim());
		props.setProperty("FACETS_USER", user.getText().trim());
		props.setProperty("FACETS_PASSWORD", new String(pass.getPassword()).trim());
		props.setProperty("R1_PRODID", rule1.getText().trim());
		props.setProperty("R2_PRODID", rule2.getText().trim());

		OutputStream out = Files.newOutputStream(configFile, new java.nio.file.OpenOption[0]);
		try {
			props.store(out, "Updated by user");
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
	}
}