/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.SpringUtils;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.sql.Statement;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.regex.Matcher;
/*  10:    */ import java.util.regex.Pattern;
/*  11:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  12:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  13:    */ 
/*  14:    */ public abstract class DBDataWrapper
/*  15:    */   extends DataWrapper
/*  16:    */ {
/*  17: 49 */   protected String sequence_name = "";
/*  18: 52 */   private HashMap<OperationType, String> sqls = new HashMap();
/*  19:    */   
/*  20:    */   public abstract String escape(String paramString);
/*  21:    */   
/*  22:    */   public abstract String get_new_id(ConnectorResultSet paramConnectorResultSet)
/*  23:    */     throws ConnectorOperationException;
/*  24:    */   
/*  25:    */   public void sequence(String sequence_name)
/*  26:    */   {
/*  27: 63 */     this.sequence_name = sequence_name;
/*  28:    */   }
/*  29:    */   
/*  30:    */   protected Connection get_connection()
/*  31:    */   {
/*  32: 72 */     return (Connection)this.connection;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void attach(OperationType name, String sql)
/*  36:    */   {
/*  37: 82 */     this.sqls.put(name, sql);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public String get_sql(OperationType name, HashMap<String, String> data)
/*  41:    */     throws ConnectorConfigException
/*  42:    */   {
/*  43: 89 */     String sql = (String)this.sqls.get(name);
/*  44: 90 */     if (sql == null) {
/*  45: 90 */       return "";
/*  46:    */     }
/*  47: 93 */     StringBuffer result = new StringBuffer();
/*  48: 94 */     Pattern regex = Pattern.compile("\\{([^}]+)\\}");
/*  49: 95 */     Matcher regexMatcher = regex.matcher(sql);
/*  50: 96 */     while (regexMatcher.find())
/*  51:    */     {
/*  52: 98 */       String value = (String)data.get(regexMatcher.group(1));
/*  53: 99 */       if (value == null) {
/*  54: 99 */         throw new ConnectorConfigException("Unknown field in sql");
/*  55:    */       }
/*  56:100 */       regexMatcher.appendReplacement(result, escape(value));
/*  57:    */     }
/*  58:102 */     regexMatcher.appendTail(result);
/*  59:    */     
/*  60:104 */     return result.toString();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void delete(DataAction data, DataRequest source)
/*  64:    */     throws ConnectorOperationException
/*  65:    */   {
/*  66:112 */     String sql = delete_query(data, source);
/*  67:113 */     query(sql);
/*  68:114 */     data.success();
/*  69:    */   }
/*  70:    */   
/*  71:    */   private String delete_query(DataAction data, DataRequest source)
/*  72:    */   {
/*  73:126 */     StringBuffer sql = new StringBuffer();
/*  74:127 */     sql.append("DELETE FROM ");
/*  75:128 */     sql.append(source.get_source());
/*  76:129 */     sql.append(" WHERE " + this.config.id.db_name + "='" + escape(data.get_id()) + "'");
/*  77:    */     
/*  78:131 */     String where = build_where(source.get_filters(), source.get_relation());
/*  79:132 */     if (!where.equals("")) {
/*  80:133 */       sql.append(" AND ( " + where + " )");
/*  81:    */     }
/*  82:134 */     return sql.toString();
/*  83:    */   }
/*  84:    */   
/*  85:    */   protected String insert_query(DataAction data, DataRequest source)
/*  86:    */   {
/*  87:146 */     StringBuffer fields = new StringBuffer();
/*  88:147 */     StringBuffer values = new StringBuffer();
/*  89:149 */     for (int i = 0; i < this.config.text.size(); i++)
/*  90:    */     {
/*  91:150 */       if (i != 0)
/*  92:    */       {
/*  93:151 */         fields.append(",");
/*  94:152 */         values.append(",");
/*  95:    */       }
/*  96:154 */       ConnectorField field = (ConnectorField)this.config.text.get(i);
/*  97:155 */       fields.append(field.db_name);
/*  98:156 */       values.append("'" + escape(data.get_value(field.name)) + "'");
/*  99:    */     }
/* 100:158 */     if (!this.config.relation_id.db_name.equals(""))
/* 101:    */     {
/* 102:159 */       fields.append("," + this.config.relation_id.db_name);
/* 103:160 */       values.append(",'" + escape(data.get_value(this.config.relation_id.name)) + "'");
/* 104:    */     }
/* 105:163 */     if (!this.sequence_name.equals(""))
/* 106:    */     {
/* 107:164 */       fields.append("," + this.config.id.db_name);
/* 108:165 */       values.append("," + this.sequence_name);
/* 109:    */     }
/* 110:168 */     return "INSERT INTO " + source.get_source() + " (" + fields.toString() + ")" + " VALUES (" + values.toString() + ")";
/* 111:    */   }
/* 112:    */   
/* 113:    */   public String get_size(DataRequest source)
/* 114:    */     throws ConnectorOperationException
/* 115:    */   {
/* 116:176 */     DataRequest count = new DataRequest(source);
/* 117:    */     
/* 118:178 */     count.set_fieldset("COUNT(*) as DHX_COUNT ");
/* 119:179 */     count.set_sort(null);
/* 120:180 */     count.set_limit("", "");
/* 121:    */     
/* 122:182 */     ConnectorResultSet result = select(count);
/* 123:183 */     return result.get("DHX_COUNT");
/* 124:    */   }
/* 125:    */   
/* 126:    */   public ConnectorResultSet get_variants(DataRequest source)
/* 127:    */     throws ConnectorOperationException
/* 128:    */   {
/* 129:191 */     DataRequest data = new DataRequest(source);
/* 130:192 */     data.set_fieldset("DISTINCT " + this.config.db_names_list());
/* 131:193 */     data.set_sort(null);
/* 132:194 */     data.set_limit("", "");
/* 133:    */     
/* 134:196 */     return select(data);
/* 135:    */   }
/* 136:    */   
/* 137:    */   public void insert(DataAction data, DataRequest source)
/* 138:    */     throws ConnectorOperationException
/* 139:    */   {
/* 140:204 */     String sql = insert_query(data, source);
/* 141:205 */     data.success(get_new_id(query(sql)));
/* 142:    */   }
/* 143:    */   
/* 144:    */   public ConnectorResultSet select(DataRequest source)
/* 145:    */     throws ConnectorOperationException
/* 146:    */   {
/* 147:213 */     String select = source.get_fieldset();
/* 148:214 */     if (select.equals("")) {
/* 149:215 */       select = this.config.db_names_list();
/* 150:    */     }
/* 151:217 */     String where = build_where(source.get_filters(), source.get_relation());
/* 152:218 */     String sort = build_order(source.get_sort_by());
/* 153:219 */     return query(select_query(select, source.get_source(), where, sort, source.get_start(), source.get_count()));
/* 154:    */   }
/* 155:    */   
/* 156:    */   protected String select_query(String select, String from, String where, String sort, String start, String count)
/* 157:    */   {
/* 158:235 */     if (from == "") {
/* 159:236 */       return select;
/* 160:    */     }
/* 161:238 */     String sql = "SELECT " + select + " FROM " + from;
/* 162:239 */     if (!where.equals("")) {
/* 163:239 */       sql = sql + " WHERE " + where;
/* 164:    */     }
/* 165:240 */     if (!sort.equals("")) {
/* 166:240 */       sql = sql + " ORDER BY " + sort;
/* 167:    */     }
/* 168:241 */     if ((!start.equals("")) || (!count.equals(""))) {
/* 169:241 */       sql = sql + " LIMIT " + start + "," + count;
/* 170:    */     }
/* 171:242 */     return sql;
/* 172:    */   }
/* 173:    */   
/* 174:    */   protected String build_where(ArrayList<FilteringRule> rules, String relation)
/* 175:    */   {
/* 176:254 */     ArrayList<String> sql = new ArrayList();
/* 177:255 */     for (int i = 0; i < rules.size(); i++) {
/* 178:256 */       sql.add(((FilteringRule)rules.get(i)).to_sql(this));
/* 179:    */     }
/* 180:258 */     if (!relation.equals("")) {
/* 181:259 */       sql.add(this.config.relation_id.db_name + "='" + escape(relation) + "'");
/* 182:    */     }
/* 183:260 */     return ConnectorUtils.join(sql.toArray(), " AND ");
/* 184:    */   }
/* 185:    */   
/* 186:    */   protected String build_order(ArrayList<SortingRule> sorts)
/* 187:    */   {
/* 188:271 */     ArrayList<String> sql = new ArrayList();
/* 189:272 */     for (int i = 0; i < sorts.size(); i++) {
/* 190:273 */       sql.add(((SortingRule)sorts.get(i)).to_sql());
/* 191:    */     }
/* 192:275 */     return ConnectorUtils.join(sql.toArray(), ",");
/* 193:    */   }
/* 194:    */   
/* 195:    */   public void update(DataAction data, DataRequest source)
/* 196:    */     throws ConnectorOperationException
/* 197:    */   {
/* 198:283 */     String sql = update_query(data, source);
/* 199:284 */     query(sql);
/* 200:285 */     data.success();
/* 201:    */   }
/* 202:    */   
/* 203:    */   private String update_query(DataAction data, DataRequest source)
/* 204:    */   {
/* 205:297 */     StringBuffer sql = new StringBuffer();
/* 206:298 */     sql.append("UPDATE " + source.get_source() + " SET ");
/* 207:299 */     for (int i = 0; i < this.config.text.size(); i++)
/* 208:    */     {
/* 209:300 */       if (i != 0) {
/* 210:300 */         sql.append(",");
/* 211:    */       }
/* 212:302 */       ConnectorField field = (ConnectorField)this.config.text.get(i);
/* 213:303 */       sql.append(field.db_name + "='" + escape(data.get_value(field.name)) + "'");
/* 214:    */     }
/* 215:305 */     if (!this.config.relation_id.db_name.equals(""))
/* 216:    */     {
/* 217:306 */       if (this.config.text.size() > 0) {
/* 218:307 */         sql.append(",");
/* 219:    */       }
/* 220:308 */       sql.append(this.config.relation_id.db_name + "='" + escape(data.get_value(this.config.relation_id.name)) + "'");
/* 221:    */     }
/* 222:311 */     sql.append(" WHERE " + this.config.id.db_name + "='" + escape(data.get_id()) + "' ");
/* 223:    */     
/* 224:313 */     String where = build_where(source.get_filters(), source.get_relation());
/* 225:314 */     if (!where.equals("")) {
/* 226:315 */       sql.append(" AND (" + where + ")");
/* 227:    */     }
/* 228:317 */     return sql.toString();
/* 229:    */   }
/* 230:    */   
/* 231:    */   public void begin_transaction()
/* 232:    */     throws ConnectorOperationException
/* 233:    */   {
/* 234:324 */     query("BEGIN");
/* 235:    */   }
/* 236:    */   
/* 237:    */   public void commit_transaction()
/* 238:    */     throws ConnectorOperationException
/* 239:    */   {
/* 240:331 */     query("COMMIT");
/* 241:    */   }
/* 242:    */   
/* 243:    */   public void rollback_transaction()
/* 244:    */     throws ConnectorOperationException
/* 245:    */   {
/* 246:338 */     query("ROLLBACK");
/* 247:    */   }
/* 248:    */   
/* 249:    */   protected Statement getStatement()
/* 250:    */     throws SQLException
/* 251:    */   {
/* 252:350 */     Connection conn = get_connection();
/* 253:351 */     return get_connection().createStatement(1003, 1007);
/* 254:    */   }
/* 255:    */   
/* 256:    */   public ConnectorResultSet query(String data)
/* 257:    */     throws ConnectorOperationException
/* 258:    */   {
/* 259:364 */     LogManager.getInstance().log("DB query \n" + data + "\n\n");
/* 260:    */     
/* 261:    */ 
/* 262:367 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/* 263:    */     
/* 264:    */ 
/* 265:    */ 
/* 266:371 */     SqlRowSet r = jt.queryForRowSet(data);
/* 267:372 */     if ((r != null) && 
/* 268:373 */       (!r.first())) {
/* 269:375 */       r = null;
/* 270:    */     }
/* 271:378 */     return new ConnectorResultSet(r);
/* 272:    */   }
/* 273:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.DBDataWrapper
 * JD-Core Version:    0.7.0.1
 */