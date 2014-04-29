/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ public class MySQLDBDataWrapper
/*  4:   */   extends DBDataWrapper
/*  5:   */ {
/*  6:   */   public String escape(String data)
/*  7:   */   {
/*  8:17 */     return data.replace("\\", "\\\\'").replace("'", "\\'");
/*  9:   */   }
/* 10:   */   
/* 11:   */   public String get_new_id(ConnectorResultSet result)
/* 12:   */     throws ConnectorOperationException
/* 13:   */   {
/* 14:25 */     return query("SELECT LAST_INSERT_ID() as new_id").get("new_id");
/* 15:   */   }
/* 16:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.MySQLDBDataWrapper
 * JD-Core Version:    0.7.0.1
 */