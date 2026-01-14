package com.bsc.qa.facets.utils.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface QueryExecutor {
  List<Map<String, Object>> fetchMultipleRows(String paramString) throws SQLException;
  
  Map<String, String> fetchSingleRow(String paramString) throws SQLException;
  
  List<Map<String, Object>> fetchMultipleRowsPrepared(String paramString, Object... paramVarArgs) throws SQLException;
  
  Map<String, Object> fetchSingleRowPrepared(String paramString, Object... paramVarArgs) throws SQLException;
}


/* Location:              C:\Users\praj04\Music\SmithRxTool\app\SmithrxTool-0.0.1.jar!\com\bsc\qa\facet\\utils\db\QueryExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */