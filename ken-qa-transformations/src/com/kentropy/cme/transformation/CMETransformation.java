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
/* 11:   */ public class CMETransformation
/* 12:   */   implements Transformation
/* 13:   */ {
/* 14:   */   public static Transformation getTransformation(String table, String field, String outputTable, String outputField)
/* 15:   */   {
/* 16:15 */     XModel transformationModel = new XBaseModel();
/* 17:16 */     TestXUIDB.getInstance().getData("transformations", "transformation_class", "table1='" + table + "' AND field1='" + field + "' AND output_table='" + outputTable + "' AND output_field='" + outputField + "'", transformationModel);
/* 18:17 */     String className = ((XModel)transformationModel.get(0).get("transformation_class")).get().toString();
/* 19:   */     try
/* 20:   */     {
/* 21:19 */       return (Transformation)Class.forName(className).newInstance();
/* 22:   */     }
/* 23:   */     catch (InstantiationException e)
/* 24:   */     {
/* 25:23 */       e.printStackTrace();
/* 26:   */     }
/* 27:   */     catch (IllegalAccessException e)
/* 28:   */     {
/* 29:26 */       e.printStackTrace();
/* 30:   */     }
/* 31:   */     catch (ClassNotFoundException e)
/* 32:   */     {
/* 33:29 */       e.printStackTrace();
/* 34:   */     }
/* 35:31 */     return null;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public static String getCancellation(String phys1, String phys2, String uniqno)
/* 39:   */   {
/* 40:35 */     XModel xm = new XBaseModel();
/* 41:36 */     String path = "/cme/" + uniqno + "/Coding";
/* 42:37 */     TestXUIDB.getInstance().getKeyValues(xm, "keyvalue", path);
/* 43:38 */     String firstCancel = (String)((XModel)xm.get(phys1 + "/cancellation/reason")).get();
/* 44:39 */     String secondCancel = (String)((XModel)xm.get(phys2 + "/cancellation/reason")).get();
/* 45:40 */     if ((firstCancel != null) || (secondCancel != null)) {
/* 46:41 */       return "true";
/* 47:   */     }
/* 48:43 */     return "false";
/* 49:   */   }
/* 50:   */   
/* 51:   */   public static void main(String[] args)
/* 52:   */   {
/* 53:48 */     Transformation transformation = getTransformation("keyvalue", "*", "cme_report", "*");
/* 54:49 */     System.out.println("CMETransformation:" + transformation.getClass());
/* 55:   */     
/* 56:51 */     Hashtable ht = new Hashtable();
/* 57:52 */     ht.put("phys1", "20");
/* 58:53 */     ht.put("phys2", "21");
/* 59:   */     
/* 60:55 */     transformation.transform(ht, "pid", "14300113_01_01", null, null);
/* 61:   */   }
/* 62:   */   
/* 63:   */   public Object transform(ResultSet rs, String name, String value, StringBuffer errorMsg, String domain)
/* 64:   */     throws Exception
/* 65:   */   {
/* 66:61 */     return null;
/* 67:   */   }
/* 68:   */   
/* 69:   */   public Object transform(Hashtable rs, String name, String value, StringBuffer errorMsg, String domain)
/* 70:   */   {
/* 71:67 */     return null;
/* 72:   */   }
/* 73:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa-transformations\ken-qa-transformations.jar
 * Qualified Name:     com.kentropy.cme.transformation.CMETransformation
 * JD-Core Version:    0.7.0.1
 */