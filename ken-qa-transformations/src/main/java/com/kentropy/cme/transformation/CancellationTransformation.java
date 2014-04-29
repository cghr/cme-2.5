/*  1:   */ package com.kentropy.cme.transformation;
/*  2:   */ 
/*  3:   */ import com.kentropy.cme.qa.transformation.Transformation;
/*  4:   */ import com.kentropy.db.TestXUIDB;
/*  5:   */ import java.io.PrintStream;
/*  6:   */ import java.sql.ResultSet;
/*  7:   */ import java.util.Hashtable;
/*  8:   */ import net.xoetrope.xui.data.XBaseModel;
/*  9:   */ import net.xoetrope.xui.data.XModel;
/* 10:   */ 
/* 11:   */ public class CancellationTransformation
/* 12:   */   implements Transformation
/* 13:   */ {
/* 14:   */   public Object transform(ResultSet rs, String name, String value, StringBuffer errorMsg, String domain)
/* 15:   */     throws Exception
/* 16:   */   {
/* 17:17 */     return null;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public Object transform(Hashtable rs, String name, String value, StringBuffer errorMsg, String domain)
/* 21:   */   {
/* 22:22 */     System.out.println("Inside CancellationTransformation");
/* 23:23 */     String phys1 = (String)rs.get("phys1");
/* 24:24 */     String phys2 = (String)rs.get("phys2");
/* 25:25 */     String uniqno = (String)rs.get("uniqno");
/* 26:26 */     String cancelled = CMETransformation.getCancellation(phys1, phys2, uniqno);
/* 27:   */     
/* 28:28 */     String role = "coder";
/* 29:29 */     if (name.equals("adjudicator")) {
/* 30:30 */       role = "adjudicator";
/* 31:   */     }
/* 32:   */     try
/* 33:   */     {
/* 34:34 */       if (cancelled.equals("true")) {
/* 35:35 */         updateCancellationReport1(value, role, uniqno);
/* 36:   */       }
/* 37:   */     }
/* 38:   */     catch (Exception e)
/* 39:   */     {
/* 40:38 */       e.printStackTrace();
/* 41:   */     }
/* 42:41 */     return null;
/* 43:   */   }
/* 44:   */   
/* 45:   */   private void updateCancellationReport1(String phys1, String role, String uniqno)
/* 46:   */     throws Exception
/* 47:   */   {
/* 48:47 */     String path = "/cme/" + uniqno;
/* 49:   */     
/* 50:49 */     String reason1Path = path + "/Coding/" + phys1 + "/cancellation/reason";
/* 51:50 */     String comment1Path = path + "/Coding/" + phys1 + "/cancellation/comment";
/* 52:51 */     System.out.println("reason1Path" + reason1Path);
/* 53:   */     
/* 54:53 */     String reason1 = TestXUIDB.getInstance().getValue("keyvalue", reason1Path);
/* 55:54 */     String comment1 = TestXUIDB.getInstance().getValue("keyvalue", comment1Path);
/* 56:56 */     if ((reason1 != null) || (comment1 != null))
/* 57:   */     {
/* 58:57 */       XModel cancellationModel = new XBaseModel();
/* 59:58 */       cancellationModel.setId(uniqno);
/* 60:59 */       cancellationModel.set("uniqno", uniqno);
/* 61:60 */       cancellationModel.set("physician1", phys1);
/* 62:61 */       cancellationModel.set("reason1", reason1);
/* 63:62 */       cancellationModel.set("comments1", comment1);
/* 64:63 */       cancellationModel.set("role", role);
/* 65:   */       
/* 66:65 */       TestXUIDB.getInstance().saveDataM2("cancellation", "uniqno='" + uniqno + "' and physician1='" + phys1 + "'", cancellationModel);
/* 67:   */     }
/* 68:   */   }
/* 69:   */   
/* 70:   */   public static void main(String[] args) {}
/* 71:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa-transformations\ken-qa-transformations.jar
 * Qualified Name:     com.kentropy.cme.transformation.CancellationTransformation
 * JD-Core Version:    0.7.0.1
 */