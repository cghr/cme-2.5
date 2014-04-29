/*   1:    */ package org.springframework.jdbc.core.simple;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.PreparedStatement;
/*   5:    */ import java.sql.ResultSet;
/*   6:    */ import java.sql.SQLException;
/*   7:    */ import java.sql.Statement;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.Arrays;
/*  10:    */ import java.util.Collections;
/*  11:    */ import java.util.HashMap;
/*  12:    */ import java.util.List;
/*  13:    */ import java.util.Map;
/*  14:    */ import javax.sql.DataSource;
/*  15:    */ import org.apache.commons.logging.Log;
/*  16:    */ import org.apache.commons.logging.LogFactory;
/*  17:    */ import org.springframework.dao.DataAccessException;
/*  18:    */ import org.springframework.dao.DataIntegrityViolationException;
/*  19:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*  20:    */ import org.springframework.dao.InvalidDataAccessResourceUsageException;
/*  21:    */ import org.springframework.jdbc.core.BatchPreparedStatementSetter;
/*  22:    */ import org.springframework.jdbc.core.ConnectionCallback;
/*  23:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  24:    */ import org.springframework.jdbc.core.PreparedStatementCreator;
/*  25:    */ import org.springframework.jdbc.core.StatementCreatorUtils;
/*  26:    */ import org.springframework.jdbc.core.metadata.TableMetaDataContext;
/*  27:    */ import org.springframework.jdbc.core.namedparam.SqlParameterSource;
/*  28:    */ import org.springframework.jdbc.support.GeneratedKeyHolder;
/*  29:    */ import org.springframework.jdbc.support.JdbcUtils;
/*  30:    */ import org.springframework.jdbc.support.KeyHolder;
/*  31:    */ import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
/*  32:    */ import org.springframework.util.Assert;
/*  33:    */ 
/*  34:    */ public abstract class AbstractJdbcInsert
/*  35:    */ {
/*  36: 65 */   protected final Log logger = LogFactory.getLog(getClass());
/*  37:    */   private final JdbcTemplate jdbcTemplate;
/*  38: 71 */   private final TableMetaDataContext tableMetaDataContext = new TableMetaDataContext();
/*  39: 74 */   private final List<String> declaredColumns = new ArrayList();
/*  40: 81 */   private boolean compiled = false;
/*  41:    */   private String insertString;
/*  42:    */   private int[] insertTypes;
/*  43: 90 */   private String[] generatedKeyNames = new String[0];
/*  44:    */   
/*  45:    */   protected AbstractJdbcInsert(DataSource dataSource)
/*  46:    */   {
/*  47: 97 */     this.jdbcTemplate = new JdbcTemplate(dataSource);
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected AbstractJdbcInsert(JdbcTemplate jdbcTemplate)
/*  51:    */   {
/*  52:104 */     Assert.notNull(jdbcTemplate, "JdbcTemplate must not be null");
/*  53:105 */     this.jdbcTemplate = jdbcTemplate;
/*  54:106 */     setNativeJdbcExtractor(jdbcTemplate.getNativeJdbcExtractor());
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setTableName(String tableName)
/*  58:    */   {
/*  59:118 */     checkIfConfigurationModificationIsAllowed();
/*  60:119 */     this.tableMetaDataContext.setTableName(tableName);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public String getTableName()
/*  64:    */   {
/*  65:126 */     return this.tableMetaDataContext.getTableName();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setSchemaName(String schemaName)
/*  69:    */   {
/*  70:133 */     checkIfConfigurationModificationIsAllowed();
/*  71:134 */     this.tableMetaDataContext.setSchemaName(schemaName);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public String getSchemaName()
/*  75:    */   {
/*  76:141 */     return this.tableMetaDataContext.getSchemaName();
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setCatalogName(String catalogName)
/*  80:    */   {
/*  81:148 */     checkIfConfigurationModificationIsAllowed();
/*  82:149 */     this.tableMetaDataContext.setCatalogName(catalogName);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public String getCatalogName()
/*  86:    */   {
/*  87:156 */     return this.tableMetaDataContext.getCatalogName();
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setColumnNames(List<String> columnNames)
/*  91:    */   {
/*  92:163 */     checkIfConfigurationModificationIsAllowed();
/*  93:164 */     this.declaredColumns.clear();
/*  94:165 */     this.declaredColumns.addAll(columnNames);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public List<String> getColumnNames()
/*  98:    */   {
/*  99:172 */     return Collections.unmodifiableList(this.declaredColumns);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public String[] getGeneratedKeyNames()
/* 103:    */   {
/* 104:179 */     return this.generatedKeyNames;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void setGeneratedKeyNames(String[] generatedKeyNames)
/* 108:    */   {
/* 109:186 */     checkIfConfigurationModificationIsAllowed();
/* 110:187 */     this.generatedKeyNames = generatedKeyNames;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setGeneratedKeyName(String generatedKeyName)
/* 114:    */   {
/* 115:194 */     checkIfConfigurationModificationIsAllowed();
/* 116:195 */     this.generatedKeyNames = new String[] { generatedKeyName };
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setAccessTableColumnMetaData(boolean accessTableColumnMetaData)
/* 120:    */   {
/* 121:202 */     this.tableMetaDataContext.setAccessTableColumnMetaData(accessTableColumnMetaData);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setOverrideIncludeSynonymsDefault(boolean override)
/* 125:    */   {
/* 126:209 */     this.tableMetaDataContext.setOverrideIncludeSynonymsDefault(override);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor)
/* 130:    */   {
/* 131:216 */     this.tableMetaDataContext.setNativeJdbcExtractor(nativeJdbcExtractor);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public String getInsertString()
/* 135:    */   {
/* 136:223 */     return this.insertString;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public int[] getInsertTypes()
/* 140:    */   {
/* 141:230 */     return this.insertTypes;
/* 142:    */   }
/* 143:    */   
/* 144:    */   protected JdbcTemplate getJdbcTemplate()
/* 145:    */   {
/* 146:237 */     return this.jdbcTemplate;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public final synchronized void compile()
/* 150:    */     throws InvalidDataAccessApiUsageException
/* 151:    */   {
/* 152:253 */     if (!isCompiled())
/* 153:    */     {
/* 154:254 */       if (getTableName() == null) {
/* 155:255 */         throw new InvalidDataAccessApiUsageException("Table name is required");
/* 156:    */       }
/* 157:    */       try
/* 158:    */       {
/* 159:259 */         this.jdbcTemplate.afterPropertiesSet();
/* 160:    */       }
/* 161:    */       catch (IllegalArgumentException ex)
/* 162:    */       {
/* 163:262 */         throw new InvalidDataAccessApiUsageException(ex.getMessage());
/* 164:    */       }
/* 165:265 */       compileInternal();
/* 166:266 */       this.compiled = true;
/* 167:268 */       if (this.logger.isDebugEnabled()) {
/* 168:269 */         this.logger.debug("JdbcInsert for table [" + getTableName() + "] compiled");
/* 169:    */       }
/* 170:    */     }
/* 171:    */   }
/* 172:    */   
/* 173:    */   protected void compileInternal()
/* 174:    */   {
/* 175:280 */     this.tableMetaDataContext.processMetaData(getJdbcTemplate().getDataSource(), getColumnNames(), getGeneratedKeyNames());
/* 176:    */     
/* 177:282 */     this.insertString = this.tableMetaDataContext.createInsertString(getGeneratedKeyNames());
/* 178:    */     
/* 179:284 */     this.insertTypes = this.tableMetaDataContext.createInsertTypes();
/* 180:286 */     if (this.logger.isDebugEnabled()) {
/* 181:287 */       this.logger.debug("Compiled JdbcInsert. Insert string is [" + getInsertString() + "]");
/* 182:    */     }
/* 183:290 */     onCompileInternal();
/* 184:    */   }
/* 185:    */   
/* 186:    */   protected void onCompileInternal() {}
/* 187:    */   
/* 188:    */   public boolean isCompiled()
/* 189:    */   {
/* 190:305 */     return this.compiled;
/* 191:    */   }
/* 192:    */   
/* 193:    */   protected void checkCompiled()
/* 194:    */   {
/* 195:314 */     if (!isCompiled())
/* 196:    */     {
/* 197:315 */       this.logger.debug("JdbcInsert not compiled before execution - invoking compile");
/* 198:316 */       compile();
/* 199:    */     }
/* 200:    */   }
/* 201:    */   
/* 202:    */   protected void checkIfConfigurationModificationIsAllowed()
/* 203:    */   {
/* 204:325 */     if (isCompiled()) {
/* 205:326 */       throw new InvalidDataAccessApiUsageException("Configuration can't be altered once the class has been compiled or used.");
/* 206:    */     }
/* 207:    */   }
/* 208:    */   
/* 209:    */   protected int doExecute(Map<String, Object> args)
/* 210:    */   {
/* 211:342 */     checkCompiled();
/* 212:343 */     List<Object> values = matchInParameterValuesWithInsertColumns(args);
/* 213:344 */     return executeInsertInternal(values);
/* 214:    */   }
/* 215:    */   
/* 216:    */   protected int doExecute(SqlParameterSource parameterSource)
/* 217:    */   {
/* 218:354 */     checkCompiled();
/* 219:355 */     List<Object> values = matchInParameterValuesWithInsertColumns(parameterSource);
/* 220:356 */     return executeInsertInternal(values);
/* 221:    */   }
/* 222:    */   
/* 223:    */   private int executeInsertInternal(List<Object> values)
/* 224:    */   {
/* 225:363 */     if (this.logger.isDebugEnabled()) {
/* 226:364 */       this.logger.debug("The following parameters are used for insert " + getInsertString() + " with: " + values);
/* 227:    */     }
/* 228:366 */     int updateCount = this.jdbcTemplate.update(getInsertString(), values.toArray(), getInsertTypes());
/* 229:367 */     return updateCount;
/* 230:    */   }
/* 231:    */   
/* 232:    */   protected Number doExecuteAndReturnKey(Map<String, Object> args)
/* 233:    */   {
/* 234:378 */     checkCompiled();
/* 235:379 */     List<Object> values = matchInParameterValuesWithInsertColumns(args);
/* 236:380 */     return executeInsertAndReturnKeyInternal(values);
/* 237:    */   }
/* 238:    */   
/* 239:    */   protected Number doExecuteAndReturnKey(SqlParameterSource parameterSource)
/* 240:    */   {
/* 241:391 */     checkCompiled();
/* 242:392 */     List<Object> values = matchInParameterValuesWithInsertColumns(parameterSource);
/* 243:393 */     return executeInsertAndReturnKeyInternal(values);
/* 244:    */   }
/* 245:    */   
/* 246:    */   protected KeyHolder doExecuteAndReturnKeyHolder(Map<String, Object> args)
/* 247:    */   {
/* 248:404 */     checkCompiled();
/* 249:405 */     List<Object> values = matchInParameterValuesWithInsertColumns(args);
/* 250:406 */     return executeInsertAndReturnKeyHolderInternal(values);
/* 251:    */   }
/* 252:    */   
/* 253:    */   protected KeyHolder doExecuteAndReturnKeyHolder(SqlParameterSource parameterSource)
/* 254:    */   {
/* 255:417 */     checkCompiled();
/* 256:418 */     List<Object> values = matchInParameterValuesWithInsertColumns(parameterSource);
/* 257:419 */     return executeInsertAndReturnKeyHolderInternal(values);
/* 258:    */   }
/* 259:    */   
/* 260:    */   private Number executeInsertAndReturnKeyInternal(List<Object> values)
/* 261:    */   {
/* 262:426 */     KeyHolder kh = executeInsertAndReturnKeyHolderInternal(values);
/* 263:427 */     if ((kh != null) && (kh.getKey() != null)) {
/* 264:428 */       return kh.getKey();
/* 265:    */     }
/* 266:431 */     throw new DataIntegrityViolationException("Unable to retrieve the generated key for the insert: " + 
/* 267:432 */       getInsertString());
/* 268:    */   }
/* 269:    */   
/* 270:    */   private KeyHolder executeInsertAndReturnKeyHolderInternal(final List<Object> values)
/* 271:    */   {
/* 272:440 */     if (this.logger.isDebugEnabled()) {
/* 273:441 */       this.logger.debug("The following parameters are used for call " + getInsertString() + " with: " + values);
/* 274:    */     }
/* 275:443 */     final KeyHolder keyHolder = new GeneratedKeyHolder();
/* 276:444 */     if (this.tableMetaDataContext.isGetGeneratedKeysSupported())
/* 277:    */     {
/* 278:445 */       this.jdbcTemplate.update(
/* 279:446 */         new PreparedStatementCreator()
/* 280:    */         {
/* 281:    */           public PreparedStatement createPreparedStatement(Connection con)
/* 282:    */             throws SQLException
/* 283:    */           {
/* 284:448 */             PreparedStatement ps = AbstractJdbcInsert.this.prepareStatementForGeneratedKeys(con);
/* 285:449 */             AbstractJdbcInsert.this.setParameterValues(ps, values, AbstractJdbcInsert.this.getInsertTypes());
/* 286:450 */             return ps;
/* 287:    */           }
/* 288:453 */         }, keyHolder);
/* 289:    */     }
/* 290:    */     else
/* 291:    */     {
/* 292:456 */       if (!this.tableMetaDataContext.isGetGeneratedKeysSimulated()) {
/* 293:457 */         throw new InvalidDataAccessResourceUsageException(
/* 294:458 */           "The getGeneratedKeys feature is not supported by this database");
/* 295:    */       }
/* 296:460 */       if (getGeneratedKeyNames().length < 1) {
/* 297:461 */         throw new InvalidDataAccessApiUsageException("Generated Key Name(s) not specificed. Using the generated keys features requires specifying the name(s) of the generated column(s)");
/* 298:    */       }
/* 299:464 */       if (getGeneratedKeyNames().length > 1) {
/* 300:465 */         throw new InvalidDataAccessApiUsageException(
/* 301:466 */           "Current database only supports retreiving the key for a single column. There are " + 
/* 302:467 */           getGeneratedKeyNames().length + " columns specified: " + Arrays.asList(getGeneratedKeyNames()));
/* 303:    */       }
/* 304:472 */       final String keyQuery = this.tableMetaDataContext.getSimulationQueryForGetGeneratedKey(
/* 305:473 */         this.tableMetaDataContext.getTableName(), 
/* 306:474 */         getGeneratedKeyNames()[0]);
/* 307:475 */       Assert.notNull(keyQuery, "Query for simulating get generated keys can't be null");
/* 308:476 */       if (keyQuery.toUpperCase().startsWith("RETURNING"))
/* 309:    */       {
/* 310:477 */         Long key = Long.valueOf(this.jdbcTemplate.queryForLong(
/* 311:478 */           getInsertString() + " " + keyQuery, 
/* 312:479 */           values.toArray(new Object[values.size()])));
/* 313:480 */         HashMap keys = new HashMap(1);
/* 314:481 */         keys.put(getGeneratedKeyNames()[0], key);
/* 315:482 */         keyHolder.getKeyList().add(keys);
/* 316:    */       }
/* 317:    */       else
/* 318:    */       {
/* 319:485 */         this.jdbcTemplate.execute(new ConnectionCallback()
/* 320:    */         {
/* 321:    */           public Object doInConnection(Connection con)
/* 322:    */             throws SQLException, DataAccessException
/* 323:    */           {
/* 324:488 */             PreparedStatement ps = null;
/* 325:    */             try
/* 326:    */             {
/* 327:490 */               ps = con.prepareStatement(AbstractJdbcInsert.this.getInsertString());
/* 328:491 */               AbstractJdbcInsert.this.setParameterValues(ps, values, AbstractJdbcInsert.this.getInsertTypes());
/* 329:492 */               ps.executeUpdate();
/* 330:    */             }
/* 331:    */             finally
/* 332:    */             {
/* 333:494 */               JdbcUtils.closeStatement(ps);
/* 334:    */             }
/* 335:497 */             Statement keyStmt = null;
/* 336:498 */             ResultSet rs = null;
/* 337:499 */             HashMap keys = new HashMap(1);
/* 338:    */             try
/* 339:    */             {
/* 340:501 */               keyStmt = con.createStatement();
/* 341:502 */               rs = keyStmt.executeQuery(keyQuery);
/* 342:503 */               if (rs.next())
/* 343:    */               {
/* 344:504 */                 long key = rs.getLong(1);
/* 345:505 */                 keys.put(AbstractJdbcInsert.this.getGeneratedKeyNames()[0], Long.valueOf(key));
/* 346:506 */                 keyHolder.getKeyList().add(keys);
/* 347:    */               }
/* 348:    */             }
/* 349:    */             finally
/* 350:    */             {
/* 351:509 */               JdbcUtils.closeResultSet(rs);
/* 352:510 */               JdbcUtils.closeStatement(keyStmt);
/* 353:    */             }
/* 354:512 */             return null;
/* 355:    */           }
/* 356:    */         });
/* 357:    */       }
/* 358:516 */       return keyHolder;
/* 359:    */     }
/* 360:518 */     return keyHolder;
/* 361:    */   }
/* 362:    */   
/* 363:    */   private PreparedStatement prepareStatementForGeneratedKeys(Connection con)
/* 364:    */     throws SQLException
/* 365:    */   {
/* 366:529 */     if (getGeneratedKeyNames().length < 1) {
/* 367:530 */       throw new InvalidDataAccessApiUsageException("Generated Key Name(s) not specificed. Using the generated keys features requires specifying the name(s) of the generated column(s)");
/* 368:    */     }
/* 369:    */     PreparedStatement ps;
/* 370:    */     PreparedStatement ps;
/* 371:534 */     if (this.tableMetaDataContext.isGeneratedKeysColumnNameArraySupported())
/* 372:    */     {
/* 373:535 */       if (this.logger.isDebugEnabled()) {
/* 374:536 */         this.logger.debug("Using generated keys support with array of column names.");
/* 375:    */       }
/* 376:538 */       ps = con.prepareStatement(getInsertString(), getGeneratedKeyNames());
/* 377:    */     }
/* 378:    */     else
/* 379:    */     {
/* 380:541 */       if (this.logger.isDebugEnabled()) {
/* 381:542 */         this.logger.debug("Using generated keys support with Statement.RETURN_GENERATED_KEYS.");
/* 382:    */       }
/* 383:544 */       ps = con.prepareStatement(getInsertString(), 1);
/* 384:    */     }
/* 385:546 */     return ps;
/* 386:    */   }
/* 387:    */   
/* 388:    */   protected int[] doExecuteBatch(Map<String, Object>[] batch)
/* 389:    */   {
/* 390:556 */     checkCompiled();
/* 391:557 */     List[] batchValues = new ArrayList[batch.length];
/* 392:558 */     int i = 0;
/* 393:559 */     for (Map<String, Object> args : batch)
/* 394:    */     {
/* 395:560 */       List<Object> values = matchInParameterValuesWithInsertColumns(args);
/* 396:561 */       batchValues[(i++)] = values;
/* 397:    */     }
/* 398:563 */     return executeBatchInternal(batchValues);
/* 399:    */   }
/* 400:    */   
/* 401:    */   protected int[] doExecuteBatch(SqlParameterSource[] batch)
/* 402:    */   {
/* 403:573 */     checkCompiled();
/* 404:574 */     List[] batchValues = new ArrayList[batch.length];
/* 405:575 */     int i = 0;
/* 406:576 */     for (SqlParameterSource parameterSource : batch)
/* 407:    */     {
/* 408:577 */       List<Object> values = matchInParameterValuesWithInsertColumns(parameterSource);
/* 409:578 */       batchValues[(i++)] = values;
/* 410:    */     }
/* 411:580 */     return executeBatchInternal(batchValues);
/* 412:    */   }
/* 413:    */   
/* 414:    */   private int[] executeBatchInternal(final List<Object>[] batchValues)
/* 415:    */   {
/* 416:588 */     if (this.logger.isDebugEnabled()) {
/* 417:589 */       this.logger.debug("Executing statement " + getInsertString() + " with batch of size: " + batchValues.length);
/* 418:    */     }
/* 419:591 */     int[] updateCounts = this.jdbcTemplate.batchUpdate(
/* 420:592 */       getInsertString(), 
/* 421:593 */       new BatchPreparedStatementSetter()
/* 422:    */       {
/* 423:    */         public void setValues(PreparedStatement ps, int i)
/* 424:    */           throws SQLException
/* 425:    */         {
/* 426:596 */           List<Object> values = batchValues[i];
/* 427:597 */           AbstractJdbcInsert.this.setParameterValues(ps, values, AbstractJdbcInsert.this.getInsertTypes());
/* 428:    */         }
/* 429:    */         
/* 430:    */         public int getBatchSize()
/* 431:    */         {
/* 432:601 */           return batchValues.length;
/* 433:    */         }
/* 434:603 */       });
/* 435:604 */     return updateCounts;
/* 436:    */   }
/* 437:    */   
/* 438:    */   private void setParameterValues(PreparedStatement preparedStatement, List<Object> values, int[] columnTypes)
/* 439:    */     throws SQLException
/* 440:    */   {
/* 441:614 */     int colIndex = 0;
/* 442:615 */     for (Object value : values)
/* 443:    */     {
/* 444:616 */       colIndex++;
/* 445:617 */       if ((columnTypes == null) || (colIndex > columnTypes.length)) {
/* 446:618 */         StatementCreatorUtils.setParameterValue(preparedStatement, colIndex, -2147483648, value);
/* 447:    */       } else {
/* 448:621 */         StatementCreatorUtils.setParameterValue(preparedStatement, colIndex, columnTypes[(colIndex - 1)], value);
/* 449:    */       }
/* 450:    */     }
/* 451:    */   }
/* 452:    */   
/* 453:    */   protected List<Object> matchInParameterValuesWithInsertColumns(SqlParameterSource parameterSource)
/* 454:    */   {
/* 455:634 */     return this.tableMetaDataContext.matchInParameterValuesWithInsertColumns(parameterSource);
/* 456:    */   }
/* 457:    */   
/* 458:    */   protected List<Object> matchInParameterValuesWithInsertColumns(Map<String, Object> args)
/* 459:    */   {
/* 460:645 */     return this.tableMetaDataContext.matchInParameterValuesWithInsertColumns(args);
/* 461:    */   }
/* 462:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.simple.AbstractJdbcInsert
 * JD-Core Version:    0.7.0.1
 */