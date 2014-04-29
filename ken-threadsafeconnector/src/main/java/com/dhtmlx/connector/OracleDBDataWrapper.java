/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.sql.CallableStatement;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.sql.Statement;
/*   7:    */ import java.util.regex.Matcher;
/*   8:    */ import java.util.regex.Pattern;
/*   9:    */ 
/*  10:    */ public class OracleDBDataWrapper
/*  11:    */   extends DBDataWrapper
/*  12:    */ {
/*  13:    */   public String escape(String data)
/*  14:    */   {
/*  15: 24 */     return data.replace("\\", "\\\\'").replace("'", "\\'");
/*  16:    */   }
/*  17:    */   
/*  18:    */   protected Statement getStatement()
/*  19:    */     throws SQLException
/*  20:    */   {
/*  21: 32 */     return get_connection().createStatement(1004, 1007);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public String insert_query(String data)
/*  25:    */     throws ConnectorOperationException
/*  26:    */   {
/*  27:    */     try
/*  28:    */     {
/*  29: 50 */       CallableStatement cs = get_connection().prepareCall(data);
/*  30: 51 */       cs.registerOutParameter(1, 4);
/*  31: 52 */       cs.execute();
/*  32:    */       
/*  33: 54 */       return Integer.toString(cs.getInt(1));
/*  34:    */     }
/*  35:    */     catch (SQLException e)
/*  36:    */     {
/*  37: 57 */       throw new ConnectorOperationException("Invalid SQL: " + data + "\n" + e.getMessage());
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void insert(DataAction data, DataRequest source)
/*  42:    */     throws ConnectorOperationException
/*  43:    */   {
/*  44: 66 */     String sql = insert_query(data, source);
/*  45: 67 */     data.success(insert_query(sql));
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected String insert_query(DataAction data, DataRequest source)
/*  49:    */   {
/*  50: 76 */     return "BEGIN " + super.insert_query(data, source) + " returning " + this.config.id.db_name + " into ?; END;";
/*  51:    */   }
/*  52:    */   
/*  53:    */   public String get_new_id(ConnectorResultSet result)
/*  54:    */     throws ConnectorOperationException
/*  55:    */   {
/*  56: 85 */     if (!this.sequence_name.equals("")) {
/*  57: 86 */       return query("SELECT " + Pattern.compile("nextval", 2).matcher(this.sequence_name).replaceAll("CURRVAL") + " as dhx_id FROM DUAL").get("dhx_id");
/*  58:    */     }
/*  59: 87 */     return null;
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected String select_query(String select, String from, String where, String sort, String start, String count)
/*  63:    */   {
/*  64: 96 */     String sql = "SELECT " + select + " FROM " + from;
/*  65: 97 */     if (!where.equals("")) {
/*  66: 97 */       sql = sql + " WHERE " + where;
/*  67:    */     }
/*  68: 98 */     if (!sort.equals("")) {
/*  69: 98 */       sql = sql + " ORDER BY " + sort;
/*  70:    */     }
/*  71: 99 */     if ((!start.equals("")) || (!count.equals("")))
/*  72:    */     {
/*  73:100 */       String end = Integer.toString(Integer.parseInt(count) + Integer.parseInt(start));
/*  74:101 */       sql = "SELECT * FROM ( select /*+ FIRST_ROWS(" + count + ")*/dhx_table.*, ROWNUM rnum FROM (" + sql + ") dhx_table where ROWNUM <= " + end + " ) where rnum >" + start;
/*  75:    */     }
/*  76:103 */     return sql;
/*  77:    */   }
/*  78:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.OracleDBDataWrapper
 * JD-Core Version:    0.7.0.1
 */