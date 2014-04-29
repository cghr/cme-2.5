/*   1:    */ package com.kentropy.utilities.dhtmlx;
/*   2:    */ 
/*   3:    */ import com.kentropy.tree.Node;
/*   4:    */ import com.kentropy.util.SpringApplicationContext;
/*   5:    */ import com.kentropy.util.SpringUtils;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import java.text.MessageFormat;
/*  10:    */ import java.util.Arrays;
/*  11:    */ import java.util.Hashtable;
/*  12:    */ import java.util.List;
/*  13:    */ import java.util.Vector;
/*  14:    */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*  15:    */ import org.springframework.dao.DataAccessException;
/*  16:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  17:    */ import org.springframework.jdbc.core.ResultSetExtractor;
/*  18:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  19:    */ 
/*  20:    */ public class ReportGenerator1
/*  21:    */ {
/*  22:    */   public String getReport(int reportId, String[] params)
/*  23:    */   {
/*  24: 29 */     Hashtable<String, String> ht = new Hashtable();
/*  25: 30 */     getReportDefinition(reportId, ht);
/*  26: 31 */     Node n = new Node("row", "root", "");
/*  27: 32 */     String table = (String)ht.get("table");
/*  28: 33 */     String headers = (String)ht.get("headings");
/*  29: 34 */     String images = (String)ht.get("images");
/*  30: 35 */     String aggregations = (String)ht.get("aggregations");
/*  31: 36 */     String dimFields = (String)ht.get("dim_fields");
/*  32: 37 */     String factFields = (String)ht.get("fact_fields");
/*  33: 38 */     String where = (String)ht.get("where");
/*  34: 39 */     int numDims = dimFields.split(",").length;
/*  35: 40 */     int numFacts = factFields.split(",").length;
/*  36: 41 */     List<String> headers1 = Arrays.asList(headers.split(","));
/*  37: 42 */     List<String> images1 = Arrays.asList(images.split(","));
/*  38: 43 */     List<String> aggregations1 = Arrays.asList(aggregations.split(","));
/*  39: 44 */     Vector<String> headers2 = new Vector();
/*  40: 45 */     headers2.addAll(headers1);
/*  41: 46 */     Vector<String> images2 = new Vector();
/*  42: 47 */     images2.addAll(images1);
/*  43: 48 */     Vector<String> aggregations2 = new Vector();
/*  44: 49 */     aggregations2.addAll(aggregations1);
/*  45:    */     
/*  46: 51 */     MessageFormat mf = new MessageFormat(where);
/*  47: 52 */     String where1 = mf.format(params);
/*  48: 53 */     System.out.println(" Where " + where1);
/*  49: 54 */     String sql = "select " + dimFields + "," + factFields + " from " + table + " where " + where1 + " group by  " + dimFields;
/*  50: 55 */     System.out.println("sql:" + sql);
/*  51: 56 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  52: 57 */     SqlRowSet rs = jt.queryForRowSet(sql);
/*  53:    */     
/*  54: 59 */     Vector<Vector<String>> values = new Vector();
/*  55: 60 */     values = (Vector)jt.query(sql, new ResultSetExtractor()
/*  56:    */     {
/*  57:    */       int numDims1;
/*  58:    */       int numFacts1;
/*  59:    */       
/*  60:    */       public Vector<Vector<String>> extractData(ResultSet rs)
/*  61:    */         throws SQLException, DataAccessException
/*  62:    */       {
/*  63: 68 */         Vector<Vector<String>> values = new Vector();
/*  64: 69 */         while (rs.next())
/*  65:    */         {
/*  66: 71 */           Vector<String> row1 = new Vector();
/*  67: 72 */           for (int i = 0; i < this.numDims1 + this.numFacts1; i++) {
/*  68: 74 */             row1.add(rs.getString(i + 1));
/*  69:    */           }
/*  70: 76 */           values.add(row1);
/*  71:    */         }
/*  72: 78 */         return values;
/*  73:    */       }
/*  74: 86 */     });
/*  75: 87 */     Node.testNode(headers2, images2, aggregations2, values, numFacts, n);
/*  76: 88 */     return n.toString();
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void getReportDefinition(int reportId, Hashtable ht)
/*  80:    */   {
/*  81: 94 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  82: 95 */     SqlRowSet rs = jt.queryForRowSet("select `table`,dim_fields,fact_fields,`where`,headings,init_widths,col_align,col_types,sorting,images,aggregations from reports where id=" + reportId);
/*  83: 96 */     if (rs.next())
/*  84:    */     {
/*  85: 98 */       ht.put("table", rs.getString(1));
/*  86: 99 */       ht.put("dim_fields", rs.getString(2));
/*  87:100 */       ht.put("fact_fields", rs.getString(3));
/*  88:101 */       ht.put("where", rs.getString(4));
/*  89:102 */       ht.put("headings", rs.getString(5));
/*  90:103 */       ht.put("init_widths", rs.getString(6));
/*  91:104 */       ht.put("col_align", rs.getString(7));
/*  92:105 */       ht.put("col_types", rs.getString(8));
/*  93:106 */       ht.put("sorting", rs.getString(9));
/*  94:107 */       ht.put("images", rs.getString(10));
/*  95:108 */       ht.put("aggregations", rs.getString(11));
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public static void main(String[] args)
/* 100:    */   {
/* 101:113 */     new SpringApplicationContext().setApplicationContext(new ClassPathXmlApplicationContext("appContext.xml"));
/* 102:114 */     ReportGenerator1 r = new ReportGenerator1();
/* 103:115 */     String[] params = { "2012-03-01" };
/* 104:116 */     System.out.println(r.getReport(1, params));
/* 105:    */   }
/* 106:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.utilities.dhtmlx.ReportGenerator1
 * JD-Core Version:    0.7.0.1
 */