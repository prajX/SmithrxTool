/*    */ package com.bsc.qa.facets.utils.filemapper;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Field
/*    */ {
/*    */   private String name;
/*    */   private int startPos;
/*    */   private int endPos;
/*    */   private int length;
/*    */   private boolean goldStandardOptional;
/*    */   private boolean NavitusOptional;
/*    */   private DataType dataType;
/*    */   
/*    */   public Field(String name, int startPos, int endPos, int length, boolean goldStandardOptional, boolean navitusOptional, DataType dataType) {
/* 16 */     this.name = name;
/* 17 */     this.startPos = startPos;
/* 18 */     this.endPos = endPos;
/* 19 */     this.length = length;
/* 20 */     this.goldStandardOptional = goldStandardOptional;
/* 21 */     this.NavitusOptional = navitusOptional;
/* 22 */     this.dataType = dataType;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 26 */     return this.name;
/*    */   }
/*    */   
/*    */   public int getStartPos() {
/* 30 */     return this.startPos;
/*    */   }
/*    */   
/*    */   public int getEndPos() {
/* 34 */     return this.endPos;
/*    */   }
/*    */   
/*    */   public int getLength() {
/* 38 */     return this.length;
/*    */   }
/*    */   
/*    */   public boolean isGoldStandardOptional() {
/* 42 */     return this.goldStandardOptional;
/*    */   }
/*    */   
/*    */   public boolean isNavitusOptional() {
/* 46 */     return this.NavitusOptional;
/*    */   }
/*    */   
/*    */   public DataType getDataType() {
/* 50 */     return this.dataType;
/*    */   }
/*    */ }


/* Location:              C:\Users\praj04\Music\SmithRxTool\app\SmithrxTool-0.0.1.jar!\com\bsc\qa\facet\\utils\filemapper\Field.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */