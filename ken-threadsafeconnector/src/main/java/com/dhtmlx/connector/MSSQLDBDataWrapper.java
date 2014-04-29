/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import java.sql.Statement;
/*  6:   */ 
/*  7:   */ public class MSSQLDBDataWrapper
/*  8:   */   extends DBDataWrapper
/*  9:   */ {
/* 10:   */   private int start_from;
/* 11:   */   
/* 12:   */   public String escape(String data)
/* 13:   */   {
/* 14:23 */     return data.replace("\\", "\\\\'").replace("'", "''");
/* 15:   */   }
/* 16:   */   
/* 17:   */   public String get_new_id(ConnectorResultSet result)
/* 18:   */     throws ConnectorOperationException
/* 19:   */   {
/* 20:33 */     return query("SELECT @@IDENTITY AS dhx_id").get("dhx_id");
/* 21:   */   }
/* 22:   */   
/* 23:   */   protected String select_query(String select, String from, String where, String sort, String start, String count)
/* 24:   */   {
/* 25:42 */     if (count.equals("")) {
/* 26:43 */       return super.select_query(select, from, where, sort, start, count);
/* 27:   */     }
/* 28:45 */     String sql = "SELECT ";
/* 29:46 */     if (!count.equals("")) {
/* 30:47 */       sql = sql + " TOP " + Integer.toString(Integer.parseInt(count) + Integer.parseInt(start));
/* 31:   */     }
/* 32:48 */     sql = sql + " " + select + " FROM " + from;
/* 33:49 */     if (!where.equals("")) {
/* 34:49 */       sql = sql + " WHERE " + where;
/* 35:   */     }
/* 36:50 */     if (!sort.equals("")) {
/* 37:50 */       sql = sql + " ORDER BY " + sort;
/* 38:   */     }
/* 39:52 */     if (!start.equals("")) {
/* 40:53 */       this.start_from = Integer.parseInt(start);
/* 41:   */     } else {
/* 42:55 */       this.start_from = 0;
/* 43:   */     }
/* 44:57 */     return sql;
/* 45:   */   }
/* 46:   */   
/* 47:   */   protected Statement getStatement()
/* 48:   */     throws SQLException
/* 49:   */   {
/* 50:65 */     return get_connection().createStatement(1004, 1007);
/* 51:   */   }
/* 52:   */   
/* 53:   */   public ConnectorResultSet select(DataRequest source)
/* 54:   */     throws ConnectorOperationException
/* 55:   */   {
/* 56:74 */     ConnectorResultSet temp = super.select(source);
/* 57:75 */     temp.jump_to(this.start_from);
/* 58:76 */     return temp;
/* 59:   */   }
/* 60:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.MSSQLDBDataWrapper
 * JD-Core Version:    0.7.0.1
 */