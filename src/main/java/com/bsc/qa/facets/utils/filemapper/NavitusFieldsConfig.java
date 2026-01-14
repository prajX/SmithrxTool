/*    */ package com.bsc.qa.facets.utils.filemapper;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class NavitusFieldsConfig
/*    */ {
/*    */   public static List<Field> getFields() {
/*  9 */     List<Field> fields = new ArrayList<>();
/*    */ 
/*    */     
/* 12 */     fields.add(new Field("SenderId", 1, 50, 50, false, false, DataType.vendorInboundMsg));
/* 13 */     fields.add(new Field("ReceiverId", 51, 70, 20, false, false, DataType.vendorInboundMsg));
/* 14 */     fields.add(new Field("TransmissionDateTime", 71, 86, 16, false, false, DataType.vendorInboundMsg));
/* 15 */     fields.add(new Field("TransmissionId", 87, 136, 50, false, false, DataType.vendorInboundMsg));
/*    */ 
/*    */     
/* 18 */     fields.add(new Field("DateOfService", 137, 144, 8, false, false, DataType.patientBenefitDetails));
/* 19 */     fields.add(new Field("AccumulatorBalanceCount", 145, 146, 2, true, false, DataType.patientBenefitDetails));
/* 20 */     fields.add(new Field("CardHolderId", 147, 166, 20, true, false, DataType.patientBenefitDetails));
/* 21 */     fields.add(new Field("GroupId", 167, 181, 15, true, false, DataType.patientBenefitDetails));
/* 22 */     fields.add(new Field("CarrierNumber", 182, 191, 10, true, false, DataType.patientBenefitDetails));
/* 23 */     fields.add(new Field("PatientId", 192, 211, 20, true, false, DataType.patientBenefitDetails));
/* 24 */     fields.add(new Field("PersonCode", 212, 214, 3, true, false, DataType.patientBenefitDetails));
/* 25 */     fields.add(new Field("PatientFirstName", 215, 239, 25, false, false, DataType.patientBenefitDetails));
/* 26 */     fields.add(new Field("MiddleInitial", 240, 240, 1, true, true, DataType.patientBenefitDetails));
/* 27 */     fields.add(new Field("PatientLastName", 241, 275, 35, false, false, DataType.patientBenefitDetails));
/* 28 */     fields.add(new Field("PatientRelationshipCode", 276, 278, 3, true, false, DataType.patientBenefitDetails));
/* 29 */     fields.add(new Field("DateOfBirth", 279, 286, 8, false, false, DataType.patientBenefitDetails));
/* 30 */     fields.add(new Field("PatientGenderCode", 287, 288, 2, true, true, DataType.patientBenefitDetails));
/*    */ 
/*    */     
/* 33 */     fields.add(new Field("DedAccumulatorBalanceQualifier", 289, 290, 2, false, false, DataType.accumulatorTransactionInformation));
/*    */     
/* 35 */     fields.add(new Field("DedAccumulatorNetworkIndicator", 291, 292, 2, false, false, DataType.accumulatorTransactionInformation));
/*    */     
/* 37 */     fields.add(new Field("DedAccumulatorAppliedAmount", 293, 302, 10, false, false, DataType.accumulatorTransactionInformation));
/*    */     
/* 39 */     fields.add(new Field("DedActionCode", 303, 303, 1, false, false, DataType.accumulatorTransactionInformation));
/* 40 */     fields.add(new Field("OopAccumulatorBalanceQualifier", 304, 305, 2, false, false, DataType.accumulatorTransactionInformation));
/*    */     
/* 42 */     fields.add(new Field("OopAccumulatorNetworkIndicator", 306, 307, 2, false, false, DataType.accumulatorTransactionInformation));
/*    */     
/* 44 */     fields.add(new Field("OopAccumulatorAppliedAmount", 308, 317, 10, false, false, DataType.accumulatorTransactionInformation));
/*    */     
/* 46 */     fields.add(new Field("OopActionCode", 318, 318, 1, false, false, DataType.accumulatorTransactionInformation));
/*    */     
/* 48 */     return fields;
/*    */   }
/*    */ }


/* Location:              C:\Users\praj04\Music\SmithRxTool\app\SmithrxTool-0.0.1.jar!\com\bsc\qa\facet\\utils\filemapper\NavitusFieldsConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */