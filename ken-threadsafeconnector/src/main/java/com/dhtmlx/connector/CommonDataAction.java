/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ 
/*  5:   */ public class CommonDataAction
/*  6:   */   extends DataAction
/*  7:   */ {
/*  8:   */   public CommonDataAction(String status, String id, HashMap<String, String> data)
/*  9:   */   {
/* 10: 9 */     super(status, id, data);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public String to_xml()
/* 14:   */   {
/* 15:14 */     if ((get_status() == "error") || (get_status() == "invalid")) {
/* 16:15 */       return "false";
/* 17:   */     }
/* 18:17 */     return "true" + (get_status().equals("insert") ? "\n" + get_new_id() : "");
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.CommonDataAction
 * JD-Core Version:    0.7.0.1
 */