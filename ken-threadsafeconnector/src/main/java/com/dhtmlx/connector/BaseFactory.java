/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ 
/*  5:   */ public class BaseFactory
/*  6:   */ {
/*  7:   */   public DataItem createDataItem(HashMap<String, String> data, DataConfig config, int index)
/*  8:   */   {
/*  9:25 */     return new DataItem(data, config, index);
/* 10:   */   }
/* 11:   */   
/* 12:   */   public DataWrapper createDataWrapper()
/* 13:   */   {
/* 14:36 */     return new MySQLDBDataWrapper();
/* 15:   */   }
/* 16:   */   
/* 17:   */   public DataProcessor createDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory)
/* 18:   */   {
/* 19:52 */     return new DataProcessor(connector, config, request, cfactory);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public DataAction createDataAction(String status, String id, HashMap<String, String> item_data)
/* 23:   */   {
/* 24:66 */     return new DataAction(status, id, item_data);
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.BaseFactory
 * JD-Core Version:    0.7.0.1
 */