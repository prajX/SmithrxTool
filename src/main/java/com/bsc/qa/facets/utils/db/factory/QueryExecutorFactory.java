/*    */ package com.bsc.qa.facets.utils.db.factory;
/*    */ 
/*    */ import com.bsc.qa.facets.utils.db.FacetsQueryExecutor;
/*    */ import com.bsc.qa.facets.utils.db.QueryExecutor;
/*    */ import com.bsc.qa.facets.utils.db.RtcQueryExecutor;
/*    */ import com.bsc.qa.facets.utils.db.connectionmanager.DbType;
/*    */ import java.sql.Connection;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QueryExecutorFactory
/*    */ {
/*    */   public static QueryExecutor getExecutor(DbType dbType, Connection conn) {
/* 14 */     switch (dbType) {
/*    */       case FACETS:
/* 16 */         return (QueryExecutor)new FacetsQueryExecutor(conn);
/*    */       
/*    */       case RTC:
/* 19 */         return (QueryExecutor)new RtcQueryExecutor(conn);
/*    */     } 
/*    */     
/* 22 */     throw new UnsupportedOperationException("Unsupported DB");
/*    */   }
/*    */ }


/* Location:              C:\Users\praj04\Music\SmithRxTool\app\SmithrxTool-0.0.1.jar!\com\bsc\qa\facet\\utils\db\factory\QueryExecutorFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */