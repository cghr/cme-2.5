/*   1:    */ package com.kentropy.grid;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.SpringUtils;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ import javax.servlet.http.HttpServletResponse;
/*   7:    */ import javax.servlet.http.HttpSession;
/*   8:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*   9:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  10:    */ import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
/*  11:    */ 
/*  12:    */ public class PagingHelper
/*  13:    */ {
/*  14: 14 */   SpringUtils su = new SpringUtils();
/*  15: 15 */   JdbcTemplate jt = this.su.getJdbcTemplate();
/*  16: 17 */   SqlRowSet rs = null;
/*  17: 19 */   StringBuffer gridHeadings = new StringBuffer();
/*  18: 20 */   StringBuffer gridWidths = new StringBuffer();
/*  19: 21 */   StringBuffer tableFields = new StringBuffer();
/*  20: 22 */   StringBuffer filters = new StringBuffer();
/*  21: 23 */   String reportSQL = "";
/*  22:    */   
/*  23:    */   public StringBuffer getGridHeadings()
/*  24:    */   {
/*  25: 27 */     return this.gridHeadings;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setGridHeadings(StringBuffer gridHeadings)
/*  29:    */   {
/*  30: 30 */     this.gridHeadings = gridHeadings;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public StringBuffer getGridWidths()
/*  34:    */   {
/*  35: 33 */     return this.gridWidths;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setGridWidths(StringBuffer gridWidths)
/*  39:    */   {
/*  40: 36 */     this.gridWidths = gridWidths;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public StringBuffer getTableFields()
/*  44:    */   {
/*  45: 39 */     return this.tableFields;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setTableFields(StringBuffer tableFields)
/*  49:    */   {
/*  50: 42 */     this.tableFields = tableFields;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public StringBuffer getFilters()
/*  54:    */   {
/*  55: 47 */     return this.filters;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setFilters(StringBuffer filters)
/*  59:    */   {
/*  60: 50 */     this.filters = filters;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void createGridModelPaging(HttpServletRequest request, HttpServletResponse response, String sql, String id, int width, int dynamicLoading)
/*  64:    */   {
/*  65:    */     try
/*  66:    */     {
/*  67: 57 */       this.reportSQL = sql;
/*  68: 58 */       this.rs = this.jt.queryForRowSet(sql);
/*  69: 59 */       SqlRowSetMetaData rsmd = this.rs.getMetaData();
/*  70: 61 */       for (int i = 1; i <= rsmd.getColumnCount(); i++)
/*  71:    */       {
/*  72: 63 */         this.gridHeadings.append(rsmd.getColumnLabel(i) + ",");
/*  73: 64 */         this.gridWidths.append(width + ",");
/*  74:    */       }
/*  75: 67 */       this.gridHeadings.deleteCharAt(this.gridHeadings.length() - 1);
/*  76: 68 */       this.gridWidths.deleteCharAt(this.gridWidths.length() - 1);
/*  77: 69 */       HttpSession session = request.getSession();
/*  78: 70 */       session.setAttribute("gridHeadings", this.gridHeadings);
/*  79: 71 */       session.setAttribute("gridWidths", this.gridWidths);
/*  80: 72 */       session.setAttribute("sql", sql);
/*  81: 73 */       session.setAttribute("gridId", id);
/*  82: 74 */       session.setAttribute("dynamicLoading", Integer.valueOf(dynamicLoading));
/*  83: 76 */       if (hasSelectFilters())
/*  84:    */       {
/*  85: 78 */         session.setAttribute("hasSelectFilters", Boolean.valueOf(hasSelectFilters()));
/*  86: 79 */         session.setAttribute("selectFilterMap", getSelectFilterOptions());
/*  87:    */       }
/*  88:    */       else
/*  89:    */       {
/*  90: 83 */         session.setAttribute("hasSelectFilters", Boolean.valueOf(false));
/*  91:    */       }
/*  92:    */     }
/*  93:    */     catch (Exception e)
/*  94:    */     {
/*  95: 89 */       e.printStackTrace();
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public StringBuffer getTableFields(String table)
/* 100:    */   {
/* 101:    */     try
/* 102:    */     {
/* 103: 97 */       this.rs = this.jt.queryForRowSet("desc " + table);
/* 104: 98 */       while (this.rs.next()) {
/* 105: 99 */         this.tableFields.append(this.rs.getString(1) + ",");
/* 106:    */       }
/* 107:100 */       this.tableFields.deleteCharAt(this.tableFields.length() - 1);
/* 108:    */     }
/* 109:    */     catch (Exception e)
/* 110:    */     {
/* 111:103 */       e.printStackTrace();
/* 112:    */     }
/* 113:106 */     return this.tableFields;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public boolean hasSelectFilters()
/* 117:    */   {
/* 118:111 */     String[] filtersArray = this.filters.toString().split(",");
/* 119:113 */     for (int i = 0; i < filtersArray.length; i++) {
/* 120:115 */       if (filtersArray[i].equals("#connector_select_filter")) {
/* 121:116 */         return true;
/* 122:    */       }
/* 123:    */     }
/* 124:118 */     return false;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public HashMap<String, HashMap<String, String>> getSelectFilterOptions()
/* 128:    */   {
/* 129:123 */     String[] filtersArray = this.filters.toString().split(",");
/* 130:124 */     String[] headingsArray = this.gridHeadings.toString().split(",");
/* 131:125 */     HashMap optionsMap = new HashMap();
/* 132:127 */     for (int i = 0; i < filtersArray.length; i++) {
/* 133:129 */       if (filtersArray[i].equals("#connector_select_filter")) {
/* 134:130 */         optionsMap.put(headingsArray[i], getOptions(headingsArray[i], filtersArray));
/* 135:    */       }
/* 136:    */     }
/* 137:134 */     return optionsMap;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public HashMap<String, String> getOptions(String columnHeading, String[] filtersArray)
/* 141:    */   {
/* 142:139 */     String distinct_cols = "SELECT DISTINCT " + columnHeading + " FROM (" + this.reportSQL + ") der";
/* 143:140 */     HashMap options = new HashMap();
/* 144:141 */     SqlRowSet rs = this.jt.queryForRowSet(distinct_cols);
/* 145:143 */     while (rs.next()) {
/* 146:    */       try
/* 147:    */       {
/* 148:147 */         options.put(rs.getString(1), rs.getString(1));
/* 149:    */       }
/* 150:    */       catch (Exception e)
/* 151:    */       {
/* 152:150 */         e.printStackTrace();
/* 153:    */       }
/* 154:    */     }
/* 155:155 */     return options;
/* 156:    */   }
/* 157:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-grid\ken-grid.jar
 * Qualified Name:     com.kentropy.grid.PagingHelper
 * JD-Core Version:    0.7.0.1
 */