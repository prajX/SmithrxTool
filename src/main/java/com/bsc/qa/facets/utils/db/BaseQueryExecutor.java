/*     */ package com.bsc.qa.facets.utils.db;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract class BaseQueryExecutor implements QueryExecutor {
/*     */   protected final Connection connection;
/*     */   
/*     */   public BaseQueryExecutor(Connection connection) {
/*  18 */     this.connection = connection;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Map<String, Object>> fetchMultipleRows(String query) {
/*  23 */     List<Map<String, Object>> results = new ArrayList<>(); 
/*  24 */     try { Statement stmt = this.connection.createStatement(); try { ResultSet rs = stmt.executeQuery(query); 
/*  25 */         try { ResultSetMetaData meta = rs.getMetaData();
/*  26 */           int columnCount = meta.getColumnCount();
/*     */           
/*  28 */           while (rs.next()) {
/*  29 */             Map<String, Object> row = new HashMap<>();
/*  30 */             for (int i = 1; i <= columnCount; i++) {
/*  31 */               row.put(meta.getColumnName(i), rs.getObject(i));
/*     */             }
/*  33 */             results.add(row);
/*     */           } 
/*  35 */           if (rs != null) rs.close();  } catch (Throwable throwable) { if (rs != null) try { rs.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (stmt != null) stmt.close();  } catch (Throwable throwable) { if (stmt != null) try { stmt.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException e)
/*     */     
/*  37 */     { e.printStackTrace(); }
/*     */     
/*  39 */     return results;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> fetchSingleRow(String query) {
/*  45 */     Map<String, String> result = new HashMap<>(); 
/*  46 */     try { Statement stmt = this.connection.createStatement(); try { ResultSet rs = stmt.executeQuery(query); 
/*  47 */         try { ResultSetMetaData meta = rs.getMetaData();
/*  48 */           if (rs.next()) {
/*  49 */             for (int i = 1; i <= meta.getColumnCount(); i++) {
/*  50 */               result.put(meta.getColumnName(i), rs.getString(i));
/*     */             }
/*     */           }
/*  53 */           if (rs != null) rs.close();  } catch (Throwable throwable) { if (rs != null) try { rs.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (stmt != null) stmt.close();  } catch (Throwable throwable) { if (stmt != null) try { stmt.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException e)
/*     */     
/*  55 */     { e.printStackTrace(); }
/*     */     
/*  57 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Map<String, Object>> fetchMultipleRowsPrepared(String query, Object... params) {
/*  62 */     List<Map<String, Object>> results = new ArrayList<>(); 
/*  63 */     try { PreparedStatement pstmt = this.connection.prepareStatement(query); 
/*  64 */       try { for (int i = 0; i < params.length; i++) {
/*  65 */           pstmt.setObject(i + 1, params[i]);
/*     */         }
/*  67 */         ResultSet rs = pstmt.executeQuery(); 
/*  68 */         try { ResultSetMetaData meta = rs.getMetaData();
/*  69 */           int columnCount = meta.getColumnCount();
/*     */           
/*  71 */           while (rs.next()) {
/*  72 */             Map<String, Object> row = new HashMap<>();
/*  73 */             for (int j = 1; j <= columnCount; j++) {
/*  74 */               row.put(meta.getColumnName(j), rs.getObject(j));
/*     */             }
/*  76 */             results.add(row);
/*     */           } 
/*  78 */           if (rs != null) rs.close();  } catch (Throwable throwable) { if (rs != null)
/*  79 */             try { rs.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (pstmt != null) pstmt.close();  } catch (Throwable throwable) { if (pstmt != null) try { pstmt.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException e)
/*     */     
/*  81 */     { e.printStackTrace(); }
/*     */     
/*  83 */     return results;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> fetchSingleRowPrepared(String query, Object... params) {
/*  88 */     Map<String, Object> result = new HashMap<>(); 
/*  89 */     try { PreparedStatement pstmt = this.connection.prepareStatement(query); 
/*  90 */       try { for (int i = 0; i < params.length; i++) {
/*  91 */           pstmt.setObject(i + 1, params[i]);
/*     */         }
/*  93 */         ResultSet rs = pstmt.executeQuery(); 
/*  94 */         try { ResultSetMetaData meta = rs.getMetaData();
/*  95 */           int columnCount = meta.getColumnCount();
/*     */           
/*  97 */           if (rs.next()) {
/*  98 */             for (int j = 1; j <= columnCount; j++) {
/*  99 */               result.put(meta.getColumnName(j), rs.getObject(j));
/*     */             }
/*     */           }
/* 102 */           if (rs != null) rs.close();  } catch (Throwable throwable) { if (rs != null)
/* 103 */             try { rs.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (pstmt != null) pstmt.close();  } catch (Throwable throwable) { if (pstmt != null) try { pstmt.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException e)
/*     */     
/* 105 */     { e.printStackTrace(); }
/*     */     
/* 107 */     return result;
/*     */   }
/*     */ }