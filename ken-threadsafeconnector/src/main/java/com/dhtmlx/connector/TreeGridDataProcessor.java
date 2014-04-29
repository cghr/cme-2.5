/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ public class TreeGridDataProcessor
/*  4:   */   extends GridDataProcessor
/*  5:   */ {
/*  6:   */   public TreeGridDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory)
/*  7:   */   {
/*  8:20 */     super(connector, config, request, cfactory);
/*  9:21 */     request.set_relation("");
/* 10:   */   }
/* 11:   */   
/* 12:   */   public String name_data(String name)
/* 13:   */   {
/* 14:29 */     if (name.equals("gr_pid")) {
/* 15:30 */       return this.config.relation_id.name;
/* 16:   */     }
/* 17:31 */     return super.name_data(name);
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.TreeGridDataProcessor
 * JD-Core Version:    0.7.0.1
 */