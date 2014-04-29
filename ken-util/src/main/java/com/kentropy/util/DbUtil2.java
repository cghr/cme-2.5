/*  1:   */ package com.kentropy.util;
/*  2:   */ 
/*  3:   */ import org.springframework.jdbc.core.JdbcTemplate;
/*  4:   */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  5:   */ import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
/*  6:   */ 
/*  7:   */ public class DbUtil2
/*  8:   */   extends DbUtil
/*  9:   */ {
/* 10:   */   public StringBuffer getDataAsJSArrayTranspose1(String sql, Object[] args, String headings)
/* 11:   */   {
/* 12:10 */     StringBuffer js = new StringBuffer();
/* 13:   */     
/* 14:12 */     SqlRowSet rs = this.jt.queryForRowSet(sql, args);
/* 15:13 */     SqlRowSetMetaData rsmd = rs.getMetaData();
/* 16:   */     
/* 17:15 */     int cols = rsmd.getColumnCount();
/* 18:16 */     int rows = 0;
/* 19:   */     
/* 20:18 */     js.append(headings + ",");
/* 21:20 */     while (rs.next())
/* 22:   */     {
/* 23:22 */       for (int i = 1; i <= cols; i++) {
/* 24:23 */         js.append("['" + rsmd.getColumnLabel(i) + "','" + rs.getString(i) + "'],");
/* 25:   */       }
/* 26:26 */       js.deleteCharAt(js.length() - 1);
/* 27:   */       
/* 28:28 */       rows++;
/* 29:   */     }
/* 30:31 */     return js;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public static void main(String[] args) {}
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-util\ken-util.jar
 * Qualified Name:     com.kentropy.util.DbUtil2
 * JD-Core Version:    0.7.0.1
 */