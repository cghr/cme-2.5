/*   1:    */ package com.kentropy.grid;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.DbUtil2;
/*   4:    */ import com.kentropy.util.SpringUtils;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*   8:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*   9:    */ 
/*  10:    */ public class GoogleApis
/*  11:    */ {
/*  12: 14 */   DbUtil2 db = new DbUtil2();
/*  13: 16 */   StringBuffer dataModels = new StringBuffer();
/*  14: 17 */   StringBuffer dataModelsJSON = new StringBuffer();
/*  15:    */   String tt;
/*  16:    */   
/*  17:    */   public void generateMetaDataForDataView(String table, String cols, String where)
/*  18:    */   {
/*  19: 21 */     this.dataModelsJSON.append("var dataModels=[");
/*  20:    */     
/*  21: 23 */     List result = this.db.getDataAsListofMaps(table, cols, where);
/*  22: 25 */     for (int i = 0; i < result.size(); i++)
/*  23:    */     {
/*  24: 27 */       Map row = (Map)result.get(i);
/*  25: 28 */       String title = (String)row.get("title");
/*  26: 29 */       String sql = (String)row.get("sql");
/*  27: 30 */       String tableProps = (String)row.get("tableProps");
/*  28: 31 */       String chartProps = (String)row.get("chartProps");
/*  29: 32 */       String type = (String)row.get("type");
/*  30:    */       
/*  31: 34 */       this.dataModels.append("var data" + (i + 1) + "= google.visualization.arrayToDataTable([" + this.db.getDataAsJSArray(sql) + "]);");
/*  32:    */       
/*  33: 36 */       this.dataModelsJSON.append(" {dataModel:data" + (i + 1) + ",title:'" + title + "',type:'" + type + "',tableProps:" + tableProps + ",chartProps:" + chartProps + "},");
/*  34:    */     }
/*  35: 39 */     this.dataModels.deleteCharAt(this.dataModels.length() - 1);
/*  36: 40 */     this.dataModelsJSON.deleteCharAt(this.dataModelsJSON.length() - 1);
/*  37:    */     
/*  38: 42 */     this.dataModelsJSON.append("];");
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void generateMetaDataForDataView(String table, String cols, String where, Object[] args)
/*  42:    */   {
/*  43: 47 */     this.dataModelsJSON.append("var dataModels=[");
/*  44:    */     
/*  45: 49 */     List result = this.db.getDataAsListofMaps(table, cols, where, args);
/*  46: 51 */     for (int i = 0; i < result.size(); i++)
/*  47:    */     {
/*  48: 53 */       Map row = (Map)result.get(i);
/*  49: 54 */       String title = (String)row.get("title");
/*  50: 55 */       String sql = (String)row.get("sql");
/*  51: 56 */       String tableProps = (String)row.get("tableProps");
/*  52: 57 */       String chartProps = (String)row.get("chartProps");
/*  53: 58 */       String type = (String)row.get("type");
/*  54:    */       
/*  55: 60 */       this.dataModels.append("var data" + (i + 1) + "= google.visualization.arrayToDataTable([" + this.db.getDataAsJSArray(sql) + "]);");
/*  56:    */       
/*  57: 62 */       this.dataModelsJSON.append(" {dataModel:data" + (i + 1) + ",title:'" + title + "',type:'" + type + "',tableProps:" + tableProps + ",chartProps:" + chartProps + "},");
/*  58:    */     }
/*  59: 65 */     this.dataModels.deleteCharAt(this.dataModels.length() - 1);
/*  60: 66 */     this.dataModelsJSON.deleteCharAt(this.dataModelsJSON.length() - 1);
/*  61:    */     
/*  62: 68 */     this.dataModelsJSON.append("];");
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void generateMetaDataForDataView(String table, String cols, String where, Object[] args, Object[] argsForQueryInTable)
/*  66:    */   {
/*  67: 73 */     this.dataModelsJSON.append("var dataModels=[");
/*  68:    */     
/*  69: 75 */     List result = this.db.getDataAsListofMaps(table, cols, where, args);
/*  70: 77 */     for (int i = 0; i < result.size(); i++)
/*  71:    */     {
/*  72: 79 */       Map row = (Map)result.get(i);
/*  73: 80 */       String title = (String)row.get("title");
/*  74: 81 */       String sql = (String)row.get("sql");
/*  75: 82 */       String tableProps = (String)row.get("tableProps");
/*  76: 83 */       String chartProps = (String)row.get("chartProps");
/*  77: 84 */       String type = (String)row.get("type");
/*  78:    */       
/*  79: 86 */       this.dataModels.append("var data" + (i + 1) + "= google.visualization.arrayToDataTable([" + this.db.getDataAsJSArray(sql, argsForQueryInTable) + "]);");
/*  80:    */       
/*  81: 88 */       this.dataModelsJSON.append(" {dataModel:data" + (i + 1) + ",title:'" + title + "',type:'" + type + "',tableProps:" + tableProps + ",chartProps:" + chartProps + "},");
/*  82:    */     }
/*  83: 91 */     this.dataModels.deleteCharAt(this.dataModels.length() - 1);
/*  84: 92 */     this.dataModelsJSON.deleteCharAt(this.dataModelsJSON.length() - 1);
/*  85:    */     
/*  86: 94 */     this.dataModelsJSON.append("];");
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void generateMetaDataForDataViewFromListOfParams(String table, String cols, String where, Object[] args, List paramsList)
/*  90:    */   {
/*  91: 99 */     this.dataModelsJSON.append("var dataModels=[");
/*  92:    */     
/*  93:101 */     List result = this.db.getDataAsListofMaps(table, cols, where, args);
/*  94:103 */     for (int i = 0; i < result.size(); i++)
/*  95:    */     {
/*  96:105 */       Map row = (Map)result.get(i);
/*  97:106 */       String title = (String)row.get("title");
/*  98:107 */       String sql = (String)row.get("sql");
/*  99:108 */       String tableProps = (String)row.get("tableProps");
/* 100:109 */       String chartProps = (String)row.get("chartProps");
/* 101:110 */       String type = (String)row.get("type");
/* 102:    */       
/* 103:112 */       this.dataModels.append("var data" + (i + 1) + "= google.visualization.arrayToDataTable([" + this.db.getDataAsJSArray(sql, (Object[])paramsList.get(i)) + "]);");
/* 104:    */       
/* 105:114 */       this.dataModelsJSON.append(" {dataModel:data" + (i + 1) + ",title:'" + title + "',type:'" + type + "',tableProps:" + tableProps + ",chartProps:" + chartProps + "},");
/* 106:    */     }
/* 107:117 */     this.dataModels.deleteCharAt(this.dataModels.length() - 1);
/* 108:118 */     this.dataModelsJSON.deleteCharAt(this.dataModelsJSON.length() - 1);
/* 109:    */     
/* 110:120 */     this.dataModelsJSON.append("];");
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void generateMetaDataForTableView(String table, String cols, String where, Object[] args, Object[] argsForQueryInTable)
/* 114:    */   {
/* 115:126 */     this.dataModelsJSON.append("var dataModels=[");
/* 116:    */     
/* 117:128 */     List result = this.db.getDataAsListofMaps(table, cols, where, args);
/* 118:130 */     for (int i = 0; i < result.size(); i++)
/* 119:    */     {
/* 120:132 */       Map row = (Map)result.get(i);
/* 121:133 */       String title = (String)row.get("title");
/* 122:134 */       String sql = (String)row.get("sql");
/* 123:135 */       String tableProps = (String)row.get("tableProps");
/* 124:136 */       String type = (String)row.get("type");
/* 125:137 */       String headings = (String)row.get("headings");
/* 126:138 */       if ((headings == null) || (headings.equals(""))) {
/* 127:140 */         headings = "['Property','Value']";
/* 128:    */       }
/* 129:143 */       this.dataModels.append("var data" + (i + 1) + "= google.visualization.arrayToDataTable([" + this.db.getDataAsJSArrayTranspose1(sql, argsForQueryInTable, headings) + "]);");
/* 130:    */       
/* 131:145 */       this.dataModelsJSON.append(" {dataModel:data" + (i + 1) + ",title:'" + title + "',type:'" + type + "',tableProps:" + tableProps + "},");
/* 132:    */     }
/* 133:148 */     this.dataModels.deleteCharAt(this.dataModels.length() - 1);
/* 134:149 */     this.dataModelsJSON.deleteCharAt(this.dataModelsJSON.length() - 1);
/* 135:    */     
/* 136:151 */     this.dataModelsJSON.append("];");
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void generateMetaDataForTableViewWithoutTranspose(String table, String cols, String where, Object[] args, Object[] argsForQueryInTable)
/* 140:    */   {
/* 141:156 */     this.dataModelsJSON.append("var dataModels=[");
/* 142:    */     
/* 143:158 */     List result = this.db.getDataAsListofMaps(table, cols, where, args);
/* 144:160 */     for (int i = 0; i < result.size(); i++)
/* 145:    */     {
/* 146:162 */       Map row = (Map)result.get(i);
/* 147:163 */       String title = (String)row.get("title");
/* 148:164 */       String sql = (String)row.get("sql");
/* 149:165 */       String tableProps = (String)row.get("tableProps");
/* 150:166 */       String type = (String)row.get("type");
/* 151:    */       
/* 152:168 */       this.dataModels.append("var data" + (i + 1) + "= google.visualization.arrayToDataTable([" + this.db.getDataAsJSArrayWithAllStrings(sql, argsForQueryInTable) + "]);");
/* 153:    */       
/* 154:170 */       this.dataModelsJSON.append(" {dataModel:data" + (i + 1) + ",title:'" + title + "',type:'" + type + "',tableProps:" + tableProps + "},");
/* 155:    */     }
/* 156:173 */     this.dataModels.deleteCharAt(this.dataModels.length() - 1);
/* 157:174 */     this.dataModelsJSON.deleteCharAt(this.dataModelsJSON.length() - 1);
/* 158:    */     
/* 159:176 */     this.dataModelsJSON.append("];");
/* 160:    */   }
/* 161:    */   
/* 162:    */   public StringBuffer getDataModels()
/* 163:    */   {
/* 164:181 */     return this.dataModels;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void setDataModels(StringBuffer dataModels)
/* 168:    */   {
/* 169:186 */     this.dataModels = dataModels;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public StringBuffer getDataModelsJSON()
/* 173:    */   {
/* 174:191 */     return this.dataModelsJSON;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void setDataModelsJSON(StringBuffer dataModelsJSON)
/* 178:    */   {
/* 179:196 */     this.dataModelsJSON = dataModelsJSON;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public static void main(String[] args)
/* 183:    */   {
/* 184:201 */     String[] tt = { "1" };
/* 185:202 */     ClassPathXmlApplicationContext appC = new ClassPathXmlApplicationContext("appContext.xml");
/* 186:203 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/* 187:204 */     DbUtil2 db = new DbUtil2();
/* 188:205 */     db.setJt(jt);
/* 189:206 */     GoogleApis gp = new GoogleApis();
/* 190:207 */     gp.db = db;
/* 191:208 */     gp.generateMetaDataForTableView("`dataview_admin_dashboard`", "*", null, new Object[] { Integer.valueOf(1) }, tt);
/* 192:    */   }
/* 193:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-grid\ken-grid.jar
 * Qualified Name:     com.kentropy.grid.GoogleApis
 * JD-Core Version:    0.7.0.1
 */