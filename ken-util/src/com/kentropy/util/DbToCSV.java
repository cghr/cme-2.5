/*   1:    */ package com.kentropy.util;
/*   2:    */ 
/*   3:    */ import org.apache.log4j.Logger;
/*   4:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*   5:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*   6:    */ import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
/*   7:    */ 
/*   8:    */ public class DbToCSV
/*   9:    */ {
/*  10: 11 */   JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  11: 12 */   Logger log = Logger.getLogger(DbToCSV.class);
/*  12:    */   
/*  13:    */   public boolean createCSVForTable(String table, String pathToCSV)
/*  14:    */   {
/*  15: 17 */     String sql1 = "DESC " + table;
/*  16: 18 */     SqlRowSet rs = this.jt.queryForRowSet(sql1);
/*  17: 19 */     StringBuffer headings = new StringBuffer();
/*  18: 21 */     while (rs.next()) {
/*  19: 23 */       headings.append("'" + rs.getString(1) + "',");
/*  20:    */     }
/*  21: 27 */     headings.deleteCharAt(headings.length() - 1);
/*  22: 28 */     String sql = "SELECT " + headings + " UNION SELECT * INTO OUTFILE '" + pathToCSV + "' FIELDS TERMINATED BY ',' ENCLOSED BY '\"'  LINES TERMINATED BY '\n' from " + table;
/*  23: 29 */     this.jt.execute(sql);
/*  24:    */     
/*  25:    */ 
/*  26:    */ 
/*  27: 33 */     return true;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public boolean createCSVForSql(String table, String cols, String where, Object[] args, String pathToCSV)
/*  31:    */   {
/*  32: 38 */     String sql1 = "";
/*  33: 40 */     if (where != null) {
/*  34: 41 */       sql1 = "SELECT " + cols + " FROM " + table + " WHERE " + where;
/*  35:    */     } else {
/*  36: 43 */       sql1 = "SELECT " + cols + " FROM " + table;
/*  37:    */     }
/*  38: 45 */     this.log.debug("create csv from sql " + sql1);
/*  39:    */     
/*  40:    */ 
/*  41: 48 */     SqlRowSet rs = this.jt.queryForRowSet(sql1, args);
/*  42: 49 */     SqlRowSetMetaData rsmd = rs.getMetaData();
/*  43: 50 */     StringBuffer headings = new StringBuffer();
/*  44: 52 */     for (int i = 1; i <= rsmd.getColumnCount(); i++) {
/*  45: 54 */       headings.append("'" + rsmd.getColumnLabel(i) + "',");
/*  46:    */     }
/*  47: 58 */     headings.deleteCharAt(headings.length() - 1);
/*  48:    */     
/*  49: 60 */     String sql = "";
/*  50: 62 */     if (where != null) {
/*  51: 63 */       sql = "SELECT " + headings + " UNION SELECT " + cols + " INTO OUTFILE '" + pathToCSV + "' FIELDS TERMINATED BY ',' ENCLOSED BY '\"'  LINES TERMINATED BY '\n' from " + table + " WHERE " + where;
/*  52:    */     } else {
/*  53: 65 */       sql = "SELECT " + headings + " UNION SELECT " + cols + " INTO OUTFILE '" + pathToCSV + "' FIELDS TERMINATED BY ',' ENCLOSED BY '\"'  LINES TERMINATED BY '\n' from " + table;
/*  54:    */     }
/*  55: 68 */     this.jt.execute(sql);
/*  56:    */     
/*  57: 70 */     return true;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public boolean createCSVForSqlWithOutQuotes(String table, String cols, String where, Object[] args, String pathToCSV)
/*  61:    */   {
/*  62: 75 */     String sql1 = "";
/*  63: 77 */     if (where != null) {
/*  64: 78 */       sql1 = "SELECT " + cols + " FROM " + table + " WHERE " + where;
/*  65:    */     } else {
/*  66: 80 */       sql1 = "SELECT " + cols + " FROM " + table;
/*  67:    */     }
/*  68: 82 */     this.log.debug("create csv from sql " + sql1);
/*  69:    */     
/*  70:    */ 
/*  71: 85 */     SqlRowSet rs = this.jt.queryForRowSet(sql1, args);
/*  72: 86 */     SqlRowSetMetaData rsmd = rs.getMetaData();
/*  73: 87 */     StringBuffer headings = new StringBuffer();
/*  74: 89 */     for (int i = 1; i <= rsmd.getColumnCount(); i++) {
/*  75: 91 */       headings.append("'" + rsmd.getColumnLabel(i) + "',");
/*  76:    */     }
/*  77: 95 */     headings.deleteCharAt(headings.length() - 1);
/*  78:    */     
/*  79: 97 */     String sql = "";
/*  80: 99 */     if (where != null) {
/*  81:100 */       sql = "SELECT " + headings + " UNION SELECT " + cols + " INTO OUTFILE '" + pathToCSV + "' FIELDS TERMINATED BY ','  LINES TERMINATED BY '\n' from " + table + " WHERE " + where;
/*  82:    */     } else {
/*  83:102 */       sql = "SELECT " + headings + " UNION SELECT " + cols + " INTO OUTFILE '" + pathToCSV + "' FIELDS TERMINATED BY ','   LINES TERMINATED BY '\n' from " + table;
/*  84:    */     }
/*  85:105 */     this.jt.execute(sql);
/*  86:    */     
/*  87:107 */     return true;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public static void main(String[] args)
/*  91:    */   {
/*  92:114 */     new DbToCSV().createCSVForTable("keyvalue", "/home/navaneeth/csv/keyvalue.csv");
/*  93:    */   }
/*  94:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-util\ken-util.jar
 * Qualified Name:     com.kentropy.util.DbToCSV
 * JD-Core Version:    0.7.0.1
 */