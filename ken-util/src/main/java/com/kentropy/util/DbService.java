/*  1:   */ package com.kentropy.util;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.apache.log4j.Logger;
/*  6:   */ import org.springframework.jdbc.core.JdbcTemplate;
/*  7:   */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  8:   */ import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
/*  9:   */ 
/* 10:   */ public class DbService
/* 11:   */ {
/* 12:16 */   SpringUtils su = new SpringUtils();
/* 13:17 */   JdbcTemplate jt = this.su.getJdbcTemplate();
/* 14:18 */   private static Logger log = Logger.getLogger(DbService.class);
/* 15:20 */   SqlRowSet rs = null;
/* 16:   */   
/* 17:   */   public String uniqueResult(String sql)
/* 18:   */   {
/* 19:23 */     String result = null;
/* 20:   */     try
/* 21:   */     {
/* 22:26 */       this.rs = this.jt.queryForRowSet(sql);
/* 23:27 */       if (this.rs.next()) {
/* 24:28 */         result = this.rs.getString(1);
/* 25:   */       }
/* 26:   */     }
/* 27:   */     catch (Exception e)
/* 28:   */     {
/* 29:31 */       e.printStackTrace();
/* 30:   */     }
/* 31:35 */     return result;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public List<StringBuffer[]> queryResult(String sql)
/* 35:   */   {
/* 36:42 */     List<StringBuffer[]> result = new ArrayList();
/* 37:   */     try
/* 38:   */     {
/* 39:46 */       this.rs = this.jt.queryForRowSet(sql);
/* 40:   */       
/* 41:48 */       SqlRowSetMetaData rsmd = this.rs.getMetaData();
/* 42:49 */       int column_count = rsmd.getColumnCount();
/* 43:50 */       while (this.rs.next())
/* 44:   */       {
/* 45:52 */         StringBuffer[] rowArray = new StringBuffer[column_count];
/* 46:53 */         for (int i = 1; i <= column_count; i++) {
/* 47:58 */           rowArray[(i - 1)] = (this.rs.getString(i) == null ? new StringBuffer() : new StringBuffer(this.rs.getString(i)));
/* 48:   */         }
/* 49:61 */         result.add(rowArray);
/* 50:   */       }
/* 51:   */     }
/* 52:   */     catch (Exception e)
/* 53:   */     {
/* 54:65 */       e.printStackTrace();
/* 55:   */     }
/* 56:67 */     return result;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public int updateDatabase(String sql)
/* 60:   */   {
/* 61:74 */     int effect = 0;
/* 62:   */     try
/* 63:   */     {
/* 64:77 */       effect = this.jt.queryForInt(sql);
/* 65:   */     }
/* 66:   */     catch (Exception e)
/* 67:   */     {
/* 68:80 */       e.printStackTrace();
/* 69:   */     }
/* 70:83 */     return effect;
/* 71:   */   }
/* 72:   */   
/* 73:   */   public static void main(String[] args) {}
/* 74:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-util\ken-util.jar
 * Qualified Name:     com.kentropy.util.DbService
 * JD-Core Version:    0.7.0.1
 */