/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ 
/*  5:   */ public class GridDataProcessor
/*  6:   */   extends DataProcessor
/*  7:   */ {
/*  8:   */   public GridDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory)
/*  9:   */   {
/* 10:20 */     super(connector, config, request, cfactory);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public String name_data(String name)
/* 14:   */   {
/* 15:27 */     if (name == "gr_id") {
/* 16:27 */       return this.config.id.name;
/* 17:   */     }
/* 18:28 */     String[] parts = name.split("c");
/* 19:   */     try
/* 20:   */     {
/* 21:32 */       if (parts[0].equals(""))
/* 22:   */       {
/* 23:33 */         int index = Integer.parseInt(parts[1]);
/* 24:34 */         return ((ConnectorField)this.config.text.get(index)).name;
/* 25:   */       }
/* 26:   */     }
/* 27:   */     catch (NumberFormatException localNumberFormatException) {}
/* 28:38 */     return name;
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.GridDataProcessor
 * JD-Core Version:    0.7.0.1
 */