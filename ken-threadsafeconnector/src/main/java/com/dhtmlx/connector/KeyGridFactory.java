/* 1:  */ package com.dhtmlx.connector;
/* 2:  */ 
/* 3:  */ public class KeyGridFactory
/* 4:  */   extends GridFactory
/* 5:  */ {
/* 6:  */   public DataProcessor createDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory)
/* 7:  */   {
/* 8:9 */     return new KeyGridDataprocessor(connector, config, request, cfactory);
/* 9:  */   }
/* ::  */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.KeyGridFactory
 * JD-Core Version:    0.7.0.1
 */