/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ 
/*  5:   */ public class SchedulerDataProcessor
/*  6:   */   extends DataProcessor
/*  7:   */ {
/*  8:   */   public SchedulerDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory)
/*  9:   */   {
/* 10:20 */     super(connector, config, request, cfactory);
/* 11:   */   }
/* 12:   */   
/* 13:   */   protected String name_data(String name)
/* 14:   */   {
/* 15:28 */     if (name.equals("start_date")) {
/* 16:29 */       return ((ConnectorField)this.config.text.get(0)).name;
/* 17:   */     }
/* 18:30 */     if (name.equals("end_date")) {
/* 19:31 */       return ((ConnectorField)this.config.text.get(1)).name;
/* 20:   */     }
/* 21:32 */     if (name.equals("text")) {
/* 22:33 */       return ((ConnectorField)this.config.text.get(2)).name;
/* 23:   */     }
/* 24:35 */     return super.name_data(name);
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.SchedulerDataProcessor
 * JD-Core Version:    0.7.0.1
 */