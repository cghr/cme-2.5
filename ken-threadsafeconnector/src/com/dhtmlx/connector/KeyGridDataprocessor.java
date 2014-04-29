/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ public class KeyGridDataprocessor
/*  4:   */   extends GridDataProcessor
/*  5:   */ {
/*  6:   */   public KeyGridDataprocessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory)
/*  7:   */   {
/*  8: 6 */     super(connector, config, request, cfactory);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public String name_data(String name)
/* 12:   */   {
/* 13:11 */     if (name.equals("gr_id")) {
/* 14:12 */       return "__dummy__id__";
/* 15:   */     }
/* 16:13 */     return super.name_data(name);
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.KeyGridDataprocessor
 * JD-Core Version:    0.7.0.1
 */