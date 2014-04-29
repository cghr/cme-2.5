/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ public class KeyGridBehavior
/*  4:   */   extends ConnectorBehavior
/*  5:   */ {
/*  6:   */   private GridConnector grid;
/*  7:   */   
/*  8:   */   public KeyGridBehavior(GridConnector grid)
/*  9:   */   {
/* 10: 7 */     this.grid = grid;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public void afterProcessing(DataAction action)
/* 14:   */   {
/* 15:12 */     super.afterProcessing(action);
/* 16:   */     
/* 17:14 */     String status = action.get_status();
/* 18:15 */     if ((status.equals("inserted")) || (status.equals("updated")))
/* 19:   */     {
/* 20:16 */       action.success(action.get_value(this.grid.config.id.name));
/* 21:17 */       action.set_status("inserted");
/* 22:   */     }
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void beforeProcessing(DataAction action)
/* 26:   */   {
/* 27:23 */     String idvalue = action.get_value(this.grid.config.id.name);
/* 28:24 */     if ((idvalue == null) || (idvalue.equals(""))) {
/* 29:25 */       action.error();
/* 30:   */     }
/* 31:27 */     super.beforeProcessing(action);
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.KeyGridBehavior
 * JD-Core Version:    0.7.0.1
 */