/*   1:    */ package com.kentropy.cme.transformation;
/*   2:    */ 
/*   3:    */ import com.kentropy.cme.qa.transformation.Transformation;
/*   4:    */ import com.kentropy.util.DbUtil;
/*   5:    */ import com.kentropy.util.SpringUtils;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.util.Hashtable;
/*   9:    */ import java.util.StringTokenizer;
/*  10:    */ import net.xoetrope.xui.data.XBaseModel;
/*  11:    */ import net.xoetrope.xui.data.XModel;
/*  12:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  13:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  14:    */ 
/*  15:    */ public class CMEReportTransformation
/*  16:    */   implements Transformation
/*  17:    */ {
/*  18: 18 */   JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  19: 19 */   DbUtil db = new DbUtil();
/*  20:    */   
/*  21:    */   public Object transform(ResultSet rs, String name, String value, StringBuffer errorMsg, String domain)
/*  22:    */     throws Exception
/*  23:    */   {
/*  24: 24 */     return null;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public Object transform(Hashtable rs, String name, String value, StringBuffer errorMsg, String domain)
/*  28:    */   {
/*  29:    */     try
/*  30:    */     {
/*  31: 31 */       String uniqno = rs.get("uniqno").toString();
/*  32: 32 */       String path = "/va/" + uniqno;
/*  33: 33 */       String values = rs.get("value1").toString();
/*  34: 34 */       StringTokenizer st = new StringTokenizer(values, "@#$%^");
/*  35:    */       
/*  36: 36 */       String[] props = values.split("@#$%^");
/*  37: 37 */       System.out.println("PROPS:" + props.length);
/*  38: 38 */       Hashtable ht = new Hashtable();
/*  39: 39 */       while (st.hasMoreTokens())
/*  40:    */       {
/*  41: 40 */         String prop = st.nextToken();
/*  42: 41 */         System.out.println("Prop:" + prop);
/*  43: 42 */         StringTokenizer st1 = new StringTokenizer(prop, "=");
/*  44:    */         
/*  45: 44 */         String field = st1.nextToken();
/*  46: 45 */         String value1 = "";
/*  47: 46 */         if (st1.hasMoreTokens()) {
/*  48: 47 */           value1 = st1.nextToken();
/*  49:    */         }
/*  50: 48 */         ht.put(field, value1);
/*  51:    */       }
/*  52: 51 */       String statecode = uniqno.substring(0, 2);
/*  53: 52 */       String age = (String)ht.get(path + "/report/age");
/*  54: 53 */       String srs_code = (String)ht.get(path + "/report/srs_code");
/*  55: 54 */       String sex = (String)ht.get(path + "/report/sex");
/*  56: 55 */       String domain1 = (String)ht.get(path + "/type");
/*  57: 56 */       String language = (String)ht.get(path + "/gi/language");
/*  58: 57 */       String year = (String)ht.get(path + "/report/year");
/*  59:    */       
/*  60: 59 */       XModel xm = new XBaseModel();
/*  61: 60 */       xm.set("age", age);
/*  62: 61 */       xm.set("sex", sex);
/*  63: 62 */       xm.set("type", domain1);
/*  64: 63 */       xm.set("language", language);
/*  65: 64 */       xm.set("state_code", statecode);
/*  66: 65 */       xm.set("uniqno", uniqno);
/*  67: 66 */       xm.set("year", year);
/*  68: 67 */       xm.set("srs_code", srs_code);
/*  69: 68 */       xm.set("study", uniqno.indexOf('_') > -1 ? "mds" : "barshi");
/*  70:    */       
/*  71:    */ 
/*  72: 71 */       this.db.saveData("report", "uniqno='" + uniqno + "'", xm);
/*  73:    */       
/*  74:    */ 
/*  75:    */ 
/*  76:    */ 
/*  77:    */ 
/*  78:    */ 
/*  79:    */ 
/*  80:    */ 
/*  81:    */ 
/*  82:    */ 
/*  83: 82 */       XModel xm1 = new XBaseModel();
/*  84: 83 */       String sql = "SELECT  FIELD,path FROM cme_dictionary  a LEFT JOIN rgi_context_mapping b ON  CONCAT('va/',RIGHT(a.domain,LENGTH(a.domain)-4))= b.domain AND a.field=b.value WHERE b.domain='va/" + domain1 + "'";
/*  85: 84 */       SqlRowSet result = this.jt.queryForRowSet(sql);
/*  86:    */       
/*  87: 86 */       xm1.set("uniqno", uniqno);
/*  88: 88 */       while (result.next())
/*  89:    */       {
/*  90: 90 */         String key1 = result.getString("field");
/*  91: 91 */         String path1 = result.getString("path");
/*  92: 92 */         System.out.println(path1);
/*  93:    */         
/*  94: 94 */         xm1.set(key1, (String)ht.get(path + "/" + path1));
/*  95:    */       }
/*  96:103 */       domain1 = domain1.toLowerCase();
/*  97:    */       
/*  98:105 */       this.db.saveData("cme_" + domain1, "uniqno='" + uniqno + "'", xm1);
/*  99:    */     }
/* 100:    */     catch (Exception e)
/* 101:    */     {
/* 102:118 */       e.printStackTrace();
/* 103:    */     }
/* 104:120 */     return null;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public static void main(String[] args)
/* 108:    */   {
/* 109:126 */     String domain = args[0];
/* 110:    */     
/* 111:128 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/* 112:    */     
/* 113:130 */     Transformation transformation = new CMEReportTransformation();
/* 114:    */     
/* 115:    */ 
/* 116:    */ 
/* 117:    */ 
/* 118:    */ 
/* 119:136 */     String sql = "SELECT uniqno,value1 FROM cme_records WHERE uniqno IN  (SELECT pid  FROM `process` WHERE pid NOT IN (SELECT uniqno FROM report) AND stateMachineClass LIKE '%CME%' )";
/* 120:    */     
/* 121:    */ 
/* 122:    */ 
/* 123:140 */     SqlRowSet rs = jt.queryForRowSet(sql);
/* 124:141 */     while (rs.next())
/* 125:    */     {
/* 126:146 */       Hashtable ht = new Hashtable();
/* 127:    */       
/* 128:148 */       String uniqno = rs.getString("uniqno");
/* 129:149 */       String value1 = rs.getString("value1");
/* 130:    */       
/* 131:151 */       ht.put("uniqno", uniqno);
/* 132:152 */       ht.put("value1", value1);
/* 133:    */       
/* 134:154 */       transformation.transform(ht, "uniqno", uniqno, null, domain);
/* 135:    */     }
/* 136:    */   }
/* 137:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa-transformations\ken-qa-transformations.jar
 * Qualified Name:     com.kentropy.cme.transformation.CMEReportTransformation
 * JD-Core Version:    0.7.0.1
 */