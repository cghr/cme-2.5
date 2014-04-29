/*   1:    */ package org.springframework.jdbc.core.metadata;
/*   2:    */ 
/*   3:    */ import java.sql.DatabaseMetaData;
/*   4:    */ import java.sql.ResultSet;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Arrays;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.apache.commons.logging.LogFactory;
/*  13:    */ import org.springframework.dao.DataAccessResourceFailureException;
/*  14:    */ import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
/*  15:    */ 
/*  16:    */ public class GenericTableMetaDataProvider
/*  17:    */   implements TableMetaDataProvider
/*  18:    */ {
/*  19: 45 */   protected static final Log logger = LogFactory.getLog(TableMetaDataProvider.class);
/*  20: 48 */   private boolean tableColumnMetaDataUsed = false;
/*  21:    */   private String databaseVersion;
/*  22:    */   private String userName;
/*  23: 57 */   private boolean storesUpperCaseIdentifiers = true;
/*  24: 60 */   private boolean storesLowerCaseIdentifiers = false;
/*  25: 63 */   private boolean getGeneratedKeysSupported = true;
/*  26: 66 */   private boolean generatedKeysColumnNameArraySupported = true;
/*  27: 70 */   private List<String> productsNotSupportingGeneratedKeysColumnNameArray = Arrays.asList(new String[] { "Apache Derby", "HSQL Database Engine" });
/*  28: 73 */   private List<TableParameterMetaData> insertParameterMetaData = new ArrayList();
/*  29:    */   private NativeJdbcExtractor nativeJdbcExtractor;
/*  30:    */   
/*  31:    */   protected GenericTableMetaDataProvider(DatabaseMetaData databaseMetaData)
/*  32:    */     throws SQLException
/*  33:    */   {
/*  34: 84 */     this.userName = databaseMetaData.getUserName();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setStoresUpperCaseIdentifiers(boolean storesUpperCaseIdentifiers)
/*  38:    */   {
/*  39: 92 */     this.storesUpperCaseIdentifiers = storesUpperCaseIdentifiers;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public boolean isStoresUpperCaseIdentifiers()
/*  43:    */   {
/*  44: 99 */     return this.storesUpperCaseIdentifiers;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setStoresLowerCaseIdentifiers(boolean storesLowerCaseIdentifiers)
/*  48:    */   {
/*  49:106 */     this.storesLowerCaseIdentifiers = storesLowerCaseIdentifiers;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public boolean isStoresLowerCaseIdentifiers()
/*  53:    */   {
/*  54:113 */     return this.storesLowerCaseIdentifiers;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean isTableColumnMetaDataUsed()
/*  58:    */   {
/*  59:117 */     return this.tableColumnMetaDataUsed;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public List<TableParameterMetaData> getTableParameterMetaData()
/*  63:    */   {
/*  64:121 */     return this.insertParameterMetaData;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public boolean isGetGeneratedKeysSupported()
/*  68:    */   {
/*  69:125 */     return this.getGeneratedKeysSupported;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public boolean isGetGeneratedKeysSimulated()
/*  73:    */   {
/*  74:129 */     return false;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String getSimpleQueryForGetGeneratedKey(String tableName, String keyColumnName)
/*  78:    */   {
/*  79:133 */     return null;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void setGetGeneratedKeysSupported(boolean getGeneratedKeysSupported)
/*  83:    */   {
/*  84:140 */     this.getGeneratedKeysSupported = getGeneratedKeysSupported;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setGeneratedKeysColumnNameArraySupported(boolean generatedKeysColumnNameArraySupported)
/*  88:    */   {
/*  89:147 */     this.generatedKeysColumnNameArraySupported = generatedKeysColumnNameArraySupported;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public boolean isGeneratedKeysColumnNameArraySupported()
/*  93:    */   {
/*  94:151 */     return this.generatedKeysColumnNameArraySupported;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor)
/*  98:    */   {
/*  99:155 */     this.nativeJdbcExtractor = nativeJdbcExtractor;
/* 100:    */   }
/* 101:    */   
/* 102:    */   protected NativeJdbcExtractor getNativeJdbcExtractor()
/* 103:    */   {
/* 104:159 */     return this.nativeJdbcExtractor;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void initializeWithMetaData(DatabaseMetaData databaseMetaData)
/* 108:    */     throws SQLException
/* 109:    */   {
/* 110:    */     try
/* 111:    */     {
/* 112:165 */       if (databaseMetaData.supportsGetGeneratedKeys())
/* 113:    */       {
/* 114:166 */         logger.debug("GetGeneratedKeys is supported");
/* 115:167 */         setGetGeneratedKeysSupported(true);
/* 116:    */       }
/* 117:    */       else
/* 118:    */       {
/* 119:170 */         logger.debug("GetGeneratedKeys is not supported");
/* 120:171 */         setGetGeneratedKeysSupported(false);
/* 121:    */       }
/* 122:    */     }
/* 123:    */     catch (SQLException se)
/* 124:    */     {
/* 125:175 */       logger.warn("Error retrieving 'DatabaseMetaData.getGeneratedKeys' - " + se.getMessage());
/* 126:    */     }
/* 127:    */     try
/* 128:    */     {
/* 129:178 */       String databaseProductName = databaseMetaData.getDatabaseProductName();
/* 130:179 */       if (this.productsNotSupportingGeneratedKeysColumnNameArray.contains(databaseProductName))
/* 131:    */       {
/* 132:180 */         logger.debug("GeneratedKeysColumnNameArray is not supported for " + databaseProductName);
/* 133:181 */         setGeneratedKeysColumnNameArraySupported(false);
/* 134:    */       }
/* 135:    */       else
/* 136:    */       {
/* 137:184 */         logger.debug("GeneratedKeysColumnNameArray is supported for " + databaseProductName);
/* 138:185 */         setGeneratedKeysColumnNameArraySupported(true);
/* 139:    */       }
/* 140:    */     }
/* 141:    */     catch (SQLException se)
/* 142:    */     {
/* 143:189 */       logger.warn("Error retrieving 'DatabaseMetaData.getDatabaseProductName' - " + se.getMessage());
/* 144:    */     }
/* 145:    */     try
/* 146:    */     {
/* 147:192 */       this.databaseVersion = databaseMetaData.getDatabaseProductVersion();
/* 148:    */     }
/* 149:    */     catch (SQLException se)
/* 150:    */     {
/* 151:195 */       logger.warn("Error retrieving 'DatabaseMetaData.getDatabaseProductVersion' - " + se.getMessage());
/* 152:    */     }
/* 153:    */     try
/* 154:    */     {
/* 155:198 */       setStoresUpperCaseIdentifiers(databaseMetaData.storesUpperCaseIdentifiers());
/* 156:    */     }
/* 157:    */     catch (SQLException se)
/* 158:    */     {
/* 159:201 */       logger.warn("Error retrieving 'DatabaseMetaData.storesUpperCaseIdentifiers' - " + se.getMessage());
/* 160:    */     }
/* 161:    */     try
/* 162:    */     {
/* 163:204 */       setStoresLowerCaseIdentifiers(databaseMetaData.storesLowerCaseIdentifiers());
/* 164:    */     }
/* 165:    */     catch (SQLException se)
/* 166:    */     {
/* 167:207 */       logger.warn("Error retrieving 'DatabaseMetaData.storesLowerCaseIdentifiers' - " + se.getMessage());
/* 168:    */     }
/* 169:    */   }
/* 170:    */   
/* 171:    */   public void initializeWithTableColumnMetaData(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, String tableName)
/* 172:    */     throws SQLException
/* 173:    */   {
/* 174:215 */     this.tableColumnMetaDataUsed = true;
/* 175:216 */     locateTableAndProcessMetaData(databaseMetaData, catalogName, schemaName, tableName);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public String tableNameToUse(String tableName)
/* 179:    */   {
/* 180:220 */     if (tableName == null) {
/* 181:221 */       return null;
/* 182:    */     }
/* 183:223 */     if (isStoresUpperCaseIdentifiers()) {
/* 184:224 */       return tableName.toUpperCase();
/* 185:    */     }
/* 186:226 */     if (isStoresLowerCaseIdentifiers()) {
/* 187:227 */       return tableName.toLowerCase();
/* 188:    */     }
/* 189:230 */     return tableName;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public String catalogNameToUse(String catalogName)
/* 193:    */   {
/* 194:235 */     if (catalogName == null) {
/* 195:236 */       return null;
/* 196:    */     }
/* 197:238 */     if (isStoresUpperCaseIdentifiers()) {
/* 198:239 */       return catalogName.toUpperCase();
/* 199:    */     }
/* 200:241 */     if (isStoresLowerCaseIdentifiers()) {
/* 201:242 */       return catalogName.toLowerCase();
/* 202:    */     }
/* 203:245 */     return catalogName;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public String schemaNameToUse(String schemaName)
/* 207:    */   {
/* 208:250 */     if (schemaName == null) {
/* 209:251 */       return null;
/* 210:    */     }
/* 211:253 */     if (isStoresUpperCaseIdentifiers()) {
/* 212:254 */       return schemaName.toUpperCase();
/* 213:    */     }
/* 214:256 */     if (isStoresLowerCaseIdentifiers()) {
/* 215:257 */       return schemaName.toLowerCase();
/* 216:    */     }
/* 217:260 */     return schemaName;
/* 218:    */   }
/* 219:    */   
/* 220:    */   public String metaDataCatalogNameToUse(String catalogName)
/* 221:    */   {
/* 222:265 */     return catalogNameToUse(catalogName);
/* 223:    */   }
/* 224:    */   
/* 225:    */   public String metaDataSchemaNameToUse(String schemaName)
/* 226:    */   {
/* 227:269 */     if (schemaName == null) {
/* 228:270 */       return schemaNameToUse(getDefaultSchema());
/* 229:    */     }
/* 230:272 */     return schemaNameToUse(schemaName);
/* 231:    */   }
/* 232:    */   
/* 233:    */   protected String getDefaultSchema()
/* 234:    */   {
/* 235:279 */     return this.userName;
/* 236:    */   }
/* 237:    */   
/* 238:    */   protected String getDatabaseVersion()
/* 239:    */   {
/* 240:286 */     return this.databaseVersion;
/* 241:    */   }
/* 242:    */   
/* 243:    */   private void locateTableAndProcessMetaData(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, String tableName)
/* 244:    */   {
/* 245:295 */     Map<String, TableMetaData> tableMeta = new HashMap();
/* 246:296 */     ResultSet tables = null;
/* 247:    */     try
/* 248:    */     {
/* 249:298 */       tables = databaseMetaData.getTables(
/* 250:299 */         catalogNameToUse(catalogName), 
/* 251:300 */         schemaNameToUse(schemaName), 
/* 252:301 */         tableNameToUse(tableName), 
/* 253:302 */         null);
/* 254:    */       do
/* 255:    */       {
/* 256:304 */         TableMetaData tmd = new TableMetaData(null);
/* 257:305 */         tmd.setCatalogName(tables.getString("TABLE_CAT"));
/* 258:306 */         tmd.setSchemaName(tables.getString("TABLE_SCHEM"));
/* 259:307 */         tmd.setTableName(tables.getString("TABLE_NAME"));
/* 260:308 */         tmd.setType(tables.getString("TABLE_TYPE"));
/* 261:309 */         if (tmd.getSchemaName() == null) {
/* 262:310 */           tableMeta.put(this.userName.toUpperCase(), tmd);
/* 263:    */         } else {
/* 264:313 */           tableMeta.put(tmd.getSchemaName().toUpperCase(), tmd);
/* 265:    */         }
/* 266:303 */         if (tables == null) {
/* 267:    */           break;
/* 268:    */         }
/* 269:303 */       } while (tables.next());
/* 270:    */     }
/* 271:    */     catch (SQLException se)
/* 272:    */     {
/* 273:318 */       logger.warn("Error while accessing table meta data results" + se.getMessage());
/* 274:321 */       if (tables != null) {
/* 275:    */         try
/* 276:    */         {
/* 277:323 */           tables.close();
/* 278:    */         }
/* 279:    */         catch (SQLException e)
/* 280:    */         {
/* 281:325 */           logger.warn("Error while closing table meta data reults" + e.getMessage());
/* 282:    */         }
/* 283:    */       }
/* 284:    */     }
/* 285:    */     finally
/* 286:    */     {
/* 287:321 */       if (tables != null) {
/* 288:    */         try
/* 289:    */         {
/* 290:323 */           tables.close();
/* 291:    */         }
/* 292:    */         catch (SQLException e)
/* 293:    */         {
/* 294:325 */           logger.warn("Error while closing table meta data reults" + e.getMessage());
/* 295:    */         }
/* 296:    */       }
/* 297:    */     }
/* 298:330 */     if (tableMeta.size() < 1)
/* 299:    */     {
/* 300:331 */       logger.warn("Unable to locate table meta data for '" + tableName + "' -- column names must be provided");
/* 301:    */     }
/* 302:    */     else
/* 303:    */     {
/* 304:    */       TableMetaData tmd;
/* 305:335 */       if (schemaName == null)
/* 306:    */       {
/* 307:336 */         TableMetaData tmd = (TableMetaData)tableMeta.get(getDefaultSchema());
/* 308:337 */         if (tmd == null) {
/* 309:338 */           tmd = (TableMetaData)tableMeta.get(this.userName.toUpperCase());
/* 310:    */         }
/* 311:340 */         if (tmd == null) {
/* 312:341 */           tmd = (TableMetaData)tableMeta.get("PUBLIC");
/* 313:    */         }
/* 314:343 */         if (tmd == null) {
/* 315:344 */           tmd = (TableMetaData)tableMeta.get("DBO");
/* 316:    */         }
/* 317:346 */         if (tmd == null) {
/* 318:347 */           throw new DataAccessResourceFailureException("Unable to locate table meta data for '" + 
/* 319:348 */             tableName + "' in the default schema");
/* 320:    */         }
/* 321:    */       }
/* 322:    */       else
/* 323:    */       {
/* 324:352 */         tmd = (TableMetaData)tableMeta.get(schemaName.toUpperCase());
/* 325:353 */         if (tmd == null) {
/* 326:354 */           throw new DataAccessResourceFailureException("Unable to locate table meta data for '" + 
/* 327:355 */             tableName + "' in the '" + schemaName + "' schema");
/* 328:    */         }
/* 329:    */       }
/* 330:359 */       processTableColumns(databaseMetaData, tmd);
/* 331:    */     }
/* 332:    */   }
/* 333:    */   
/* 334:    */   private void processTableColumns(DatabaseMetaData databaseMetaData, TableMetaData tmd)
/* 335:    */   {
/* 336:367 */     ResultSet tableColumns = null;
/* 337:368 */     String metaDataCatalogName = metaDataCatalogNameToUse(tmd.getCatalogName());
/* 338:369 */     String metaDataSchemaName = metaDataSchemaNameToUse(tmd.getSchemaName());
/* 339:370 */     String metaDataTableName = tableNameToUse(tmd.getTableName());
/* 340:371 */     if (logger.isDebugEnabled()) {
/* 341:372 */       logger.debug("Retrieving metadata for " + metaDataCatalogName + "/" + 
/* 342:373 */         metaDataSchemaName + "/" + metaDataTableName);
/* 343:    */     }
/* 344:    */     try
/* 345:    */     {
/* 346:376 */       tableColumns = databaseMetaData.getColumns(
/* 347:377 */         metaDataCatalogName, 
/* 348:378 */         metaDataSchemaName, 
/* 349:379 */         metaDataTableName, 
/* 350:380 */         null);
/* 351:381 */       while (tableColumns.next())
/* 352:    */       {
/* 353:382 */         String columnName = tableColumns.getString("COLUMN_NAME");
/* 354:383 */         int dataType = tableColumns.getInt("DATA_TYPE");
/* 355:384 */         if (dataType == 3)
/* 356:    */         {
/* 357:385 */           String typeName = tableColumns.getString("TYPE_NAME");
/* 358:386 */           int decimalDigits = tableColumns.getInt("DECIMAL_DIGITS");
/* 359:390 */           if (("NUMBER".equals(typeName)) && (decimalDigits == 0))
/* 360:    */           {
/* 361:391 */             dataType = 2;
/* 362:392 */             if (logger.isDebugEnabled()) {
/* 363:393 */               logger.debug("Overriding metadata: " + 
/* 364:394 */                 columnName + 
/* 365:395 */                 " now using NUMERIC instead of DECIMAL");
/* 366:    */             }
/* 367:    */           }
/* 368:    */         }
/* 369:400 */         boolean nullable = tableColumns.getBoolean("NULLABLE");
/* 370:401 */         TableParameterMetaData meta = new TableParameterMetaData(
/* 371:402 */           columnName, 
/* 372:403 */           dataType, 
/* 373:404 */           nullable);
/* 374:    */         
/* 375:406 */         this.insertParameterMetaData.add(meta);
/* 376:407 */         if (logger.isDebugEnabled()) {
/* 377:408 */           logger.debug("Retrieved metadata: " + 
/* 378:409 */             meta.getParameterName() + 
/* 379:410 */             " " + meta.getSqlType() + 
/* 380:411 */             " " + meta.isNullable());
/* 381:    */         }
/* 382:    */       }
/* 383:    */     }
/* 384:    */     catch (SQLException se)
/* 385:    */     {
/* 386:417 */       logger.warn("Error while retrieving metadata for table columns: " + se.getMessage());
/* 387:    */       try
/* 388:    */       {
/* 389:421 */         if (tableColumns != null) {
/* 390:422 */           tableColumns.close();
/* 391:    */         }
/* 392:    */       }
/* 393:    */       catch (SQLException se)
/* 394:    */       {
/* 395:425 */         logger.warn("Problem closing ResultSet for table column metadata " + se.getMessage());
/* 396:    */       }
/* 397:    */     }
/* 398:    */     finally
/* 399:    */     {
/* 400:    */       try
/* 401:    */       {
/* 402:421 */         if (tableColumns != null) {
/* 403:422 */           tableColumns.close();
/* 404:    */         }
/* 405:    */       }
/* 406:    */       catch (SQLException se)
/* 407:    */       {
/* 408:425 */         logger.warn("Problem closing ResultSet for table column metadata " + se.getMessage());
/* 409:    */       }
/* 410:    */     }
/* 411:    */   }
/* 412:    */   
/* 413:    */   private static class TableMetaData
/* 414:    */   {
/* 415:    */     private String catalogName;
/* 416:    */     private String schemaName;
/* 417:    */     private String tableName;
/* 418:    */     private String type;
/* 419:    */     
/* 420:    */     public void setCatalogName(String catalogName)
/* 421:    */     {
/* 422:446 */       this.catalogName = catalogName;
/* 423:    */     }
/* 424:    */     
/* 425:    */     public String getCatalogName()
/* 426:    */     {
/* 427:450 */       return this.catalogName;
/* 428:    */     }
/* 429:    */     
/* 430:    */     public void setSchemaName(String schemaName)
/* 431:    */     {
/* 432:454 */       this.schemaName = schemaName;
/* 433:    */     }
/* 434:    */     
/* 435:    */     public String getSchemaName()
/* 436:    */     {
/* 437:458 */       return this.schemaName;
/* 438:    */     }
/* 439:    */     
/* 440:    */     public void setTableName(String tableName)
/* 441:    */     {
/* 442:462 */       this.tableName = tableName;
/* 443:    */     }
/* 444:    */     
/* 445:    */     public String getTableName()
/* 446:    */     {
/* 447:466 */       return this.tableName;
/* 448:    */     }
/* 449:    */     
/* 450:    */     public void setType(String type)
/* 451:    */     {
/* 452:470 */       this.type = type;
/* 453:    */     }
/* 454:    */     
/* 455:    */     public String getType()
/* 456:    */     {
/* 457:475 */       return this.type;
/* 458:    */     }
/* 459:    */   }
/* 460:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.GenericTableMetaDataProvider
 * JD-Core Version:    0.7.0.1
 */