/*   1:    */ package com.kentropy.grid;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.SpringUtils;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import org.json.simple.JSONObject;
/*   6:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*   7:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*   8:    */ import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
/*   9:    */ 
/*  10:    */ public class GridUtils
/*  11:    */ {
/*  12: 12 */   SpringUtils su = new SpringUtils();
/*  13: 13 */   private JdbcTemplate jt = this.su.getJdbcTemplate();
/*  14: 14 */   private SqlRowSet rs = null;
/*  15: 15 */   private StringBuffer headings = new StringBuffer();
/*  16: 16 */   private StringBuffer gridWidths = new StringBuffer();
/*  17: 17 */   private StringBuffer excel = new StringBuffer();
/*  18: 19 */   private int rowCount = 0;
/*  19: 20 */   private int columnCount = 0;
/*  20:    */   
/*  21:    */   public StringBuffer getExcel()
/*  22:    */   {
/*  23: 24 */     return this.excel;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setExcel(StringBuffer excel)
/*  27:    */   {
/*  28: 27 */     this.excel = excel;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public StringBuffer getHeadings()
/*  32:    */   {
/*  33: 32 */     return this.headings;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setHeadings(StringBuffer headings)
/*  37:    */   {
/*  38: 35 */     this.headings = headings;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public StringBuffer getGridWidths()
/*  42:    */   {
/*  43: 38 */     return this.gridWidths;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setGridWidths(StringBuffer gridWidths)
/*  47:    */   {
/*  48: 41 */     this.gridWidths = gridWidths;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int getRowCount()
/*  52:    */   {
/*  53: 44 */     return this.rowCount;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setRowCount(int rowCount)
/*  57:    */   {
/*  58: 47 */     this.rowCount = rowCount;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public int getColumnCount()
/*  62:    */   {
/*  63: 50 */     return this.columnCount;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setColumnCount(int columnCount)
/*  67:    */   {
/*  68: 53 */     this.columnCount = columnCount;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public StringBuffer getGridModel(String sql, int width)
/*  72:    */   {
/*  73: 58 */     StringBuffer gridModel = new StringBuffer();
/*  74: 59 */     StringBuffer head = new StringBuffer();
/*  75: 60 */     head.append("<rows profile='color'><head><columns>");
/*  76:    */     try
/*  77:    */     {
/*  78: 63 */       this.rs = this.jt.queryForRowSet(sql);
/*  79: 64 */       SqlRowSetMetaData rsmd = this.rs.getMetaData();
/*  80: 65 */       int column_count = rsmd.getColumnCount();
/*  81: 66 */       this.columnCount = rsmd.getColumnCount();
/*  82: 67 */       for (int i = 1; i <= column_count; i++)
/*  83:    */       {
/*  84: 69 */         this.headings.append(rsmd.getColumnLabel(i) + ",");
/*  85: 70 */         this.gridWidths.append(width + ",");
/*  86: 71 */         head.append("<column width='" + width + "' align='left' type='ed' hidden='false' sort='na' color=''>");
/*  87: 72 */         head.append("<![CDATA[" + rsmd.getColumnName(i) + "]]>");
/*  88: 73 */         head.append("</column>");
/*  89:    */       }
/*  90: 76 */       head.append("</columns></head>");
/*  91: 77 */       this.headings.deleteCharAt(this.headings.length() - 1);
/*  92: 78 */       this.gridWidths.deleteCharAt(this.gridWidths.length() - 1);
/*  93: 80 */       while (this.rs.next())
/*  94:    */       {
/*  95: 82 */         gridModel.append("<row>");
/*  96: 84 */         for (int i = 1; i <= column_count; i++)
/*  97:    */         {
/*  98: 86 */           gridModel.append("<cell><![CDATA[");
/*  99:    */           
/* 100: 88 */           gridModel.append(this.rs.getString(i));
/* 101:    */           
/* 102: 90 */           gridModel.append("]]></cell>");
/* 103:    */         }
/* 104: 93 */         gridModel.append("</row>");
/* 105: 94 */         this.rowCount += 1;
/* 106:    */       }
/* 107: 96 */       this.excel.append(head);
/* 108: 97 */       this.excel.append(gridModel);
/* 109: 98 */       this.excel.append("</rows>");
/* 110: 99 */       gridModel.insert(0, "<rows>");
/* 111:100 */       gridModel.append("</rows>");
/* 112:    */     }
/* 113:    */     catch (Exception e)
/* 114:    */     {
/* 115:104 */       e.printStackTrace();
/* 116:    */     }
/* 117:107 */     return gridModel;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public StringBuffer getGridModelJSON(String sql, int width)
/* 121:    */   {
/* 122:112 */     StringBuffer gridModel = new StringBuffer();
/* 123:113 */     StringBuffer head = new StringBuffer();
/* 124:114 */     head.append("<rows profile='color'><head><columns>");
/* 125:115 */     gridModel.append("{\"rows\":[");
/* 126:    */     try
/* 127:    */     {
/* 128:118 */       this.rs = this.jt.queryForRowSet(sql);
/* 129:119 */       SqlRowSetMetaData rsmd = this.rs.getMetaData();
/* 130:120 */       int column_count = rsmd.getColumnCount();
/* 131:121 */       this.columnCount = rsmd.getColumnCount();
/* 132:122 */       for (int i = 1; i <= column_count; i++)
/* 133:    */       {
/* 134:124 */         this.headings.append(rsmd.getColumnLabel(i) + ",");
/* 135:125 */         this.gridWidths.append(width + ",");
/* 136:126 */         head.append("<column width='" + width + "' align='left' type='ed' hidden='false' sort='na' color=''>");
/* 137:127 */         head.append("<![CDATA[" + rsmd.getColumnName(i) + "]]>");
/* 138:128 */         head.append("</column>");
/* 139:    */       }
/* 140:131 */       this.headings.deleteCharAt(this.headings.length() - 1);
/* 141:132 */       this.gridWidths.deleteCharAt(this.gridWidths.length() - 1);
/* 142:    */       
/* 143:134 */       int rowcount = 0;
/* 144:135 */       while (this.rs.next())
/* 145:    */       {
/* 146:137 */         gridModel.append("{\"id\":" + rowcount + ",\"data\":[");
/* 147:138 */         rowcount++;
/* 148:139 */         for (int i = 1; i <= column_count; i++) {
/* 149:141 */           gridModel.append("\"" + JSONObject.escape(this.rs.getString(i)) + "\",");
/* 150:    */         }
/* 151:144 */         gridModel.deleteCharAt(gridModel.length() - 1);
/* 152:145 */         gridModel.append("]},");
/* 153:146 */         this.rowCount += 1;
/* 154:    */       }
/* 155:148 */       if (rowcount > 0) {
/* 156:149 */         gridModel.deleteCharAt(gridModel.length() - 1);
/* 157:    */       }
/* 158:152 */       gridModel.append("]}");
/* 159:    */     }
/* 160:    */     catch (Exception e)
/* 161:    */     {
/* 162:156 */       e.printStackTrace();
/* 163:    */     }
/* 164:159 */     return gridModel;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public static void main(String[] args)
/* 168:    */   {
/* 169:164 */     GridUtils gu = new GridUtils();
/* 170:    */     
/* 171:166 */     System.out.println(gu.getGridModelJSON("select * from accounts", 100));
/* 172:    */   }
/* 173:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-grid\ken-grid.jar
 * Qualified Name:     com.kentropy.grid.GridUtils
 * JD-Core Version:    0.7.0.1
 */