/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import java.sql.Statement;
/*  6:   */ 
/*  7:   */ public class PGSQLDBDataWrapper
/*  8:   */   extends DBDataWrapper
/*  9:   */ {
/* 10:   */   public String escape(String data)
/* 11:   */   {
/* 12:20 */     return data.replace("\\", "\\\\'").replace("'", "\\'");
/* 13:   */   }
/* 14:   */   
/* 15:   */   public String get_new_id(ConnectorResultSet result)
/* 16:   */     throws ConnectorOperationException
/* 17:   */   {
/* 18:30 */     return query("SELECT LASTVAL() AS dhx_id").get("dhx_id");
/* 19:   */   }
/* 20:   */   
/* 21:   */   protected String select_query(String select, String from, String where, String sort, String start, String count)
/* 22:   */   {
/* 23:40 */     String sql = "SELECT " + select + " FROM " + from;
/* 24:41 */     if (!where.equals("")) {
/* 25:41 */       sql = sql + " WHERE " + where;
/* 26:   */     }
/* 27:42 */     if (!sort.equals("")) {
/* 28:42 */       sql = sql + " ORDER BY " + sort;
/* 29:   */     }
/* 30:43 */     if ((!start.equals("")) || (!count.equals(""))) {
/* 31:43 */       sql = sql + " OFFSET " + start + " LIMIT " + count;
/* 32:   */     }
/* 33:44 */     return sql;
/* 34:   */   }
/* 35:   */   
/* 36:   */   protected Statement getStatement()
/* 37:   */     throws SQLException
/* 38:   */   {
/* 39:52 */     return get_connection().createStatement(1004, 1007);
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.PGSQLDBDataWrapper
 * JD-Core Version:    0.7.0.1
 */