/*   1:    */ package org.springframework.jdbc.core.metadata;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.HashSet;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Set;
/*  10:    */ import javax.sql.DataSource;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.apache.commons.logging.LogFactory;
/*  13:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*  14:    */ import org.springframework.jdbc.core.namedparam.SqlParameterSource;
/*  15:    */ import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
/*  16:    */ import org.springframework.jdbc.support.JdbcUtils;
/*  17:    */ import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
/*  18:    */ 
/*  19:    */ public class TableMetaDataContext
/*  20:    */ {
/*  21: 48 */   protected final Log logger = LogFactory.getLog(getClass());
/*  22:    */   private String tableName;
/*  23:    */   private String catalogName;
/*  24:    */   private String schemaName;
/*  25: 60 */   private List<String> tableColumns = new ArrayList();
/*  26: 63 */   private boolean accessTableColumnMetaData = true;
/*  27: 66 */   private boolean overrideIncludeSynonymsDefault = false;
/*  28:    */   private TableMetaDataProvider metaDataProvider;
/*  29: 72 */   private boolean generatedKeyColumnsUsed = false;
/*  30:    */   NativeJdbcExtractor nativeJdbcExtractor;
/*  31:    */   
/*  32:    */   public void setTableName(String tableName)
/*  33:    */   {
/*  34: 82 */     this.tableName = tableName;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public String getTableName()
/*  38:    */   {
/*  39: 89 */     return this.tableName;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setCatalogName(String catalogName)
/*  43:    */   {
/*  44: 96 */     this.catalogName = catalogName;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String getCatalogName()
/*  48:    */   {
/*  49:103 */     return this.catalogName;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setSchemaName(String schemaName)
/*  53:    */   {
/*  54:110 */     this.schemaName = schemaName;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public String getSchemaName()
/*  58:    */   {
/*  59:117 */     return this.schemaName;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setAccessTableColumnMetaData(boolean accessTableColumnMetaData)
/*  63:    */   {
/*  64:124 */     this.accessTableColumnMetaData = accessTableColumnMetaData;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public boolean isAccessTableColumnMetaData()
/*  68:    */   {
/*  69:131 */     return this.accessTableColumnMetaData;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setOverrideIncludeSynonymsDefault(boolean override)
/*  73:    */   {
/*  74:139 */     this.overrideIncludeSynonymsDefault = override;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public boolean isOverrideIncludeSynonymsDefault()
/*  78:    */   {
/*  79:146 */     return this.overrideIncludeSynonymsDefault;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public List<String> getTableColumns()
/*  83:    */   {
/*  84:153 */     return this.tableColumns;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public boolean isGetGeneratedKeysSupported()
/*  88:    */   {
/*  89:161 */     return this.metaDataProvider.isGetGeneratedKeysSupported();
/*  90:    */   }
/*  91:    */   
/*  92:    */   public boolean isGetGeneratedKeysSimulated()
/*  93:    */   {
/*  94:170 */     return this.metaDataProvider.isGetGeneratedKeysSimulated();
/*  95:    */   }
/*  96:    */   
/*  97:    */   public String getSimulationQueryForGetGeneratedKey(String tableName, String keyColumnName)
/*  98:    */   {
/*  99:179 */     return this.metaDataProvider.getSimpleQueryForGetGeneratedKey(tableName, keyColumnName);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public boolean isGeneratedKeysColumnNameArraySupported()
/* 103:    */   {
/* 104:187 */     return this.metaDataProvider.isGeneratedKeysColumnNameArraySupported();
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor)
/* 108:    */   {
/* 109:194 */     this.nativeJdbcExtractor = nativeJdbcExtractor;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void processMetaData(DataSource dataSource, List<String> declaredColumns, String[] generatedKeyNames)
/* 113:    */   {
/* 114:205 */     this.metaDataProvider = TableMetaDataProviderFactory.createMetaDataProvider(dataSource, this, this.nativeJdbcExtractor);
/* 115:206 */     this.tableColumns = reconcileColumnsToUse(declaredColumns, generatedKeyNames);
/* 116:    */   }
/* 117:    */   
/* 118:    */   protected List<String> reconcileColumnsToUse(List<String> declaredColumns, String[] generatedKeyNames)
/* 119:    */   {
/* 120:215 */     if (generatedKeyNames.length > 0) {
/* 121:216 */       this.generatedKeyColumnsUsed = true;
/* 122:    */     }
/* 123:218 */     if (declaredColumns.size() > 0) {
/* 124:219 */       return new ArrayList(declaredColumns);
/* 125:    */     }
/* 126:221 */     Set<String> keys = new HashSet(generatedKeyNames.length);
/* 127:222 */     for (String key : generatedKeyNames) {
/* 128:223 */       keys.add(key.toUpperCase());
/* 129:    */     }
/* 130:225 */     List<String> columns = new ArrayList();
/* 131:226 */     for (TableParameterMetaData meta : this.metaDataProvider.getTableParameterMetaData()) {
/* 132:227 */       if (!keys.contains(meta.getParameterName().toUpperCase())) {
/* 133:228 */         columns.add(meta.getParameterName());
/* 134:    */       }
/* 135:    */     }
/* 136:231 */     return columns;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public List<Object> matchInParameterValuesWithInsertColumns(SqlParameterSource parameterSource)
/* 140:    */   {
/* 141:239 */     List<Object> values = new ArrayList();
/* 142:    */     
/* 143:    */ 
/* 144:242 */     Map caseInsensitiveParameterNames = 
/* 145:243 */       SqlParameterSourceUtils.extractCaseInsensitiveParameterNames(parameterSource);
/* 146:244 */     for (String column : this.tableColumns) {
/* 147:245 */       if (parameterSource.hasValue(column))
/* 148:    */       {
/* 149:246 */         values.add(SqlParameterSourceUtils.getTypedValue(parameterSource, column));
/* 150:    */       }
/* 151:    */       else
/* 152:    */       {
/* 153:249 */         String lowerCaseName = column.toLowerCase();
/* 154:250 */         if (parameterSource.hasValue(lowerCaseName))
/* 155:    */         {
/* 156:251 */           values.add(SqlParameterSourceUtils.getTypedValue(parameterSource, lowerCaseName));
/* 157:    */         }
/* 158:    */         else
/* 159:    */         {
/* 160:254 */           String propertyName = JdbcUtils.convertUnderscoreNameToPropertyName(column);
/* 161:255 */           if (parameterSource.hasValue(propertyName)) {
/* 162:256 */             values.add(SqlParameterSourceUtils.getTypedValue(parameterSource, propertyName));
/* 163:259 */           } else if (caseInsensitiveParameterNames.containsKey(lowerCaseName)) {
/* 164:260 */             values.add(
/* 165:261 */               SqlParameterSourceUtils.getTypedValue(parameterSource, 
/* 166:262 */               (String)caseInsensitiveParameterNames.get(lowerCaseName)));
/* 167:    */           } else {
/* 168:265 */             values.add(null);
/* 169:    */           }
/* 170:    */         }
/* 171:    */       }
/* 172:    */     }
/* 173:271 */     return values;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public List<Object> matchInParameterValuesWithInsertColumns(Map<String, Object> inParameters)
/* 177:    */   {
/* 178:279 */     List<Object> values = new ArrayList();
/* 179:280 */     Map<String, Object> source = new HashMap();
/* 180:281 */     for (String key : inParameters.keySet()) {
/* 181:282 */       source.put(key.toLowerCase(), inParameters.get(key));
/* 182:    */     }
/* 183:284 */     for (String column : this.tableColumns) {
/* 184:285 */       values.add(source.get(column.toLowerCase()));
/* 185:    */     }
/* 186:287 */     return values;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public String createInsertString(String[] generatedKeyNames)
/* 190:    */   {
/* 191:296 */     HashSet<String> keys = new HashSet(generatedKeyNames.length);
/* 192:297 */     for (String key : generatedKeyNames) {
/* 193:298 */       keys.add(key.toUpperCase());
/* 194:    */     }
/* 195:300 */     StringBuilder insertStatement = new StringBuilder();
/* 196:301 */     insertStatement.append("INSERT INTO ");
/* 197:302 */     if (getSchemaName() != null)
/* 198:    */     {
/* 199:303 */       insertStatement.append(getSchemaName());
/* 200:304 */       insertStatement.append(".");
/* 201:    */     }
/* 202:306 */     insertStatement.append(getTableName());
/* 203:307 */     insertStatement.append(" (");
/* 204:308 */     int columnCount = 0;
/* 205:309 */     for (??? = getTableColumns().iterator(); ((Iterator)???).hasNext();)
/* 206:    */     {
/* 207:309 */       String columnName = (String)((Iterator)???).next();
/* 208:310 */       if (!keys.contains(columnName.toUpperCase()))
/* 209:    */       {
/* 210:311 */         columnCount++;
/* 211:312 */         if (columnCount > 1) {
/* 212:313 */           insertStatement.append(", ");
/* 213:    */         }
/* 214:315 */         insertStatement.append(columnName);
/* 215:    */       }
/* 216:    */     }
/* 217:318 */     insertStatement.append(") VALUES(");
/* 218:319 */     if (columnCount < 1) {
/* 219:320 */       if (this.generatedKeyColumnsUsed) {
/* 220:321 */         this.logger.info("Unable to locate non-key columns for table '" + 
/* 221:322 */           getTableName() + "' so an empty insert statement is generated");
/* 222:    */       } else {
/* 223:325 */         throw new InvalidDataAccessApiUsageException("Unable to locate columns for table '" + 
/* 224:326 */           getTableName() + "' so an insert statement can't be generated");
/* 225:    */       }
/* 226:    */     }
/* 227:329 */     for (int i = 0; i < columnCount; i++)
/* 228:    */     {
/* 229:330 */       if (i > 0) {
/* 230:331 */         insertStatement.append(", ");
/* 231:    */       }
/* 232:333 */       insertStatement.append("?");
/* 233:    */     }
/* 234:335 */     insertStatement.append(")");
/* 235:336 */     return insertStatement.toString();
/* 236:    */   }
/* 237:    */   
/* 238:    */   public int[] createInsertTypes()
/* 239:    */   {
/* 240:344 */     int[] types = new int[getTableColumns().size()];
/* 241:345 */     List<TableParameterMetaData> parameters = this.metaDataProvider.getTableParameterMetaData();
/* 242:346 */     Map<String, TableParameterMetaData> parameterMap = new HashMap(parameters.size());
/* 243:347 */     for (TableParameterMetaData tpmd : parameters) {
/* 244:348 */       parameterMap.put(tpmd.getParameterName().toUpperCase(), tpmd);
/* 245:    */     }
/* 246:350 */     int typeIndx = 0;
/* 247:351 */     for (String column : getTableColumns())
/* 248:    */     {
/* 249:352 */       if (column == null)
/* 250:    */       {
/* 251:353 */         types[typeIndx] = -2147483648;
/* 252:    */       }
/* 253:    */       else
/* 254:    */       {
/* 255:356 */         TableParameterMetaData tpmd = (TableParameterMetaData)parameterMap.get(column.toUpperCase());
/* 256:357 */         if (tpmd != null) {
/* 257:358 */           types[typeIndx] = tpmd.getSqlType();
/* 258:    */         } else {
/* 259:361 */           types[typeIndx] = -2147483648;
/* 260:    */         }
/* 261:    */       }
/* 262:364 */       typeIndx++;
/* 263:    */     }
/* 264:366 */     return types;
/* 265:    */   }
/* 266:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.TableMetaDataContext
 * JD-Core Version:    0.7.0.1
 */