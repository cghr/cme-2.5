/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ 
/*  5:   */ public class TreeDataProcessor
/*  6:   */   extends DataProcessor
/*  7:   */ {
/*  8:   */   public TreeDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory)
/*  9:   */   {
/* 10:20 */     super(connector, config, request, cfactory);
/* 11:21 */     request.set_relation("");
/* 12:   */   }
/* 13:   */   
/* 14:   */   public String name_data(String name)
/* 15:   */   {
/* 16:29 */     if (name.equals("tr_pid")) {
/* 17:30 */       return this.config.relation_id.name;
/* 18:   */     }
/* 19:31 */     if (name.equals("tr_text")) {
/* 20:32 */       return ((ConnectorField)this.config.text.get(0)).name;
/* 21:   */     }
/* 22:33 */     return super.name_data(name);
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.TreeDataProcessor
 * JD-Core Version:    0.7.0.1
 */