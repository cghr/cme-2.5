/*   1:    */ package com.kentropy.cme.transformation;
/*   2:    */ 
/*   3:    */ import com.kentropy.cme.qa.transformation.Transformation;
/*   4:    */ import com.kentropy.db.TestXUIDB;
/*   5:    */ import com.kentropy.util.DbUtil;
/*   6:    */ import java.sql.PreparedStatement;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.text.SimpleDateFormat;
/*   9:    */ import java.util.Date;
/*  10:    */ import java.util.Hashtable;
/*  11:    */ import net.xoetrope.optional.data.sql.CachedDatabaseTable;
/*  12:    */ import net.xoetrope.optional.data.sql.DatabaseTableModel;
/*  13:    */ import net.xoetrope.xui.data.XBaseModel;
/*  14:    */ import net.xoetrope.xui.data.XModel;
/*  15:    */ 
/*  16:    */ public class BillableTransformation
/*  17:    */   implements Transformation
/*  18:    */ {
/*  19:    */   public Object transform(ResultSet rs, String name, String value, StringBuffer errorMsg, String domain)
/*  20:    */     throws Exception
/*  21:    */   {
/*  22: 23 */     return null;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public Object transform(Hashtable rs, String name, String value, StringBuffer errorMsg, String domain)
/*  26:    */   {
/*  27: 28 */     String phys1 = (String)rs.get("phys1");
/*  28: 29 */     String phys2 = (String)rs.get("phys2");
/*  29: 30 */     String adjudicator = (String)rs.get("adjudicator");
/*  30: 31 */     String uniqno = (String)rs.get("uniqno");
/*  31: 32 */     String cancellation = CMETransformation.getCancellation(phys1, phys2, uniqno);
/*  32: 34 */     if (cancellation.equals("true")) {
/*  33: 35 */       return null;
/*  34:    */     }
/*  35: 38 */     String role = "coder";
/*  36:    */     try
/*  37:    */     {
/*  38: 40 */       if ((value != null) && (!value.equals("")))
/*  39:    */       {
/*  40: 41 */         if (name.equals("adjudicator")) {
/*  41: 42 */           role = "adjudicator";
/*  42:    */         }
/*  43: 44 */         uniqno = rs.get("uniqno").toString();
/*  44: 45 */         updateBillable1(value, uniqno, role);
/*  45:    */       }
/*  46:    */     }
/*  47:    */     catch (Exception e)
/*  48:    */     {
/*  49: 48 */       e.printStackTrace();
/*  50:    */     }
/*  51: 51 */     return null;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void updateBillable(String phys, String report, String role)
/*  55:    */     throws Exception
/*  56:    */   {
/*  57: 56 */     if (role.equals("adjudicator")) {
/*  58: 57 */       role = "adjudication";
/*  59:    */     } else {
/*  60: 59 */       role = "coding";
/*  61:    */     }
/*  62: 61 */     if ((phys != null) && (!phys.trim().equals("")))
/*  63:    */     {
/*  64: 62 */       String sql = "insert into billables (physician_id,report_id,date_of_billable,role) VALUES('" + phys + "','" + report + "',NOW(),'" + role + "')";
/*  65: 63 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  66: 64 */       dt.setupTable("billables", "*", "", "test", false);
/*  67: 65 */       PreparedStatement ps = dt.getTable().getPreparedStatement(sql);
/*  68: 66 */       ps.execute();
/*  69: 67 */       ps.close();
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void updateBillable1(String phys, String report, String role)
/*  74:    */     throws Exception
/*  75:    */   {
/*  76: 73 */     if (role.equals("adjudicator")) {
/*  77: 74 */       role = "adjudication";
/*  78:    */     } else {
/*  79: 76 */       role = "coding";
/*  80:    */     }
/*  81: 79 */     DbUtil db = new DbUtil();
/*  82: 80 */     String icd = db.uniqueResult("cme_report", "icd", "uniqno='" + report + "' AND physician='" + phys + "'");
/*  83: 83 */     if ((phys != null) && (!phys.trim().equals("")) && (!icd.equals("null")))
/*  84:    */     {
/*  85: 84 */       String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
/*  86:    */       
/*  87: 86 */       String fromBookmark = TestXUIDB.getInstance().getLastChangeLog();
/*  88:    */       
/*  89: 88 */       XModel xm = new XBaseModel();
/*  90: 89 */       xm.set("physician_id", phys);
/*  91: 90 */       xm.set("report_id", report);
/*  92: 91 */       xm.set("date_of_billable", time);
/*  93: 92 */       xm.set("role", role);
/*  94: 93 */       TestXUIDB.getInstance().saveDataM1("billables", "report_id='" + report + "' and physician_id=" + phys, xm);
/*  95: 94 */       String toBookmark = TestXUIDB.getInstance().getLastChangeLog();
/*  96: 95 */       TestXUIDB.getInstance().addToChangeLogOutboundQueue("cmebilling", fromBookmark, toBookmark);
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static void main(String[] args)
/* 101:    */   {
/* 102:101 */     Hashtable ht = new Hashtable();
/* 103:102 */     ht.put("uniqno", "14300107_01_01");
/* 104:103 */     ht.put("phys1", "20");
/* 105:104 */     ht.put("phys2", "21");
/* 106:    */     
/* 107:106 */     Transformation transformation = new BillableTransformation();
/* 108:107 */     transformation.transform(ht, "phys1", "20", null, null);
/* 109:    */   }
/* 110:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa-transformations\ken-qa-transformations.jar
 * Qualified Name:     com.kentropy.cme.transformation.BillableTransformation
 * JD-Core Version:    0.7.0.1
 */