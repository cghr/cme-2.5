/*  1:   */ package com.kentropy.data;
/*  2:   */ 
/*  3:   */ import com.kentropy.db.TestXUIDB;
/*  4:   */ import net.xoetrope.xui.data.XBaseModel;
/*  5:   */ import net.xoetrope.xui.data.XModel;
/*  6:   */ 
/*  7:   */ public class ISTPDataHandler
/*  8:   */   implements DataHandler
/*  9:   */ {
/* 10:   */   public XModel getData(String type, String subtype, XModel context, String fields)
/* 11:   */     throws Exception
/* 12:   */   {
/* 13:13 */     XModel clinicM = (XModel)context.get("clinic");
/* 14:   */     
/* 15:15 */     String where = "ictc_code='" + clinicM.get() + "'";
/* 16:16 */     String table = "clinic";
/* 17:17 */     String idField = "ictc_code";
/* 18:18 */     XModel stdM = (XModel)context.get("std");
/* 19:19 */     if ((type.equals("std")) && (stdM != null) && (stdM.get() != null) && (!stdM.equals("")))
/* 20:   */     {
/* 21:21 */       where = "uniqno='" + stdM.get() + "'";
/* 22:22 */       table = "patients";
/* 23:23 */       idField = "uniqno";
/* 24:   */     }
/* 25:25 */     XModel details = new XBaseModel();
/* 26:26 */     return TestXUIDB.getInstance().getEnumData1(table, where, details, fields, idField);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public XModel getChildren(String type, String subtype, XModel context, String fields, String constraints)
/* 30:   */     throws Exception
/* 31:   */   {
/* 32:32 */     XModel clinicM = (XModel)context.get("clinic");
/* 33:33 */     String where = "";
/* 34:34 */     String table = "clinic";
/* 35:35 */     String idField = "ictc_code";
/* 36:36 */     if (type.equals("std"))
/* 37:   */     {
/* 38:38 */       where = "ictc_code='" + clinicM.get() + "'";
/* 39:39 */       table = "patients";
/* 40:40 */       idField = "uniqno";
/* 41:   */     }
/* 42:42 */     XModel details = new XBaseModel();
/* 43:43 */     return TestXUIDB.getInstance().getEnumData1(table, where + (constraints != null ? " and " + constraints : ""), details, fields, idField);
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void saveData(String type, String subtype, XModel context, XModel dataM)
/* 47:   */     throws Exception
/* 48:   */   {
/* 49:49 */     XModel clinicM = (XModel)context.get("clinic");
/* 50:50 */     XModel stdM = (XModel)context.get("std");
/* 51:51 */     String where = "uniqno='" + stdM.get() + "'";
/* 52:52 */     String table = "patients";
/* 53:53 */     String idField = "uniqno";
/* 54:   */     
/* 55:55 */     ((XModel)dataM.get("uniqno")).set(stdM.get());
/* 56:56 */     TestXUIDB.getInstance().saveDataM1(table, where, dataM);
/* 57:   */   }
/* 58:   */   
/* 59:   */   public static void main(String[] args) {}
/* 60:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.data.ISTPDataHandler
 * JD-Core Version:    0.7.0.1
 */