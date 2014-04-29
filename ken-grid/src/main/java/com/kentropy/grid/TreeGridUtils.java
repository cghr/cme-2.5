/*   1:    */ package com.kentropy.grid;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.SpringUtils;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*   6:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*   7:    */ import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
/*   8:    */ 
/*   9:    */ public class TreeGridUtils
/*  10:    */ {
/*  11: 11 */   JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  12:    */   String[][] metadata;
/*  13: 13 */   StringBuffer treeModel = new StringBuffer();
/*  14: 14 */   StringBuffer cols = new StringBuffer();
/*  15: 15 */   boolean cols_flag = false;
/*  16:    */   
/*  17:    */   public String getTreeGridModel()
/*  18:    */   {
/*  19: 19 */     String sql = this.metadata[0][1];
/*  20: 20 */     SqlRowSet rs = this.jt.queryForRowSet(sql);
/*  21:    */     
/*  22: 22 */     this.treeModel.append("<rows><cell></cell>");
/*  23:    */     
/*  24: 24 */     int level = 0;
/*  25: 25 */     while (rs.next())
/*  26:    */     {
/*  27: 27 */       this.treeModel.append("<row><cell>" + rs.getString(2) + "</cell>");
/*  28:    */       
/*  29: 29 */       String id = rs.getString(1);
/*  30: 30 */       appendChildren(level, id);
/*  31: 31 */       this.treeModel.append("</row>");
/*  32:    */     }
/*  33: 34 */     this.treeModel.append("</rows>");
/*  34: 35 */     this.cols.deleteCharAt(this.cols.length() - 1);
/*  35:    */     
/*  36: 37 */     return this.treeModel.toString();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String[][] getMetadata()
/*  40:    */   {
/*  41: 42 */     return this.metadata;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setMetadata(String[][] metadata)
/*  45:    */   {
/*  46: 47 */     this.metadata = metadata;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void appendChildren(int level, String parent_ref)
/*  50:    */   {
/*  51:    */     
/*  52: 54 */     if (level == this.metadata.length) {
/*  53: 55 */       return;
/*  54:    */     }
/*  55: 58 */     String ref_key = this.metadata[level][0];
/*  56: 59 */     String sql = this.metadata[level][1];
/*  57:    */     
/*  58: 61 */     SqlRowSet rs = this.jt.queryForRowSet(sql + " where " + ref_key + "=?", new Object[] { parent_ref });
/*  59: 63 */     while (rs.next()) {
/*  60: 65 */       if (level + 1 == this.metadata.length)
/*  61:    */       {
/*  62: 67 */         SqlRowSetMetaData rsmd = rs.getMetaData();
/*  63: 68 */         this.treeModel.append("<row>");
/*  64: 70 */         for (int c = 2; c <= rsmd.getColumnCount(); c++)
/*  65:    */         {
/*  66: 72 */           this.treeModel.append("<cell>" + rs.getString(c) + "</cell>");
/*  67: 73 */           if (!this.cols_flag) {
/*  68: 74 */             this.cols.append(rsmd.getColumnLabel(c) + ",");
/*  69:    */           }
/*  70:    */         }
/*  71: 78 */         this.cols_flag = true;
/*  72: 79 */         this.treeModel.append("</row>");
/*  73:    */       }
/*  74:    */       else
/*  75:    */       {
/*  76: 83 */         this.treeModel.append("<row><cell>" + rs.getString(2) + "</cell>");
/*  77: 84 */         String id = rs.getString(1);
/*  78: 85 */         appendChildren(level, id);
/*  79: 86 */         this.treeModel.append("</row>");
/*  80:    */       }
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public StringBuffer getCols()
/*  85:    */   {
/*  86: 93 */     return this.cols;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setCols(StringBuffer cols)
/*  90:    */   {
/*  91: 98 */     this.cols = cols;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public static void main(String[] args)
/*  95:    */   {
/*  96:103 */     TreeGridUtils tree = new TreeGridUtils();
/*  97:    */     
/*  98:105 */     String[][] metadata = {
/*  99:106 */       { "id", "", "Select * from country" }, 
/* 100:107 */       { "id", "country_id", "select * from state" }, 
/* 101:108 */       { "id", "state_id", "select * from city" } };
/* 102:    */     
/* 103:110 */     tree.setMetadata(metadata);
/* 104:    */     
/* 105:112 */     System.out.println(tree.getTreeGridModel());
/* 106:    */   }
/* 107:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-grid\ken-grid.jar
 * Qualified Name:     com.kentropy.grid.TreeGridUtils
 * JD-Core Version:    0.7.0.1
 */