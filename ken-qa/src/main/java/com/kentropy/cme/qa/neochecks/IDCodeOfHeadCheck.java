/*  1:   */ package com.kentropy.cme.qa.neochecks;
/*  2:   */ 
/*  3:   */ import com.kentropy.util.DbConnection;
/*  4:   */ import java.sql.Connection;
/*  5:   */ import java.sql.ResultSet;
/*  6:   */ import java.sql.Statement;
/*  7:   */ import java.util.Hashtable;
/*  8:   */ 
/*  9:   */ public class IDCodeOfHeadCheck
/* 10:   */   implements QualityCheck
/* 11:   */ {
/* 12:11 */   ResultSet rs = null;
/* 13:12 */   String name = null;
/* 14:13 */   String value = null;
/* 15:   */   
/* 16:   */   public boolean validate(ResultSet rs, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 17:   */   {
/* 18:19 */     this.rs = rs;
/* 19:20 */     this.name = name;
/* 20:21 */     this.value = value;
/* 21:   */     try
/* 22:   */     {
/* 23:24 */       if (rs.getString("source").equals("1")) {
/* 24:26 */         if (!rangeCheck()) {
/* 25:27 */           return false;
/* 26:   */         }
/* 27:   */       }
/* 28:   */     }
/* 29:   */     catch (Exception e)
/* 30:   */     {
/* 31:32 */       e.printStackTrace();
/* 32:   */     }
/* 33:34 */     return true;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public boolean rangeCheck()
/* 37:   */     throws Exception
/* 38:   */   {
/* 39:38 */     if ((this.value != null) && (this.value.length() >= 9)) {
/* 40:40 */       return true;
/* 41:   */     }
/* 42:42 */     return false;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public static void main(String[] arg)
/* 46:   */     throws Exception
/* 47:   */   {
/* 48:48 */     IDCodeOfHeadCheck ichc = new IDCodeOfHeadCheck();
/* 49:49 */     Connection c = DbConnection.getConnection();
/* 50:   */     
/* 51:   */ 
/* 52:52 */     Statement s = c.createStatement();
/* 53:53 */     ResultSet rs = s.executeQuery("SELECT * from neonatal");
/* 54:54 */     rs.next();
/* 55:   */     
/* 56:56 */     rs.close();
/* 57:57 */     c.close();
/* 58:   */   }
/* 59:   */   
/* 60:   */   public boolean validate(Hashtable rs, Hashtable flags, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 61:   */   {
/* 62:62 */     return false;
/* 63:   */   }
/* 64:   */   
/* 65:   */   public boolean validate(Hashtable rs, Hashtable flags, Hashtable checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 66:   */   {
/* 67:67 */     return false;
/* 68:   */   }
/* 69:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa\ken-qa.jar
 * Qualified Name:     com.kentropy.cme.qa.neochecks.IDCodeOfHeadCheck
 * JD-Core Version:    0.7.0.1
 */