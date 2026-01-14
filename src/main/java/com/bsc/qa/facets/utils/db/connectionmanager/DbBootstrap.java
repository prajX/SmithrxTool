
package com.bsc.qa.facets.utils.db.connectionmanager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class DbBootstrap {
	private static final Map<DbType, Connection> connections = new EnumMap<>(DbType.class);

	public static void createDbConnection(Properties p) {
		try {
			connections.put(DbType.FACETS, createFacetsConnection(p));
			System.out.println("Db connection established");
		} catch (SQLException e) {

			System.out.println("Error creating database connection:" + e.getMessage());
			System.out.println("Press ENTER to exit...");
			try {
				System.in.read();
				System.exit(0);
			} catch (IOException ie) {

				ie.printStackTrace();
			}
		}
	}

	private static Connection createFacetsConnection(Properties p) throws SQLException {
		String facetsUrl = "jdbc:oracle:thin:@//FACETS_HOST:FACETS_PORT/FACETS_ENV";
		String facetsEnv = p.getProperty("FACETS_ENV");
		String facetsHost = p.getProperty("FACETS_HOST");
		String port = p.getProperty("FACETS_PORT");
		String user = p.getProperty("FACETS_USER");
		String pass = p.getProperty("FACETS_PASSWORD");

		return DriverManager.getConnection(facetsUrl.replace("FACETS_ENV", facetsEnv).replace("FACETS_HOST", facetsHost)
				/* 54 */ .replace("FACETS_PORT", port), user, pass);
	}

	public static Connection getConnection(DbType dbType) {
		return connections.get(dbType);
	}

	public static Set<DbType> getAvailableDbTypes() {
		return connections.keySet();
	}

	public static void closeConnection() {
		for (Connection conn : connections.values()) {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				System.err.println("Error closing DB connection: " + e.getMessage());
			}
		}

		connections.clear();
	}
}