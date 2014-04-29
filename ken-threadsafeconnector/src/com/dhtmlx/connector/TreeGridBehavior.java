/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ 
/*  5:   */ public class TreeGridBehavior
/*  6:   */   extends ConnectorBehavior
/*  7:   */ {
/*  8:15 */   private HashMap<String, String> id_swap = new HashMap();
/*  9:   */   private DataConfig config;
/* 10:   */   
/* 11:   */   public TreeGridBehavior(DataConfig config)
/* 12:   */   {
/* 13:26 */     this.config = config;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void afterInsert(DataAction action)
/* 17:   */   {
/* 18:34 */     this.id_swap.put(action.get_id(), action.get_new_id());
/* 19:35 */     super.afterInsert(action);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void beforeProcessing(DataAction action)
/* 23:   */   {
/* 24:43 */     String key = (String)this.id_swap.get(action.get_value(this.config.relation_id.name));
/* 25:44 */     if (key != null) {
/* 26:45 */       action.set_value(this.config.relation_id.name, key);
/* 27:   */     }
/* 28:46 */     super.beforeProcessing(action);
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.TreeGridBehavior
 * JD-Core Version:    0.7.0.1
 */