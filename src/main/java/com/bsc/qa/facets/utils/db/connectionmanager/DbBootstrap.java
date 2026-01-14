/*     */ package com.bsc.qa.facets.utils.db.connectionmanager;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.SQLException;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import javax.sql.DataSource;
/*     */ import org.apache.commons.dbcp2.BasicDataSource;
/*     */ 
/*     */ 
/*     */ public class DbBootstrap
/*     */ {
/*  16 */   private static final Map<DbType, Connection> connections = new EnumMap<>(DbType.class);
/*  17 */   private static BasicDataSource facetsDataSource = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void createDbConnection(Properties p) {
/*     */     try {
/*  36 */       connections.put(DbType.FACETS, createFacetsConnection(p));
/*  37 */       System.out.println("Db connection established");
/*  38 */     } catch (SQLException e) {
/*     */       
/*  40 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Connection createFacetsConnection(Properties p) throws SQLException {
/*  46 */     String facetsUrl = "jdbc:oracle:thin:@//FACETS_HOST:FACETS_PORT/FACETS_ENV";
/*  47 */     String facetsEnv = p.getProperty("FACETS_ENV");
/*  48 */     String facetsHost = p.getProperty("FACETS_HOST");
/*  49 */     String port = p.getProperty("FACETS_PORT");
/*  50 */     String user = p.getProperty("FACETS_USER");
/*  51 */     String pass = p.getProperty("FACETS_PASSWORD");
/*     */     
/*  53 */     return DriverManager.getConnection(facetsUrl.replace("FACETS_ENV", facetsEnv).replace("FACETS_HOST", facetsHost)
/*  54 */         .replace("FACETS_PORT", port), user, pass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSource getPoolFacetsConnection() {
/*  64 */     if (facetsDataSource == null) {
/*     */       try {
/*  66 */         facetsDataSource = new BasicDataSource();
/*  67 */         facetsDataSource.setUrl(System.getenv("FACETS_URL").replace("FACETS_ENV", System.getenv("FACETS_ENV")));
/*  68 */         facetsDataSource.setUsername(System.getenv("FACETS_USER"));
/*  69 */         facetsDataSource.setPassword(System.getenv("FACETS_PASS"));
/*  70 */         facetsDataSource.setMinIdle(5);
/*  71 */         facetsDataSource.setMaxIdle(7);
/*  72 */         facetsDataSource.setMaxTotal(7);
/*  73 */         facetsDataSource.setMaxOpenPreparedStatements(100);
/*  74 */       } catch (Exception e) {
/*  75 */         System.out.println("Error in creating poolable facets connection");
/*  76 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/*  80 */     return (DataSource)facetsDataSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void getPoolFacetsConnectionDetails() {
/* 107 */     System.out.println("Active Connection Number: " + facetsDataSource.getNumActive());
/* 108 */     System.out.println("Idle connection Num: " + facetsDataSource.getNumIdle());
/* 109 */     System.out.println("Total Max Connection: " + facetsDataSource.getMaxTotal());
/*     */   }
/*     */ 
/*     */   
/*     */   public static Connection getConnection(DbType dbType) {
/* 114 */     return connections.get(dbType);
/*     */   }
/*     */   
/*     */   public static Set<DbType> getAvailableDbTypes() {
/* 118 */     return connections.keySet();
/*     */   }
/*     */   
/*     */   public static void closeConnection() {
/* 122 */     for (Connection conn : connections.values()) {
/*     */       try {
/* 124 */         if (conn != null && !conn.isClosed()) {
/* 125 */           conn.close();
/*     */         }
/* 127 */       } catch (SQLException e) {
/* 128 */         System.err.println("Error closing DB connection: " + e.getMessage());
/*     */       } 
/*     */     } 
/*     */     
/* 132 */     connections.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\praj04\Music\SmithRxTool\app\SmithrxTool-0.0.1.jar!\com\bsc\qa\facet\\utils\db\connectionmanager\DbBootstrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */