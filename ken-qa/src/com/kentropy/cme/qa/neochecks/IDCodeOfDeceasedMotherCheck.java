/*  1:   */ package com.kentropy.cme.qa.neochecks;
/*  2:   */ 
/*  3:   */ import java.sql.ResultSet;
/*  4:   */ import java.util.Hashtable;
/*  5:   */ 
/*  6:   */ public class IDCodeOfDeceasedMotherCheck
/*  7:   */   implements QualityCheck
/*  8:   */ {
/*  9: 8 */   ResultSet rs = null;
/* 10: 9 */   String name = null;
/* 11:10 */   String value = null;
/* 12:   */   
/* 13:   */   public boolean validate(ResultSet rs, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 14:   */   {
/* 15:15 */     this.rs = rs;
/* 16:16 */     this.name = name;
/* 17:17 */     this.value = value;
/* 18:   */     try
/* 19:   */     {
/* 20:20 */       if (rs.getString("source").equals("1")) {
/* 21:22 */         if (!rangeCheck()) {
/* 22:23 */           return false;
/* 23:   */         }
/* 24:   */       }
/* 25:   */     }
/* 26:   */     catch (Exception e)
/* 27:   */     {
/* 28:28 */       e.printStackTrace();
/* 29:   */     }
/* 30:30 */     return true;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public boolean rangeCheck()
/* 34:   */   {
/* 35:34 */     if ((this.value != null) && (this.value.length() >= 9)) {
/* 36:36 */       return true;
/* 37:   */     }
/* 38:38 */     return false;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public static void main(String[] args) {}
/* 42:   */   
/* 43:   */   public boolean validate(Hashtable rs, Hashtable flags, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 44:   */   {
/* 45:52 */     return false;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public boolean validate(Hashtable rs, Hashtable flags, Hashtable checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 49:   */   {
/* 50:57 */     return false;
/* 51:   */   }
/* 52:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa\ken-qa.jar
 * Qualified Name:     com.kentropy.cme.qa.neochecks.IDCodeOfDeceasedMotherCheck
 * JD-Core Version:    0.7.0.1
 */