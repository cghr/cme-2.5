/*   1:    */ package com.kentropy.cme.qa.neochecks;
/*   2:    */ 
/*   3:    */ import com.kentropy.cme.qa.Handler1;
/*   4:    */ import com.kentropy.util.DbConnection;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.sql.Connection;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import java.sql.Statement;
/*  10:    */ import java.util.Hashtable;
/*  11:    */ 
/*  12:    */ public class FlowErrorCleanParentMissing
/*  13:    */   implements ErrorCleaning
/*  14:    */ {
/*  15:    */   public boolean cleanField(ResultSet rs, String field, String value, StringBuffer errBuf, String domain)
/*  16:    */   {
/*  17: 26 */     String sql = "select * from corrections where name='" + field + "' and domain='" + domain + "'";
/*  18: 27 */     String sql2 = null;
/*  19: 28 */     Connection con = DbConnection.getConnection();
/*  20: 29 */     ResultSet rs1 = null;
/*  21: 30 */     ResultSet rs2 = null;
/*  22:    */     try
/*  23:    */     {
/*  24: 34 */       sql2 = "select * from " + domain + "_qa where uniqno='" + rs.getString("uniqno") + "'";
/*  25: 35 */       Statement stmt = con.createStatement();
/*  26: 36 */       rs1 = stmt.executeQuery(sql);
/*  27:    */       
/*  28:    */ 
/*  29: 39 */       String uniqno = rs.getString("uniqno");
/*  30: 41 */       if (rs1.next())
/*  31:    */       {
/*  32: 42 */         String correctionField = rs1.getString("fields");
/*  33:    */         
/*  34:    */ 
/*  35:    */ 
/*  36: 46 */         String oldValue = rs.getString(rs1.getString("name"));
/*  37: 47 */         String correctionValues = rs1.getString("source_value");
/*  38: 48 */         boolean isCorrectable = false;
/*  39: 49 */         if (correctionValues.contains(","))
/*  40:    */         {
/*  41: 50 */           String[] values = correctionValues.split(",");
/*  42: 51 */           for (int i = 0; i < values.length; i++) {
/*  43: 52 */             if (oldValue.equals(values[i]))
/*  44:    */             {
/*  45: 53 */               isCorrectable = true;
/*  46: 54 */               break;
/*  47:    */             }
/*  48:    */           }
/*  49:    */         }
/*  50: 58 */         else if (correctionValues.contains("-"))
/*  51:    */         {
/*  52: 59 */           String[] values = correctionValues.split("-");
/*  53: 60 */           for (int i = 0; i < values.length; i++)
/*  54:    */           {
/*  55: 61 */             int min = Integer.parseInt(values[0]);
/*  56: 62 */             int max = Integer.parseInt(values[1]);
/*  57: 63 */             if ((oldValue.equals("")) || (oldValue == null))
/*  58:    */             {
/*  59: 64 */               isCorrectable = false;
/*  60:    */             }
/*  61:    */             else
/*  62:    */             {
/*  63: 66 */               int oldValueInt = Integer.parseInt(oldValue);
/*  64: 67 */               if ((oldValueInt >= min) && (oldValueInt <= max))
/*  65:    */               {
/*  66: 68 */                 isCorrectable = true;
/*  67: 69 */                 break;
/*  68:    */               }
/*  69:    */             }
/*  70:    */           }
/*  71:    */         }
/*  72: 74 */         else if (oldValue.equals(correctionValues))
/*  73:    */         {
/*  74: 75 */           isCorrectable = true;
/*  75:    */         }
/*  76: 78 */         if (isCorrectable)
/*  77:    */         {
/*  78: 79 */           String newValue = rs1.getString("values");
/*  79: 80 */           Handler1 h = null;
/*  80: 81 */           h = new Handler1(null, null);
/*  81: 82 */           h.insertInQaHistory(uniqno, correctionField, oldValue, newValue, domain, "auto");
/*  82:    */           
/*  83: 84 */           h.updateField(domain, uniqno, rs1.getString("fields"), newValue);
/*  84: 85 */           errBuf.append(uniqno + " " + field + " " + value + ": Correcting " + rs1.getString("fields") + " to " + newValue + "\n");
/*  85: 86 */           System.out.println("successfully updated");
/*  86:    */         }
/*  87: 88 */         return true;
/*  88:    */       }
/*  89:    */     }
/*  90:    */     catch (SQLException e)
/*  91:    */     {
/*  92: 93 */       e.printStackTrace();
/*  93:    */     }
/*  94: 95 */     return false;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public boolean cleanField(Hashtable rs, Hashtable flags, Hashtable newValues, String field, String value, StringBuffer errBuf, String domain)
/*  98:    */   {
/*  99:102 */     return false;
/* 100:    */   }
/* 101:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa\ken-qa.jar
 * Qualified Name:     com.kentropy.cme.qa.neochecks.FlowErrorCleanParentMissing
 * JD-Core Version:    0.7.0.1
 */