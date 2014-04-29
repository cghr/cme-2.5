/*  1:   */ package com.kentropy.cme.transformation;
/*  2:   */ 
/*  3:   */ import com.kentropy.util.DbUtil;
/*  4:   */ import com.kentropy.util.SpringUtils;
/*  5:   */ import java.io.PrintStream;
/*  6:   */ import java.util.HashMap;
/*  7:   */ import java.util.Hashtable;
/*  8:   */ import java.util.Map;
/*  9:   */ import java.util.StringTokenizer;
/* 10:   */ import org.springframework.jdbc.core.JdbcTemplate;
/* 11:   */ 
/* 12:   */ public class CMEImportTransformationFilter
/* 13:   */ {
/* 14:   */   public boolean hasValidLanguage(String uniqno, String values)
/* 15:   */   {
/* 16:22 */     Map<String, String> map = getLanguageAndStateAsMap(uniqno, values);
/* 17:23 */     return isValidForState((String)map.get("language"), (String)map.get("state_code"));
/* 18:   */   }
/* 19:   */   
/* 20:   */   private Map<String, String> getLanguageAndStateAsMap(String uniqno, String values)
/* 21:   */   {
/* 22:29 */     StringTokenizer st = new StringTokenizer(values, "@#$%^");
/* 23:30 */     String path = "/va/" + uniqno;
/* 24:   */     
/* 25:32 */     Hashtable ht = new Hashtable();
/* 26:33 */     while (st.hasMoreTokens())
/* 27:   */     {
/* 28:34 */       String prop = st.nextToken();
/* 29:35 */       System.out.println("Prop:" + prop);
/* 30:36 */       StringTokenizer st1 = new StringTokenizer(prop, "=");
/* 31:   */       
/* 32:38 */       String field = st1.nextToken();
/* 33:39 */       String value1 = "";
/* 34:40 */       if (st1.hasMoreTokens()) {
/* 35:41 */         value1 = st1.nextToken();
/* 36:   */       }
/* 37:42 */       ht.put(field, value1);
/* 38:   */     }
/* 39:45 */     String statecode = uniqno.substring(0, 2);
/* 40:46 */     String language = (String)ht.get(path + "/gi/language");
/* 41:   */     
/* 42:48 */     Map<String, String> map = new HashMap();
/* 43:49 */     map.put("language", language);
/* 44:50 */     map.put("state_code", statecode);
/* 45:   */     
/* 46:52 */     return map;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public boolean isValidForState(String language, String state_code)
/* 50:   */   {
/* 51:58 */     DbUtil db = new DbUtil();
/* 52:59 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/* 53:   */     
/* 54:61 */     int count = jt.queryForInt("select count(*) from valid_languages where state_code=?", new Object[] { state_code });
/* 55:63 */     if (count == 0) {
/* 56:64 */       return false;
/* 57:   */     }
/* 58:66 */     StringTokenizer langs = new StringTokenizer(db.uniqueResult("valid_languages", "languages", "state_code=?", new Object[] { state_code }), ",");
/* 59:68 */     while (langs.hasMoreTokens()) {
/* 60:70 */       if (langs.nextToken().equals(language)) {
/* 61:71 */         return true;
/* 62:   */       }
/* 63:   */     }
/* 64:75 */     return false;
/* 65:   */   }
/* 66:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa-transformations\ken-qa-transformations.jar
 * Qualified Name:     com.kentropy.cme.transformation.CMEImportTransformationFilter
 * JD-Core Version:    0.7.0.1
 */