/*  1:   */ package com.kentropy.cme.transformation;
/*  2:   */ 
/*  3:   */ import com.kentropy.cme.qa.transformation.Transformation;
/*  4:   */ import com.kentropy.db.TestXUIDB;
/*  5:   */ import java.io.PrintStream;
/*  6:   */ import java.sql.ResultSet;
/*  7:   */ import java.util.Hashtable;
/*  8:   */ import java.util.StringTokenizer;
/*  9:   */ 
/* 10:   */ public class CMEImportTransformation
/* 11:   */   implements Transformation
/* 12:   */ {
/* 13:   */   public Object transform(ResultSet rs, String name, String value, StringBuffer errorMsg, String domain)
/* 14:   */     throws Exception
/* 15:   */   {
/* 16:18 */     return null;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public Object transform(Hashtable rs, String name, String value, StringBuffer errorMsg, String domain)
/* 20:   */   {
/* 21:   */     try
/* 22:   */     {
/* 23:25 */       String uniqno = rs.get("uniqno").toString();
/* 24:26 */       String values = rs.get("value1").toString();
/* 25:27 */       updateKeyvalue(values);
/* 26:   */     }
/* 27:   */     catch (Exception e)
/* 28:   */     {
/* 29:30 */       e.printStackTrace();
/* 30:   */     }
/* 31:32 */     return null;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void updateKeyvalue(String values)
/* 35:   */     throws Exception
/* 36:   */   {
/* 37:37 */     StringTokenizer st = new StringTokenizer(values, "@#$%^");
/* 38:   */     
/* 39:39 */     String[] props = values.split("@#$%^");
/* 40:40 */     System.out.println("PROPS:" + props.length);
/* 41:41 */     StringBuffer vaRecord = new StringBuffer();
/* 42:42 */     vaRecord.append("INSERT INTO keyvalue(key1,value1) VALUES");
/* 43:43 */     while (st.hasMoreTokens())
/* 44:   */     {
/* 45:44 */       String prop = st.nextToken();
/* 46:45 */       System.out.println("Prop:" + prop);
/* 47:46 */       StringTokenizer st1 = new StringTokenizer(prop, "=");
/* 48:   */       
/* 49:48 */       String field = st1.nextToken();
/* 50:49 */       String value1 = "";
/* 51:50 */       if (st1.hasMoreTokens()) {
/* 52:51 */         value1 = st1.nextToken();
/* 53:   */       }
/* 54:52 */       value1 = value1 == null ? "" : value1;
/* 55:   */       
/* 56:   */ 
/* 57:   */ 
/* 58:   */ 
/* 59:57 */       vaRecord.append("('" + field + "','" + value1 + "'),");
/* 60:   */     }
/* 61:62 */     vaRecord.deleteCharAt(vaRecord.length() - 1);
/* 62:63 */     StringBuffer str = new StringBuffer();
/* 63:64 */     TestXUIDB.getInstance().execSQL(vaRecord.toString(), str);
/* 64:   */   }
/* 65:   */   
/* 66:   */   public void createBachForInsert() {}
/* 67:   */   
/* 68:   */   public static void main(String[] args)
/* 69:   */     throws Exception
/* 70:   */   {
/* 71:80 */     CMEImportTransformation transformation = new CMEImportTransformation();
/* 72:   */     
/* 73:   */ 
/* 74:   */ 
/* 75:   */ 
/* 76:   */ 
/* 77:   */ 
/* 78:87 */     transformation.updateKeyvalue("/va/06300065_01_05/gi/death_age=32Y@#$%^/va/06300065_01_05/report/age=32.0@#$%^/va/06300065_01_05/pmh/06 Cancer=No@#$%^/va/06300065_01_05/pmh/07 Asthma=Yes@#$%^/va/06300065_01_05/gi/02 res_relation=Brother/Sister@#$%^/va/06300065_01_05/pmh/02 Heart disease=No@#$%^/va/06300065_01_05/pmh/08 Chronic illness=No@#$%^/va/06300065_01_05/pmh/11 Death_within_42_days_abortion=@#$%^/va/06300065_01_05/pmh/04 Diabetes=No@#$%^/va/06300065_01_05/pmh/03 Stroke=No@#$%^/va/06300065_01_05/pmh/bdiewithin=No@#$%^/va/06300065_01_05/gi/04 death_place=Home@#$%^/va/06300065_01_05/info1/stmt4=Death Date: @#$%^/va/06300065_01_05/info1/stmt3=Death Place: Home@#$%^/va/06300065_01_05/info1/stmt2=Hindi@#$%^/va/06300065_01_05/info1/stmt1=32 Years Male@#$%^/va/06300065_01_05/type=ADULT@#$%^/va/06300065_01_05/gi/deceased_sex=Male@#$%^/va/06300065_01_05/pmh/09 Pregnant=@#$%^/va/06300065_01_05/gi/death_place=Home@#$%^/va/06300065_01_05/gi/language=Hindi@#$%^/va/06300065_01_05/pmh/01 Hypertension=No@#$%^/va/06300065_01_05/pmh/05 TB=Yes@#$%^/va/06300065_01_05/report/sex=Male@#$%^/va/06300065_01_05/gi/death_date=");
/* 79:   */   }
/* 80:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa-transformations\ken-qa-transformations.jar
 * Qualified Name:     com.kentropy.cme.transformation.CMEImportTransformation
 * JD-Core Version:    0.7.0.1
 */