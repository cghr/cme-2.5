/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.regex.Pattern;
/*   5:    */ 
/*   6:    */ public class DataRequest
/*   7:    */ {
/*   8:    */   private ArrayList<FilteringRule> filters;
/*   9:    */   private ArrayList<SortingRule> sort_by;
/*  10: 23 */   private String start = "";
/*  11: 26 */   private String count = "";
/*  12: 29 */   private String relation = "";
/*  13: 32 */   private String source = "";
/*  14: 35 */   private String fieldset = "";
/*  15:    */   
/*  16:    */   public DataRequest()
/*  17:    */   {
/*  18: 41 */     this.filters = new ArrayList();
/*  19: 42 */     this.sort_by = new ArrayList();
/*  20:    */   }
/*  21:    */   
/*  22:    */   public DataRequest(DataRequest source)
/*  23:    */   {
/*  24: 51 */     this();
/*  25: 52 */     copy(source);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void copy(DataRequest original)
/*  29:    */   {
/*  30: 62 */     this.filters = original.get_filters();
/*  31: 63 */     this.sort_by = original.get_sort_by();
/*  32: 64 */     this.count = original.get_count();
/*  33: 65 */     this.start = original.get_start();
/*  34: 66 */     this.source = original.get_source();
/*  35: 67 */     this.fieldset = original.get_fieldset();
/*  36: 68 */     this.relation = original.get_relation();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String get_source()
/*  40:    */   {
/*  41: 77 */     return this.source;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public String get_start()
/*  45:    */   {
/*  46: 86 */     return this.start;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public String get_count()
/*  50:    */   {
/*  51: 95 */     return this.count;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public String get_fieldset()
/*  55:    */   {
/*  56:104 */     return this.fieldset;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public ArrayList<FilteringRule> get_filters()
/*  60:    */   {
/*  61:113 */     return this.filters;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String get_relation()
/*  65:    */   {
/*  66:122 */     return this.relation;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public ArrayList<SortingRule> get_sort_by()
/*  70:    */   {
/*  71:131 */     return this.sort_by;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void set_fieldset(String value)
/*  75:    */   {
/*  76:140 */     this.fieldset = value;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void set_filter(String field, String value, String rule)
/*  80:    */   {
/*  81:151 */     this.filters.add(new FilteringRule(field, value, rule));
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void set_filter(String field, String value)
/*  85:    */   {
/*  86:161 */     set_filter(field, value, "");
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void set_filter(String sql)
/*  90:    */   {
/*  91:170 */     this.filters.add(new FilteringRule(sql));
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void set_sort(String column)
/*  95:    */   {
/*  96:179 */     set_sort(column, "");
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void set_sort(String column, String direction)
/* 100:    */   {
/* 101:189 */     if ((column == null) || (column.equals(""))) {
/* 102:190 */       this.sort_by = new ArrayList();
/* 103:    */     } else {
/* 104:192 */       this.sort_by.add(new SortingRule(column, direction));
/* 105:    */     }
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void set_limit(String start, String count)
/* 109:    */   {
/* 110:202 */     this.count = count;
/* 111:203 */     this.start = start;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void set_limit(int start, int count)
/* 115:    */   {
/* 116:213 */     set_limit(Integer.toString(start), Integer.toString(count));
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void set_source(String name)
/* 120:    */     throws ConnectorConfigException
/* 121:    */   {
/* 122:224 */     this.source = name.trim();
/* 123:225 */     if (this.source.equals("")) {
/* 124:226 */       throw new ConnectorConfigException("Source of data can't be empty");
/* 125:    */     }
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void set_relation(String relation)
/* 129:    */   {
/* 130:235 */     this.relation = relation;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void parse_sql(String sql, Boolean as_is)
/* 134:    */     throws ConnectorConfigException
/* 135:    */   {
/* 136:246 */     if (as_is.booleanValue())
/* 137:    */     {
/* 138:247 */       this.fieldset = sql;
/* 139:248 */       return;
/* 140:    */     }
/* 141:250 */     Pattern limit_regex = Pattern.compile("[ \n]+limit[\n ,0-9]", 2);
/* 142:251 */     Pattern where_regex = Pattern.compile("[ \n]+where", 2);
/* 143:252 */     Pattern from_regex = Pattern.compile("[ \n]+from", 2);
/* 144:253 */     Pattern select_regex = Pattern.compile("select", 2);
/* 145:254 */     Pattern order_regex = Pattern.compile("[ \n]+order[ ]+by", 2);
/* 146:255 */     Pattern empty_regex = Pattern.compile("[ ]+", 2);
/* 147:256 */     Pattern groupby_regex = Pattern.compile("[ \n]+group[ \n]+by[ \n]+", 2);
/* 148:    */     
/* 149:258 */     sql = limit_regex.split(sql)[0];
/* 150:260 */     if (groupby_regex.split(sql).length > 1)
/* 151:    */     {
/* 152:261 */       set_source("(" + sql + ") dhx_group_table");
/* 153:262 */       return;
/* 154:    */     }
/* 155:266 */     String[] data = from_regex.split(sql, 2);
/* 156:267 */     set_fieldset(select_regex.split(data[0], 2)[1]);
/* 157:    */     
/* 158:269 */     String[] table_data = where_regex.split(data[1], 2);
/* 159:270 */     if (table_data.length > 1)
/* 160:    */     {
/* 161:271 */       set_source(table_data[0]);
/* 162:272 */       String[] where_data = order_regex.split(table_data[1]);
/* 163:273 */       set_filter(where_data[0]);
/* 164:274 */       if (where_data.length == 1) {
/* 165:274 */         return;
/* 166:    */       }
/* 167:275 */       sql = where_data[1].trim();
/* 168:    */     }
/* 169:    */     else
/* 170:    */     {
/* 171:277 */       String[] order_data = order_regex.split(table_data[0], 2);
/* 172:278 */       set_source(order_data[0]);
/* 173:279 */       if (order_data.length == 1) {
/* 174:279 */         return;
/* 175:    */       }
/* 176:280 */       sql = order_data[1].trim();
/* 177:    */     }
/* 178:283 */     if (!sql.equals(""))
/* 179:    */     {
/* 180:284 */       String[] order_details = empty_regex.split(sql);
/* 181:285 */       set_sort(order_details[0], order_details[1]);
/* 182:    */     }
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void parse_sql(String sql)
/* 186:    */     throws ConnectorConfigException
/* 187:    */   {
/* 188:290 */     parse_sql(sql, Boolean.valueOf(false));
/* 189:    */   }
/* 190:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.DataRequest
 * JD-Core Version:    0.7.0.1
 */