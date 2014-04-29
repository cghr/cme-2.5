/*   1:    */ package org.springframework.jdbc.core.metadata;
/*   2:    */ 
/*   3:    */ import java.sql.DatabaseMetaData;
/*   4:    */ import java.sql.ResultSet;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.List;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*  11:    */ import org.springframework.jdbc.core.SqlInOutParameter;
/*  12:    */ import org.springframework.jdbc.core.SqlOutParameter;
/*  13:    */ import org.springframework.jdbc.core.SqlParameter;
/*  14:    */ import org.springframework.util.StringUtils;
/*  15:    */ 
/*  16:    */ public class GenericCallMetaDataProvider
/*  17:    */   implements CallMetaDataProvider
/*  18:    */ {
/*  19: 45 */   protected static final Log logger = LogFactory.getLog(CallMetaDataProvider.class);
/*  20: 47 */   private boolean procedureColumnMetaDataUsed = false;
/*  21:    */   private String userName;
/*  22: 51 */   private boolean supportsCatalogsInProcedureCalls = true;
/*  23: 53 */   private boolean supportsSchemasInProcedureCalls = true;
/*  24: 55 */   private boolean storesUpperCaseIdentifiers = true;
/*  25: 57 */   private boolean storesLowerCaseIdentifiers = false;
/*  26: 59 */   private List<CallParameterMetaData> callParameterMetaData = new ArrayList();
/*  27:    */   
/*  28:    */   protected GenericCallMetaDataProvider(DatabaseMetaData databaseMetaData)
/*  29:    */     throws SQLException
/*  30:    */   {
/*  31: 67 */     this.userName = databaseMetaData.getUserName();
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void initializeWithMetaData(DatabaseMetaData databaseMetaData)
/*  35:    */     throws SQLException
/*  36:    */   {
/*  37:    */     try
/*  38:    */     {
/*  39: 73 */       setSupportsCatalogsInProcedureCalls(databaseMetaData.supportsCatalogsInProcedureCalls());
/*  40:    */     }
/*  41:    */     catch (SQLException se)
/*  42:    */     {
/*  43: 76 */       logger.warn("Error retrieving 'DatabaseMetaData.supportsCatalogsInProcedureCalls' - " + se.getMessage());
/*  44:    */     }
/*  45:    */     try
/*  46:    */     {
/*  47: 79 */       setSupportsSchemasInProcedureCalls(databaseMetaData.supportsSchemasInProcedureCalls());
/*  48:    */     }
/*  49:    */     catch (SQLException se)
/*  50:    */     {
/*  51: 82 */       logger.warn("Error retrieving 'DatabaseMetaData.supportsSchemasInProcedureCalls' - " + se.getMessage());
/*  52:    */     }
/*  53:    */     try
/*  54:    */     {
/*  55: 85 */       setStoresUpperCaseIdentifiers(databaseMetaData.storesUpperCaseIdentifiers());
/*  56:    */     }
/*  57:    */     catch (SQLException se)
/*  58:    */     {
/*  59: 88 */       logger.warn("Error retrieving 'DatabaseMetaData.storesUpperCaseIdentifiers' - " + se.getMessage());
/*  60:    */     }
/*  61:    */     try
/*  62:    */     {
/*  63: 91 */       setStoresLowerCaseIdentifiers(databaseMetaData.storesLowerCaseIdentifiers());
/*  64:    */     }
/*  65:    */     catch (SQLException se)
/*  66:    */     {
/*  67: 94 */       logger.warn("Error retrieving 'DatabaseMetaData.storesLowerCaseIdentifiers' - " + se.getMessage());
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void initializeWithProcedureColumnMetaData(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, String procedureName)
/*  72:    */     throws SQLException
/*  73:    */   {
/*  74:101 */     this.procedureColumnMetaDataUsed = true;
/*  75:102 */     processProcedureColumns(databaseMetaData, catalogName, schemaName, procedureName);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public List<CallParameterMetaData> getCallParameterMetaData()
/*  79:    */   {
/*  80:106 */     return this.callParameterMetaData;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public String procedureNameToUse(String procedureName)
/*  84:    */   {
/*  85:110 */     if (procedureName == null) {
/*  86:111 */       return null;
/*  87:    */     }
/*  88:112 */     if (isStoresUpperCaseIdentifiers()) {
/*  89:113 */       return procedureName.toUpperCase();
/*  90:    */     }
/*  91:114 */     if (isStoresLowerCaseIdentifiers()) {
/*  92:115 */       return procedureName.toLowerCase();
/*  93:    */     }
/*  94:117 */     return procedureName;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public String catalogNameToUse(String catalogName)
/*  98:    */   {
/*  99:121 */     if (catalogName == null) {
/* 100:122 */       return null;
/* 101:    */     }
/* 102:123 */     if (isStoresUpperCaseIdentifiers()) {
/* 103:124 */       return catalogName.toUpperCase();
/* 104:    */     }
/* 105:125 */     if (isStoresLowerCaseIdentifiers()) {
/* 106:126 */       return catalogName.toLowerCase();
/* 107:    */     }
/* 108:128 */     return catalogName;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public String schemaNameToUse(String schemaName)
/* 112:    */   {
/* 113:132 */     if (schemaName == null) {
/* 114:133 */       return null;
/* 115:    */     }
/* 116:134 */     if (isStoresUpperCaseIdentifiers()) {
/* 117:135 */       return schemaName.toUpperCase();
/* 118:    */     }
/* 119:136 */     if (isStoresLowerCaseIdentifiers()) {
/* 120:137 */       return schemaName.toLowerCase();
/* 121:    */     }
/* 122:139 */     return schemaName;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public String metaDataCatalogNameToUse(String catalogName)
/* 126:    */   {
/* 127:143 */     if (isSupportsCatalogsInProcedureCalls()) {
/* 128:144 */       return catalogNameToUse(catalogName);
/* 129:    */     }
/* 130:147 */     return null;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public String metaDataSchemaNameToUse(String schemaName)
/* 134:    */   {
/* 135:152 */     if (isSupportsSchemasInProcedureCalls()) {
/* 136:153 */       return schemaNameToUse(schemaName);
/* 137:    */     }
/* 138:156 */     return null;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public String parameterNameToUse(String parameterName)
/* 142:    */   {
/* 143:161 */     if (parameterName == null) {
/* 144:162 */       return null;
/* 145:    */     }
/* 146:164 */     if (isStoresUpperCaseIdentifiers()) {
/* 147:165 */       return parameterName.toUpperCase();
/* 148:    */     }
/* 149:167 */     if (isStoresLowerCaseIdentifiers()) {
/* 150:168 */       return parameterName.toLowerCase();
/* 151:    */     }
/* 152:171 */     return parameterName;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public boolean byPassReturnParameter(String parameterName)
/* 156:    */   {
/* 157:176 */     return false;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public SqlParameter createDefaultOutParameter(String parameterName, CallParameterMetaData meta)
/* 161:    */   {
/* 162:180 */     return new SqlOutParameter(parameterName, meta.getSqlType());
/* 163:    */   }
/* 164:    */   
/* 165:    */   public SqlParameter createDefaultInOutParameter(String parameterName, CallParameterMetaData meta)
/* 166:    */   {
/* 167:184 */     return new SqlInOutParameter(parameterName, meta.getSqlType());
/* 168:    */   }
/* 169:    */   
/* 170:    */   public SqlParameter createDefaultInParameter(String parameterName, CallParameterMetaData meta)
/* 171:    */   {
/* 172:188 */     return new SqlParameter(parameterName, meta.getSqlType());
/* 173:    */   }
/* 174:    */   
/* 175:    */   public String getUserName()
/* 176:    */   {
/* 177:192 */     return this.userName;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public boolean isReturnResultSetSupported()
/* 181:    */   {
/* 182:196 */     return true;
/* 183:    */   }
/* 184:    */   
/* 185:    */   public boolean isRefCursorSupported()
/* 186:    */   {
/* 187:200 */     return false;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public int getRefCursorSqlType()
/* 191:    */   {
/* 192:204 */     return 1111;
/* 193:    */   }
/* 194:    */   
/* 195:    */   public boolean isProcedureColumnMetaDataUsed()
/* 196:    */   {
/* 197:208 */     return this.procedureColumnMetaDataUsed;
/* 198:    */   }
/* 199:    */   
/* 200:    */   protected void setSupportsCatalogsInProcedureCalls(boolean supportsCatalogsInProcedureCalls)
/* 201:    */   {
/* 202:216 */     this.supportsCatalogsInProcedureCalls = supportsCatalogsInProcedureCalls;
/* 203:    */   }
/* 204:    */   
/* 205:    */   public boolean isSupportsCatalogsInProcedureCalls()
/* 206:    */   {
/* 207:223 */     return this.supportsCatalogsInProcedureCalls;
/* 208:    */   }
/* 209:    */   
/* 210:    */   protected void setSupportsSchemasInProcedureCalls(boolean supportsSchemasInProcedureCalls)
/* 211:    */   {
/* 212:230 */     this.supportsSchemasInProcedureCalls = supportsSchemasInProcedureCalls;
/* 213:    */   }
/* 214:    */   
/* 215:    */   public boolean isSupportsSchemasInProcedureCalls()
/* 216:    */   {
/* 217:237 */     return this.supportsSchemasInProcedureCalls;
/* 218:    */   }
/* 219:    */   
/* 220:    */   protected void setStoresUpperCaseIdentifiers(boolean storesUpperCaseIdentifiers)
/* 221:    */   {
/* 222:244 */     this.storesUpperCaseIdentifiers = storesUpperCaseIdentifiers;
/* 223:    */   }
/* 224:    */   
/* 225:    */   protected boolean isStoresUpperCaseIdentifiers()
/* 226:    */   {
/* 227:251 */     return this.storesUpperCaseIdentifiers;
/* 228:    */   }
/* 229:    */   
/* 230:    */   protected void setStoresLowerCaseIdentifiers(boolean storesLowerCaseIdentifiers)
/* 231:    */   {
/* 232:258 */     this.storesLowerCaseIdentifiers = storesLowerCaseIdentifiers;
/* 233:    */   }
/* 234:    */   
/* 235:    */   protected boolean isStoresLowerCaseIdentifiers()
/* 236:    */   {
/* 237:265 */     return this.storesLowerCaseIdentifiers;
/* 238:    */   }
/* 239:    */   
/* 240:    */   private void processProcedureColumns(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, String procedureName)
/* 241:    */   {
/* 242:273 */     ResultSet procs = null;
/* 243:274 */     String metaDataCatalogName = metaDataCatalogNameToUse(catalogName);
/* 244:275 */     String metaDataSchemaName = metaDataSchemaNameToUse(schemaName);
/* 245:276 */     String metaDataProcedureName = procedureNameToUse(procedureName);
/* 246:277 */     if (logger.isDebugEnabled()) {
/* 247:278 */       logger.debug("Retrieving metadata for " + metaDataCatalogName + "/" + 
/* 248:279 */         metaDataSchemaName + "/" + metaDataProcedureName);
/* 249:    */     }
/* 250:    */     try
/* 251:    */     {
/* 252:282 */       procs = databaseMetaData.getProcedures(metaDataCatalogName, metaDataSchemaName, metaDataProcedureName);
/* 253:283 */       List<String> found = new ArrayList();
/* 254:284 */       while (procs.next()) {
/* 255:285 */         found.add(procs.getString("PROCEDURE_CAT") + "." + procs.getString("PROCEDURE_SCHEM") + 
/* 256:286 */           "." + procs.getString("PROCEDURE_NAME"));
/* 257:    */       }
/* 258:288 */       procs.close();
/* 259:289 */       if (found.size() > 1) {
/* 260:290 */         throw new InvalidDataAccessApiUsageException("Unable to determine the correct call signature - multiple procedures/functions/signatures for " + 
/* 261:291 */           metaDataProcedureName + " found " + found);
/* 262:    */       }
/* 263:293 */       if ((found.size() < 1) && 
/* 264:294 */         (metaDataProcedureName.contains(".")) && (!StringUtils.hasText(metaDataCatalogName)))
/* 265:    */       {
/* 266:295 */         String packageName = metaDataProcedureName.substring(0, metaDataProcedureName.indexOf("."));
/* 267:296 */         throw new InvalidDataAccessApiUsageException("Unable to determine the correct call signature for " + 
/* 268:297 */           metaDataProcedureName + " - package name should be specified separately using " + 
/* 269:298 */           "'.withCatalogName(\"" + packageName + "\")'");
/* 270:    */       }
/* 271:302 */       procs = databaseMetaData.getProcedureColumns(
/* 272:303 */         metaDataCatalogName, metaDataSchemaName, metaDataProcedureName, null);
/* 273:304 */       while (procs.next())
/* 274:    */       {
/* 275:305 */         String columnName = procs.getString("COLUMN_NAME");
/* 276:306 */         int columnType = procs.getInt("COLUMN_TYPE");
/* 277:307 */         if ((columnName == null) && (
/* 278:308 */           (columnType == 1) || 
/* 279:309 */           (columnType == 2) || 
/* 280:310 */           (columnType == 4)))
/* 281:    */         {
/* 282:311 */           if (logger.isDebugEnabled()) {
/* 283:312 */             logger.debug("Skipping metadata for: " + 
/* 284:313 */               columnName + 
/* 285:314 */               " " + columnType + 
/* 286:315 */               " " + procs.getInt("DATA_TYPE") + 
/* 287:316 */               " " + procs.getString("TYPE_NAME") + 
/* 288:317 */               " " + procs.getBoolean("NULLABLE") + 
/* 289:318 */               " (probably a member of a collection)");
/* 290:    */           }
/* 291:    */         }
/* 292:    */         else
/* 293:    */         {
/* 294:323 */           CallParameterMetaData meta = new CallParameterMetaData(columnName, columnType, 
/* 295:324 */             procs.getInt("DATA_TYPE"), procs.getString("TYPE_NAME"), procs.getBoolean("NULLABLE"));
/* 296:    */           
/* 297:326 */           this.callParameterMetaData.add(meta);
/* 298:327 */           if (logger.isDebugEnabled()) {
/* 299:328 */             logger.debug("Retrieved metadata: " + meta.getParameterName() + " " + 
/* 300:329 */               meta.getParameterType() + " " + meta.getSqlType() + 
/* 301:330 */               " " + meta.getTypeName() + " " + meta.isNullable());
/* 302:    */           }
/* 303:    */         }
/* 304:    */       }
/* 305:    */     }
/* 306:    */     catch (SQLException ex)
/* 307:    */     {
/* 308:337 */       logger.warn("Error while retrieving metadata for procedure columns: " + ex);
/* 309:    */       try
/* 310:    */       {
/* 311:341 */         if (procs != null) {
/* 312:342 */           procs.close();
/* 313:    */         }
/* 314:    */       }
/* 315:    */       catch (SQLException ex)
/* 316:    */       {
/* 317:346 */         logger.warn("Problem closing ResultSet for procedure column metadata: " + ex);
/* 318:    */       }
/* 319:    */     }
/* 320:    */     finally
/* 321:    */     {
/* 322:    */       try
/* 323:    */       {
/* 324:341 */         if (procs != null) {
/* 325:342 */           procs.close();
/* 326:    */         }
/* 327:    */       }
/* 328:    */       catch (SQLException ex)
/* 329:    */       {
/* 330:346 */         logger.warn("Problem closing ResultSet for procedure column metadata: " + ex);
/* 331:    */       }
/* 332:    */     }
/* 333:    */   }
/* 334:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider
 * JD-Core Version:    0.7.0.1
 */