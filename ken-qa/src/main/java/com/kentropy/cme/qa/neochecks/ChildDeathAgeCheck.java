/*   1:    */ package com.kentropy.cme.qa.neochecks;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.DbConnection;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.ResultSet;
/*   6:    */ import java.sql.SQLException;
/*   7:    */ import java.sql.Statement;
/*   8:    */ import java.util.Hashtable;
/*   9:    */ 
/*  10:    */ public class ChildDeathAgeCheck
/*  11:    */   implements QualityCheck
/*  12:    */ {
/*  13:    */   public boolean validate(ResultSet rs, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/*  14:    */   {
/*  15:    */     try
/*  16:    */     {
/*  17: 25 */       String deathAgeMonth = rs.getString("death_age_month");
/*  18: 26 */       String deathAgeYear = rs.getString("death_age_year");
/*  19: 27 */       String sql = "select * from " + domain + "_qa where uniqno='" + 
/*  20: 28 */         rs.getString("uniqno") + "'";
/*  21: 29 */       Connection con = DbConnection.getConnection();
/*  22: 30 */       Statement stmt = con.createStatement();
/*  23: 31 */       ResultSet rs_qa = stmt.executeQuery(sql);
/*  24: 32 */       rs_qa.next();
/*  25: 34 */       if (rs_qa.getString(name).startsWith("M"))
/*  26:    */       {
/*  27: 35 */         if (((deathAgeMonth == null) || (deathAgeMonth.trim().equals(""))) && 
/*  28: 36 */           (rs_qa.getString("death_age_year").startsWith("V"))) {
/*  29: 37 */           return false;
/*  30:    */         }
/*  31: 38 */         if (((deathAgeYear == null) || (deathAgeYear.trim().equals(""))) && 
/*  32: 39 */           (rs_qa.getString("death_age_month").startsWith("V"))) {
/*  33: 40 */           return false;
/*  34:    */         }
/*  35:    */       }
/*  36: 44 */       return true;
/*  37:    */     }
/*  38:    */     catch (SQLException e)
/*  39:    */     {
/*  40: 47 */       e.printStackTrace();
/*  41:    */     }
/*  42: 50 */     return false;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public boolean validate(Hashtable rs, Hashtable flags, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/*  46:    */   {
/*  47:    */     try
/*  48:    */     {
/*  49: 58 */       String deathAgeMonth = (String)rs.get("death_age_month");
/*  50: 59 */       String deathAgeYear = (String)rs.get("death_age_year");
/*  51: 61 */       if (flags.get(name).toString().startsWith("M"))
/*  52:    */       {
/*  53: 62 */         if (((deathAgeMonth == null) || (deathAgeMonth.trim().equals(""))) && 
/*  54: 63 */           (flags.get("death_age_year").toString().startsWith("V"))) {
/*  55: 64 */           return false;
/*  56:    */         }
/*  57: 65 */         if (((deathAgeYear == null) || (deathAgeYear.trim().equals(""))) && 
/*  58: 66 */           (flags.get("death_age_month").toString().startsWith("V"))) {
/*  59: 67 */           return false;
/*  60:    */         }
/*  61:    */       }
/*  62: 71 */       return true;
/*  63:    */     }
/*  64:    */     catch (Exception e)
/*  65:    */     {
/*  66: 74 */       e.printStackTrace();
/*  67:    */     }
/*  68: 77 */     return false;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public boolean validate(Hashtable rs, Hashtable flags, Hashtable checkDef, String name, String value, StringBuffer errorMsg, String domain)
/*  72:    */   {
/*  73:    */     try
/*  74:    */     {
/*  75: 86 */       String deathAgeMonth = (String)rs.get("death_age_month");
/*  76: 87 */       String deathAgeYear = (String)rs.get("death_age_year");
/*  77: 89 */       if (flags.get(name).toString().startsWith("M"))
/*  78:    */       {
/*  79: 90 */         if (((deathAgeMonth == null) || (deathAgeMonth.trim().equals(""))) && 
/*  80: 91 */           (flags.get("death_age_year").toString().startsWith("V"))) {
/*  81: 92 */           return false;
/*  82:    */         }
/*  83: 93 */         if (((deathAgeYear == null) || (deathAgeYear.trim().equals(""))) && 
/*  84: 94 */           (flags.get("death_age_month").toString().startsWith("V"))) {
/*  85: 95 */           return false;
/*  86:    */         }
/*  87:    */       }
/*  88: 99 */       return true;
/*  89:    */     }
/*  90:    */     catch (Exception e)
/*  91:    */     {
/*  92:102 */       e.printStackTrace();
/*  93:    */     }
/*  94:105 */     return false;
/*  95:    */   }
/*  96:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa\ken-qa.jar
 * Qualified Name:     com.kentropy.cme.qa.neochecks.ChildDeathAgeCheck
 * JD-Core Version:    0.7.0.1
 */