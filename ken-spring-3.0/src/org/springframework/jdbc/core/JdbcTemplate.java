/*    1:     */ package org.springframework.jdbc.core;
/*    2:     */ 
/*    3:     */ import java.lang.reflect.InvocationHandler;
/*    4:     */ import java.lang.reflect.InvocationTargetException;
/*    5:     */ import java.lang.reflect.Method;
/*    6:     */ import java.lang.reflect.Proxy;
/*    7:     */ import java.sql.CallableStatement;
/*    8:     */ import java.sql.Connection;
/*    9:     */ import java.sql.PreparedStatement;
/*   10:     */ import java.sql.ResultSet;
/*   11:     */ import java.sql.SQLException;
/*   12:     */ import java.sql.SQLWarning;
/*   13:     */ import java.sql.Statement;
/*   14:     */ import java.util.ArrayList;
/*   15:     */ import java.util.Collection;
/*   16:     */ import java.util.Collections;
/*   17:     */ import java.util.HashMap;
/*   18:     */ import java.util.LinkedHashMap;
/*   19:     */ import java.util.List;
/*   20:     */ import java.util.Map;
/*   21:     */ import javax.sql.DataSource;
/*   22:     */ import org.apache.commons.logging.Log;
/*   23:     */ import org.springframework.dao.DataAccessException;
/*   24:     */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*   25:     */ import org.springframework.dao.support.DataAccessUtils;
/*   26:     */ import org.springframework.jdbc.SQLWarningException;
/*   27:     */ import org.springframework.jdbc.datasource.ConnectionProxy;
/*   28:     */ import org.springframework.jdbc.datasource.DataSourceUtils;
/*   29:     */ import org.springframework.jdbc.support.JdbcAccessor;
/*   30:     */ import org.springframework.jdbc.support.JdbcUtils;
/*   31:     */ import org.springframework.jdbc.support.KeyHolder;
/*   32:     */ import org.springframework.jdbc.support.SQLExceptionTranslator;
/*   33:     */ import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
/*   34:     */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*   35:     */ import org.springframework.util.Assert;
/*   36:     */ import org.springframework.util.LinkedCaseInsensitiveMap;
/*   37:     */ 
/*   38:     */ public class JdbcTemplate
/*   39:     */   extends JdbcAccessor
/*   40:     */   implements JdbcOperations
/*   41:     */ {
/*   42:     */   private static final String RETURN_RESULT_SET_PREFIX = "#result-set-";
/*   43:     */   private static final String RETURN_UPDATE_COUNT_PREFIX = "#update-count-";
/*   44:     */   private NativeJdbcExtractor nativeJdbcExtractor;
/*   45: 107 */   private boolean ignoreWarnings = true;
/*   46: 113 */   private int fetchSize = 0;
/*   47: 119 */   private int maxRows = 0;
/*   48: 125 */   private int queryTimeout = 0;
/*   49: 132 */   private boolean skipResultsProcessing = false;
/*   50: 140 */   private boolean skipUndeclaredResults = false;
/*   51: 147 */   private boolean resultsMapCaseInsensitive = false;
/*   52:     */   
/*   53:     */   public JdbcTemplate() {}
/*   54:     */   
/*   55:     */   public JdbcTemplate(DataSource dataSource)
/*   56:     */   {
/*   57: 164 */     setDataSource(dataSource);
/*   58: 165 */     afterPropertiesSet();
/*   59:     */   }
/*   60:     */   
/*   61:     */   public JdbcTemplate(DataSource dataSource, boolean lazyInit)
/*   62:     */   {
/*   63: 176 */     setDataSource(dataSource);
/*   64: 177 */     setLazyInit(lazyInit);
/*   65: 178 */     afterPropertiesSet();
/*   66:     */   }
/*   67:     */   
/*   68:     */   public void setNativeJdbcExtractor(NativeJdbcExtractor extractor)
/*   69:     */   {
/*   70: 189 */     this.nativeJdbcExtractor = extractor;
/*   71:     */   }
/*   72:     */   
/*   73:     */   public NativeJdbcExtractor getNativeJdbcExtractor()
/*   74:     */   {
/*   75: 196 */     return this.nativeJdbcExtractor;
/*   76:     */   }
/*   77:     */   
/*   78:     */   public void setIgnoreWarnings(boolean ignoreWarnings)
/*   79:     */   {
/*   80: 208 */     this.ignoreWarnings = ignoreWarnings;
/*   81:     */   }
/*   82:     */   
/*   83:     */   public boolean isIgnoreWarnings()
/*   84:     */   {
/*   85: 215 */     return this.ignoreWarnings;
/*   86:     */   }
/*   87:     */   
/*   88:     */   public void setFetchSize(int fetchSize)
/*   89:     */   {
/*   90: 227 */     this.fetchSize = fetchSize;
/*   91:     */   }
/*   92:     */   
/*   93:     */   public int getFetchSize()
/*   94:     */   {
/*   95: 234 */     return this.fetchSize;
/*   96:     */   }
/*   97:     */   
/*   98:     */   public void setMaxRows(int maxRows)
/*   99:     */   {
/*  100: 247 */     this.maxRows = maxRows;
/*  101:     */   }
/*  102:     */   
/*  103:     */   public int getMaxRows()
/*  104:     */   {
/*  105: 254 */     return this.maxRows;
/*  106:     */   }
/*  107:     */   
/*  108:     */   public void setQueryTimeout(int queryTimeout)
/*  109:     */   {
/*  110: 266 */     this.queryTimeout = queryTimeout;
/*  111:     */   }
/*  112:     */   
/*  113:     */   public int getQueryTimeout()
/*  114:     */   {
/*  115: 273 */     return this.queryTimeout;
/*  116:     */   }
/*  117:     */   
/*  118:     */   public void setSkipResultsProcessing(boolean skipResultsProcessing)
/*  119:     */   {
/*  120: 283 */     this.skipResultsProcessing = skipResultsProcessing;
/*  121:     */   }
/*  122:     */   
/*  123:     */   public boolean isSkipResultsProcessing()
/*  124:     */   {
/*  125: 290 */     return this.skipResultsProcessing;
/*  126:     */   }
/*  127:     */   
/*  128:     */   public void setSkipUndeclaredResults(boolean skipUndeclaredResults)
/*  129:     */   {
/*  130: 297 */     this.skipUndeclaredResults = skipUndeclaredResults;
/*  131:     */   }
/*  132:     */   
/*  133:     */   public boolean isSkipUndeclaredResults()
/*  134:     */   {
/*  135: 304 */     return this.skipUndeclaredResults;
/*  136:     */   }
/*  137:     */   
/*  138:     */   public void setResultsMapCaseInsensitive(boolean resultsMapCaseInsensitive)
/*  139:     */   {
/*  140: 312 */     this.resultsMapCaseInsensitive = resultsMapCaseInsensitive;
/*  141:     */   }
/*  142:     */   
/*  143:     */   public boolean isResultsMapCaseInsensitive()
/*  144:     */   {
/*  145: 320 */     return this.resultsMapCaseInsensitive;
/*  146:     */   }
/*  147:     */   
/*  148:     */   public <T> T execute(ConnectionCallback<T> action)
/*  149:     */     throws DataAccessException
/*  150:     */   {
/*  151: 329 */     Assert.notNull(action, "Callback object must not be null");
/*  152:     */     
/*  153: 331 */     Connection con = DataSourceUtils.getConnection(getDataSource());
/*  154:     */     try
/*  155:     */     {
/*  156: 333 */       Connection conToUse = con;
/*  157: 334 */       if (this.nativeJdbcExtractor != null) {
/*  158: 336 */         conToUse = this.nativeJdbcExtractor.getNativeConnection(con);
/*  159:     */       } else {
/*  160: 340 */         conToUse = createConnectionProxy(con);
/*  161:     */       }
/*  162: 342 */       return action.doInConnection(conToUse);
/*  163:     */     }
/*  164:     */     catch (SQLException ex)
/*  165:     */     {
/*  166: 347 */       DataSourceUtils.releaseConnection(con, getDataSource());
/*  167: 348 */       con = null;
/*  168: 349 */       throw getExceptionTranslator().translate("ConnectionCallback", getSql(action), ex);
/*  169:     */     }
/*  170:     */     finally
/*  171:     */     {
/*  172: 352 */       DataSourceUtils.releaseConnection(con, getDataSource());
/*  173:     */     }
/*  174:     */   }
/*  175:     */   
/*  176:     */   protected Connection createConnectionProxy(Connection con)
/*  177:     */   {
/*  178: 368 */     return (Connection)Proxy.newProxyInstance(
/*  179: 369 */       ConnectionProxy.class.getClassLoader(), 
/*  180: 370 */       new Class[] { ConnectionProxy.class }, 
/*  181: 371 */       new CloseSuppressingInvocationHandler(con));
/*  182:     */   }
/*  183:     */   
/*  184:     */   public <T> T execute(StatementCallback<T> action)
/*  185:     */     throws DataAccessException
/*  186:     */   {
/*  187: 380 */     Assert.notNull(action, "Callback object must not be null");
/*  188:     */     
/*  189: 382 */     Connection con = DataSourceUtils.getConnection(getDataSource());
/*  190: 383 */     Statement stmt = null;
/*  191:     */     try
/*  192:     */     {
/*  193: 385 */       Connection conToUse = con;
/*  194: 386 */       if ((this.nativeJdbcExtractor != null) && 
/*  195: 387 */         (this.nativeJdbcExtractor.isNativeConnectionNecessaryForNativeStatements())) {
/*  196: 388 */         conToUse = this.nativeJdbcExtractor.getNativeConnection(con);
/*  197:     */       }
/*  198: 390 */       stmt = conToUse.createStatement();
/*  199: 391 */       applyStatementSettings(stmt);
/*  200: 392 */       Statement stmtToUse = stmt;
/*  201: 393 */       if (this.nativeJdbcExtractor != null) {
/*  202: 394 */         stmtToUse = this.nativeJdbcExtractor.getNativeStatement(stmt);
/*  203:     */       }
/*  204: 396 */       T result = action.doInStatement(stmtToUse);
/*  205: 397 */       handleWarnings(stmt);
/*  206: 398 */       return result;
/*  207:     */     }
/*  208:     */     catch (SQLException ex)
/*  209:     */     {
/*  210: 403 */       JdbcUtils.closeStatement(stmt);
/*  211: 404 */       stmt = null;
/*  212: 405 */       DataSourceUtils.releaseConnection(con, getDataSource());
/*  213: 406 */       con = null;
/*  214: 407 */       throw getExceptionTranslator().translate("StatementCallback", getSql(action), ex);
/*  215:     */     }
/*  216:     */     finally
/*  217:     */     {
/*  218: 410 */       JdbcUtils.closeStatement(stmt);
/*  219: 411 */       DataSourceUtils.releaseConnection(con, getDataSource());
/*  220:     */     }
/*  221:     */   }
/*  222:     */   
/*  223:     */   public void execute(final String sql)
/*  224:     */     throws DataAccessException
/*  225:     */   {
/*  226: 416 */     if (this.logger.isDebugEnabled()) {
/*  227: 417 */       this.logger.debug("Executing SQL statement [" + sql + "]");
/*  228:     */     }
/*  229: 428 */     execute(new StatementCallback()
/*  230:     */     {
/*  231:     */       public Object doInStatement(Statement stmt)
/*  232:     */         throws SQLException
/*  233:     */       {
/*  234: 421 */         stmt.execute(sql);
/*  235: 422 */         return null;
/*  236:     */       }
/*  237:     */       
/*  238:     */       public String getSql()
/*  239:     */       {
/*  240: 425 */         return sql;
/*  241:     */       }
/*  242:     */     });
/*  243:     */   }
/*  244:     */   
/*  245:     */   public <T> T query(final String sql, final ResultSetExtractor<T> rse)
/*  246:     */     throws DataAccessException
/*  247:     */   {
/*  248: 432 */     Assert.notNull(sql, "SQL must not be null");
/*  249: 433 */     Assert.notNull(rse, "ResultSetExtractor must not be null");
/*  250: 434 */     if (this.logger.isDebugEnabled()) {
/*  251: 435 */       this.logger.debug("Executing SQL query [" + sql + "]");
/*  252:     */     }
/*  253: 456 */     execute(new StatementCallback()
/*  254:     */     {
/*  255:     */       public T doInStatement(Statement stmt)
/*  256:     */         throws SQLException
/*  257:     */       {
/*  258: 439 */         ResultSet rs = null;
/*  259:     */         try
/*  260:     */         {
/*  261: 441 */           rs = stmt.executeQuery(sql);
/*  262: 442 */           ResultSet rsToUse = rs;
/*  263: 443 */           if (JdbcTemplate.this.nativeJdbcExtractor != null) {
/*  264: 444 */             rsToUse = JdbcTemplate.this.nativeJdbcExtractor.getNativeResultSet(rs);
/*  265:     */           }
/*  266: 446 */           return rse.extractData(rsToUse);
/*  267:     */         }
/*  268:     */         finally
/*  269:     */         {
/*  270: 449 */           JdbcUtils.closeResultSet(rs);
/*  271:     */         }
/*  272:     */       }
/*  273:     */       
/*  274:     */       public String getSql()
/*  275:     */       {
/*  276: 453 */         return sql;
/*  277:     */       }
/*  278:     */     });
/*  279:     */   }
/*  280:     */   
/*  281:     */   public void query(String sql, RowCallbackHandler rch)
/*  282:     */     throws DataAccessException
/*  283:     */   {
/*  284: 460 */     query(sql, new RowCallbackHandlerResultSetExtractor(rch));
/*  285:     */   }
/*  286:     */   
/*  287:     */   public <T> List<T> query(String sql, RowMapper<T> rowMapper)
/*  288:     */     throws DataAccessException
/*  289:     */   {
/*  290: 464 */     return (List)query(sql, new RowMapperResultSetExtractor(rowMapper));
/*  291:     */   }
/*  292:     */   
/*  293:     */   public Map<String, Object> queryForMap(String sql)
/*  294:     */     throws DataAccessException
/*  295:     */   {
/*  296: 468 */     return (Map)queryForObject(sql, getColumnMapRowMapper());
/*  297:     */   }
/*  298:     */   
/*  299:     */   public <T> T queryForObject(String sql, RowMapper<T> rowMapper)
/*  300:     */     throws DataAccessException
/*  301:     */   {
/*  302: 472 */     List<T> results = query(sql, rowMapper);
/*  303: 473 */     return DataAccessUtils.requiredSingleResult(results);
/*  304:     */   }
/*  305:     */   
/*  306:     */   public <T> T queryForObject(String sql, Class<T> requiredType)
/*  307:     */     throws DataAccessException
/*  308:     */   {
/*  309: 477 */     return queryForObject(sql, getSingleColumnRowMapper(requiredType));
/*  310:     */   }
/*  311:     */   
/*  312:     */   public long queryForLong(String sql)
/*  313:     */     throws DataAccessException
/*  314:     */   {
/*  315: 481 */     Number number = (Number)queryForObject(sql, Long.class);
/*  316: 482 */     return number != null ? number.longValue() : 0L;
/*  317:     */   }
/*  318:     */   
/*  319:     */   public int queryForInt(String sql)
/*  320:     */     throws DataAccessException
/*  321:     */   {
/*  322: 486 */     Number number = (Number)queryForObject(sql, Integer.class);
/*  323: 487 */     return number != null ? number.intValue() : 0;
/*  324:     */   }
/*  325:     */   
/*  326:     */   public <T> List<T> queryForList(String sql, Class<T> elementType)
/*  327:     */     throws DataAccessException
/*  328:     */   {
/*  329: 491 */     return query(sql, getSingleColumnRowMapper(elementType));
/*  330:     */   }
/*  331:     */   
/*  332:     */   public List<Map<String, Object>> queryForList(String sql)
/*  333:     */     throws DataAccessException
/*  334:     */   {
/*  335: 495 */     return query(sql, getColumnMapRowMapper());
/*  336:     */   }
/*  337:     */   
/*  338:     */   public SqlRowSet queryForRowSet(String sql)
/*  339:     */     throws DataAccessException
/*  340:     */   {
/*  341: 499 */     return (SqlRowSet)query(sql, new SqlRowSetResultSetExtractor());
/*  342:     */   }
/*  343:     */   
/*  344:     */   public int update(final String sql)
/*  345:     */     throws DataAccessException
/*  346:     */   {
/*  347: 503 */     Assert.notNull(sql, "SQL must not be null");
/*  348: 504 */     if (this.logger.isDebugEnabled()) {
/*  349: 505 */       this.logger.debug("Executing SQL update [" + sql + "]");
/*  350:     */     }
/*  351: 519 */     ((Integer)execute(new StatementCallback()
/*  352:     */     {
/*  353:     */       public Integer doInStatement(Statement stmt)
/*  354:     */         throws SQLException
/*  355:     */       {
/*  356: 509 */         int rows = stmt.executeUpdate(sql);
/*  357: 510 */         if (JdbcTemplate.access$1(JdbcTemplate.this).isDebugEnabled()) {
/*  358: 511 */           JdbcTemplate.access$1(JdbcTemplate.this).debug("SQL update affected " + rows + " rows");
/*  359:     */         }
/*  360: 513 */         return Integer.valueOf(rows);
/*  361:     */       }
/*  362:     */       
/*  363:     */       public String getSql()
/*  364:     */       {
/*  365: 516 */         return sql;
/*  366:     */       }
/*  367:     */     })).intValue();
/*  368:     */   }
/*  369:     */   
/*  370:     */   public int[] batchUpdate(final String[] sql)
/*  371:     */     throws DataAccessException
/*  372:     */   {
/*  373: 523 */     Assert.notEmpty(sql, "SQL array must not be empty");
/*  374: 524 */     if (this.logger.isDebugEnabled()) {
/*  375: 525 */       this.logger.debug("Executing SQL batch update of " + sql.length + " statements");
/*  376:     */     }
/*  377: 555 */     (int[])execute(new StatementCallback()
/*  378:     */     {
/*  379:     */       private String currSql;
/*  380:     */       
/*  381:     */       public int[] doInStatement(Statement stmt)
/*  382:     */         throws SQLException, DataAccessException
/*  383:     */       {
/*  384: 530 */         int[] rowsAffected = new int[sql.length];
/*  385: 531 */         if (JdbcUtils.supportsBatchUpdates(stmt.getConnection()))
/*  386:     */         {
/*  387: 532 */           for (String sqlStmt : sql)
/*  388:     */           {
/*  389: 533 */             this.currSql = sqlStmt;
/*  390: 534 */             stmt.addBatch(sqlStmt);
/*  391:     */           }
/*  392: 536 */           rowsAffected = stmt.executeBatch();
/*  393:     */         }
/*  394:     */         else
/*  395:     */         {
/*  396: 539 */           for (int i = 0; i < sql.length; i++)
/*  397:     */           {
/*  398: 540 */             this.currSql = sql[i];
/*  399: 541 */             if (!stmt.execute(sql[i])) {
/*  400: 542 */               rowsAffected[i] = stmt.getUpdateCount();
/*  401:     */             } else {
/*  402: 545 */               throw new InvalidDataAccessApiUsageException("Invalid batch SQL statement: " + sql[i]);
/*  403:     */             }
/*  404:     */           }
/*  405:     */         }
/*  406: 549 */         return rowsAffected;
/*  407:     */       }
/*  408:     */       
/*  409:     */       public String getSql()
/*  410:     */       {
/*  411: 552 */         return this.currSql;
/*  412:     */       }
/*  413:     */     });
/*  414:     */   }
/*  415:     */   
/*  416:     */   public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action)
/*  417:     */     throws DataAccessException
/*  418:     */   {
/*  419: 566 */     Assert.notNull(psc, "PreparedStatementCreator must not be null");
/*  420: 567 */     Assert.notNull(action, "Callback object must not be null");
/*  421: 568 */     if (this.logger.isDebugEnabled())
/*  422:     */     {
/*  423: 569 */       String sql = getSql(psc);
/*  424: 570 */       this.logger.debug("Executing prepared SQL statement" + (sql != null ? " [" + sql + "]" : ""));
/*  425:     */     }
/*  426: 573 */     Connection con = DataSourceUtils.getConnection(getDataSource());
/*  427: 574 */     PreparedStatement ps = null;
/*  428:     */     try
/*  429:     */     {
/*  430: 576 */       Connection conToUse = con;
/*  431: 577 */       if ((this.nativeJdbcExtractor != null) && 
/*  432: 578 */         (this.nativeJdbcExtractor.isNativeConnectionNecessaryForNativePreparedStatements())) {
/*  433: 579 */         conToUse = this.nativeJdbcExtractor.getNativeConnection(con);
/*  434:     */       }
/*  435: 581 */       ps = psc.createPreparedStatement(conToUse);
/*  436: 582 */       applyStatementSettings(ps);
/*  437: 583 */       PreparedStatement psToUse = ps;
/*  438: 584 */       if (this.nativeJdbcExtractor != null) {
/*  439: 585 */         psToUse = this.nativeJdbcExtractor.getNativePreparedStatement(ps);
/*  440:     */       }
/*  441: 587 */       T result = action.doInPreparedStatement(psToUse);
/*  442: 588 */       handleWarnings(ps);
/*  443: 589 */       return result;
/*  444:     */     }
/*  445:     */     catch (SQLException ex)
/*  446:     */     {
/*  447: 594 */       if ((psc instanceof ParameterDisposer)) {
/*  448: 595 */         ((ParameterDisposer)psc).cleanupParameters();
/*  449:     */       }
/*  450: 597 */       String sql = getSql(psc);
/*  451: 598 */       psc = null;
/*  452: 599 */       JdbcUtils.closeStatement(ps);
/*  453: 600 */       ps = null;
/*  454: 601 */       DataSourceUtils.releaseConnection(con, getDataSource());
/*  455: 602 */       con = null;
/*  456: 603 */       throw getExceptionTranslator().translate("PreparedStatementCallback", sql, ex);
/*  457:     */     }
/*  458:     */     finally
/*  459:     */     {
/*  460: 606 */       if ((psc instanceof ParameterDisposer)) {
/*  461: 607 */         ((ParameterDisposer)psc).cleanupParameters();
/*  462:     */       }
/*  463: 609 */       JdbcUtils.closeStatement(ps);
/*  464: 610 */       DataSourceUtils.releaseConnection(con, getDataSource());
/*  465:     */     }
/*  466:     */   }
/*  467:     */   
/*  468:     */   public <T> T execute(String sql, PreparedStatementCallback<T> action)
/*  469:     */     throws DataAccessException
/*  470:     */   {
/*  471: 615 */     return execute(new SimplePreparedStatementCreator(sql), action);
/*  472:     */   }
/*  473:     */   
/*  474:     */   public <T> T query(PreparedStatementCreator psc, final PreparedStatementSetter pss, final ResultSetExtractor<T> rse)
/*  475:     */     throws DataAccessException
/*  476:     */   {
/*  477: 634 */     Assert.notNull(rse, "ResultSetExtractor must not be null");
/*  478: 635 */     this.logger.debug("Executing prepared SQL query");
/*  479:     */     
/*  480: 637 */     execute(psc, new PreparedStatementCallback()
/*  481:     */     {
/*  482:     */       public T doInPreparedStatement(PreparedStatement ps)
/*  483:     */         throws SQLException
/*  484:     */       {
/*  485: 639 */         ResultSet rs = null;
/*  486:     */         try
/*  487:     */         {
/*  488: 641 */           if (pss != null) {
/*  489: 642 */             pss.setValues(ps);
/*  490:     */           }
/*  491: 644 */           rs = ps.executeQuery();
/*  492: 645 */           ResultSet rsToUse = rs;
/*  493: 646 */           if (JdbcTemplate.this.nativeJdbcExtractor != null) {
/*  494: 647 */             rsToUse = JdbcTemplate.this.nativeJdbcExtractor.getNativeResultSet(rs);
/*  495:     */           }
/*  496: 649 */           return rse.extractData(rsToUse);
/*  497:     */         }
/*  498:     */         finally
/*  499:     */         {
/*  500: 652 */           JdbcUtils.closeResultSet(rs);
/*  501: 653 */           if ((pss instanceof ParameterDisposer)) {
/*  502: 654 */             ((ParameterDisposer)pss).cleanupParameters();
/*  503:     */           }
/*  504:     */         }
/*  505:     */       }
/*  506:     */     });
/*  507:     */   }
/*  508:     */   
/*  509:     */   public <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse)
/*  510:     */     throws DataAccessException
/*  511:     */   {
/*  512: 662 */     return query(psc, null, rse);
/*  513:     */   }
/*  514:     */   
/*  515:     */   public <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse)
/*  516:     */     throws DataAccessException
/*  517:     */   {
/*  518: 666 */     return query(new SimplePreparedStatementCreator(sql), pss, rse);
/*  519:     */   }
/*  520:     */   
/*  521:     */   public <T> T query(String sql, Object[] args, int[] argTypes, ResultSetExtractor<T> rse)
/*  522:     */     throws DataAccessException
/*  523:     */   {
/*  524: 670 */     return query(sql, newArgTypePreparedStatementSetter(args, argTypes), rse);
/*  525:     */   }
/*  526:     */   
/*  527:     */   public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse)
/*  528:     */     throws DataAccessException
/*  529:     */   {
/*  530: 674 */     return query(sql, newArgPreparedStatementSetter(args), rse);
/*  531:     */   }
/*  532:     */   
/*  533:     */   public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args)
/*  534:     */     throws DataAccessException
/*  535:     */   {
/*  536: 678 */     return query(sql, newArgPreparedStatementSetter(args), rse);
/*  537:     */   }
/*  538:     */   
/*  539:     */   public void query(PreparedStatementCreator psc, RowCallbackHandler rch)
/*  540:     */     throws DataAccessException
/*  541:     */   {
/*  542: 682 */     query(psc, new RowCallbackHandlerResultSetExtractor(rch));
/*  543:     */   }
/*  544:     */   
/*  545:     */   public void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch)
/*  546:     */     throws DataAccessException
/*  547:     */   {
/*  548: 686 */     query(sql, pss, new RowCallbackHandlerResultSetExtractor(rch));
/*  549:     */   }
/*  550:     */   
/*  551:     */   public void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch)
/*  552:     */     throws DataAccessException
/*  553:     */   {
/*  554: 690 */     query(sql, newArgTypePreparedStatementSetter(args, argTypes), rch);
/*  555:     */   }
/*  556:     */   
/*  557:     */   public void query(String sql, Object[] args, RowCallbackHandler rch)
/*  558:     */     throws DataAccessException
/*  559:     */   {
/*  560: 694 */     query(sql, newArgPreparedStatementSetter(args), rch);
/*  561:     */   }
/*  562:     */   
/*  563:     */   public void query(String sql, RowCallbackHandler rch, Object... args)
/*  564:     */     throws DataAccessException
/*  565:     */   {
/*  566: 698 */     query(sql, newArgPreparedStatementSetter(args), rch);
/*  567:     */   }
/*  568:     */   
/*  569:     */   public <T> List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper)
/*  570:     */     throws DataAccessException
/*  571:     */   {
/*  572: 702 */     return (List)query(psc, new RowMapperResultSetExtractor(rowMapper));
/*  573:     */   }
/*  574:     */   
/*  575:     */   public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper)
/*  576:     */     throws DataAccessException
/*  577:     */   {
/*  578: 706 */     return (List)query(sql, pss, new RowMapperResultSetExtractor(rowMapper));
/*  579:     */   }
/*  580:     */   
/*  581:     */   public <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
/*  582:     */     throws DataAccessException
/*  583:     */   {
/*  584: 710 */     return (List)query(sql, args, argTypes, new RowMapperResultSetExtractor(rowMapper));
/*  585:     */   }
/*  586:     */   
/*  587:     */   public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper)
/*  588:     */     throws DataAccessException
/*  589:     */   {
/*  590: 714 */     return (List)query(sql, args, new RowMapperResultSetExtractor(rowMapper));
/*  591:     */   }
/*  592:     */   
/*  593:     */   public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args)
/*  594:     */     throws DataAccessException
/*  595:     */   {
/*  596: 718 */     return (List)query(sql, args, new RowMapperResultSetExtractor(rowMapper));
/*  597:     */   }
/*  598:     */   
/*  599:     */   public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
/*  600:     */     throws DataAccessException
/*  601:     */   {
/*  602: 724 */     List<T> results = (List)query(sql, args, argTypes, new RowMapperResultSetExtractor(rowMapper, 1));
/*  603: 725 */     return DataAccessUtils.requiredSingleResult(results);
/*  604:     */   }
/*  605:     */   
/*  606:     */   public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper)
/*  607:     */     throws DataAccessException
/*  608:     */   {
/*  609: 729 */     List<T> results = (List)query(sql, args, new RowMapperResultSetExtractor(rowMapper, 1));
/*  610: 730 */     return DataAccessUtils.requiredSingleResult(results);
/*  611:     */   }
/*  612:     */   
/*  613:     */   public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args)
/*  614:     */     throws DataAccessException
/*  615:     */   {
/*  616: 734 */     List<T> results = (List)query(sql, args, new RowMapperResultSetExtractor(rowMapper, 1));
/*  617: 735 */     return DataAccessUtils.requiredSingleResult(results);
/*  618:     */   }
/*  619:     */   
/*  620:     */   public <T> T queryForObject(String sql, Object[] args, int[] argTypes, Class<T> requiredType)
/*  621:     */     throws DataAccessException
/*  622:     */   {
/*  623: 741 */     return queryForObject(sql, args, argTypes, getSingleColumnRowMapper(requiredType));
/*  624:     */   }
/*  625:     */   
/*  626:     */   public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType)
/*  627:     */     throws DataAccessException
/*  628:     */   {
/*  629: 745 */     return queryForObject(sql, args, getSingleColumnRowMapper(requiredType));
/*  630:     */   }
/*  631:     */   
/*  632:     */   public <T> T queryForObject(String sql, Class<T> requiredType, Object... args)
/*  633:     */     throws DataAccessException
/*  634:     */   {
/*  635: 749 */     return queryForObject(sql, args, getSingleColumnRowMapper(requiredType));
/*  636:     */   }
/*  637:     */   
/*  638:     */   public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes)
/*  639:     */     throws DataAccessException
/*  640:     */   {
/*  641: 753 */     return (Map)queryForObject(sql, args, argTypes, getColumnMapRowMapper());
/*  642:     */   }
/*  643:     */   
/*  644:     */   public Map<String, Object> queryForMap(String sql, Object... args)
/*  645:     */     throws DataAccessException
/*  646:     */   {
/*  647: 757 */     return (Map)queryForObject(sql, args, getColumnMapRowMapper());
/*  648:     */   }
/*  649:     */   
/*  650:     */   public long queryForLong(String sql, Object[] args, int[] argTypes)
/*  651:     */     throws DataAccessException
/*  652:     */   {
/*  653: 761 */     Number number = (Number)queryForObject(sql, args, argTypes, Long.class);
/*  654: 762 */     return number != null ? number.longValue() : 0L;
/*  655:     */   }
/*  656:     */   
/*  657:     */   public long queryForLong(String sql, Object... args)
/*  658:     */     throws DataAccessException
/*  659:     */   {
/*  660: 766 */     Number number = (Number)queryForObject(sql, args, Long.class);
/*  661: 767 */     return number != null ? number.longValue() : 0L;
/*  662:     */   }
/*  663:     */   
/*  664:     */   public int queryForInt(String sql, Object[] args, int[] argTypes)
/*  665:     */     throws DataAccessException
/*  666:     */   {
/*  667: 771 */     Number number = (Number)queryForObject(sql, args, argTypes, Integer.class);
/*  668: 772 */     return number != null ? number.intValue() : 0;
/*  669:     */   }
/*  670:     */   
/*  671:     */   public int queryForInt(String sql, Object... args)
/*  672:     */     throws DataAccessException
/*  673:     */   {
/*  674: 776 */     Number number = (Number)queryForObject(sql, args, Integer.class);
/*  675: 777 */     return number != null ? number.intValue() : 0;
/*  676:     */   }
/*  677:     */   
/*  678:     */   public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType)
/*  679:     */     throws DataAccessException
/*  680:     */   {
/*  681: 781 */     return query(sql, args, argTypes, getSingleColumnRowMapper(elementType));
/*  682:     */   }
/*  683:     */   
/*  684:     */   public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType)
/*  685:     */     throws DataAccessException
/*  686:     */   {
/*  687: 785 */     return query(sql, args, getSingleColumnRowMapper(elementType));
/*  688:     */   }
/*  689:     */   
/*  690:     */   public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args)
/*  691:     */     throws DataAccessException
/*  692:     */   {
/*  693: 789 */     return query(sql, args, getSingleColumnRowMapper(elementType));
/*  694:     */   }
/*  695:     */   
/*  696:     */   public List<Map<String, Object>> queryForList(String sql, Object[] args, int[] argTypes)
/*  697:     */     throws DataAccessException
/*  698:     */   {
/*  699: 793 */     return query(sql, args, argTypes, getColumnMapRowMapper());
/*  700:     */   }
/*  701:     */   
/*  702:     */   public List<Map<String, Object>> queryForList(String sql, Object... args)
/*  703:     */     throws DataAccessException
/*  704:     */   {
/*  705: 797 */     return query(sql, args, getColumnMapRowMapper());
/*  706:     */   }
/*  707:     */   
/*  708:     */   public SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes)
/*  709:     */     throws DataAccessException
/*  710:     */   {
/*  711: 801 */     return (SqlRowSet)query(sql, args, argTypes, new SqlRowSetResultSetExtractor());
/*  712:     */   }
/*  713:     */   
/*  714:     */   public SqlRowSet queryForRowSet(String sql, Object... args)
/*  715:     */     throws DataAccessException
/*  716:     */   {
/*  717: 805 */     return (SqlRowSet)query(sql, args, new SqlRowSetResultSetExtractor());
/*  718:     */   }
/*  719:     */   
/*  720:     */   protected int update(PreparedStatementCreator psc, final PreparedStatementSetter pss)
/*  721:     */     throws DataAccessException
/*  722:     */   {
/*  723: 811 */     this.logger.debug("Executing prepared SQL update");
/*  724: 812 */     ((Integer)execute(psc, new PreparedStatementCallback()
/*  725:     */     {
/*  726:     */       public Integer doInPreparedStatement(PreparedStatement ps)
/*  727:     */         throws SQLException
/*  728:     */       {
/*  729:     */         try
/*  730:     */         {
/*  731: 815 */           if (pss != null) {
/*  732: 816 */             pss.setValues(ps);
/*  733:     */           }
/*  734: 818 */           int rows = ps.executeUpdate();
/*  735: 819 */           if (JdbcTemplate.access$1(JdbcTemplate.this).isDebugEnabled()) {
/*  736: 820 */             JdbcTemplate.access$1(JdbcTemplate.this).debug("SQL update affected " + rows + " rows");
/*  737:     */           }
/*  738: 822 */           return Integer.valueOf(rows);
/*  739:     */         }
/*  740:     */         finally
/*  741:     */         {
/*  742: 825 */           if ((pss instanceof ParameterDisposer)) {
/*  743: 826 */             ((ParameterDisposer)pss).cleanupParameters();
/*  744:     */           }
/*  745:     */         }
/*  746:     */       }
/*  747:     */     })).intValue();
/*  748:     */   }
/*  749:     */   
/*  750:     */   public int update(PreparedStatementCreator psc)
/*  751:     */     throws DataAccessException
/*  752:     */   {
/*  753: 834 */     return update(psc, null);
/*  754:     */   }
/*  755:     */   
/*  756:     */   public int update(PreparedStatementCreator psc, final KeyHolder generatedKeyHolder)
/*  757:     */     throws DataAccessException
/*  758:     */   {
/*  759: 840 */     Assert.notNull(generatedKeyHolder, "KeyHolder must not be null");
/*  760: 841 */     this.logger.debug("Executing SQL update and returning generated keys");
/*  761:     */     
/*  762: 843 */     ((Integer)execute(psc, new PreparedStatementCallback()
/*  763:     */     {
/*  764:     */       public Integer doInPreparedStatement(PreparedStatement ps)
/*  765:     */         throws SQLException
/*  766:     */       {
/*  767: 845 */         int rows = ps.executeUpdate();
/*  768: 846 */         List<Map<String, Object>> generatedKeys = generatedKeyHolder.getKeyList();
/*  769: 847 */         generatedKeys.clear();
/*  770: 848 */         ResultSet keys = ps.getGeneratedKeys();
/*  771: 849 */         if (keys != null) {
/*  772:     */           try
/*  773:     */           {
/*  774: 851 */             RowMapperResultSetExtractor<Map<String, Object>> rse = 
/*  775: 852 */               new RowMapperResultSetExtractor(JdbcTemplate.this.getColumnMapRowMapper(), 1);
/*  776: 853 */             generatedKeys.addAll((Collection)rse.extractData(keys));
/*  777:     */           }
/*  778:     */           finally
/*  779:     */           {
/*  780: 856 */             JdbcUtils.closeResultSet(keys);
/*  781:     */           }
/*  782:     */         }
/*  783: 859 */         if (JdbcTemplate.access$1(JdbcTemplate.this).isDebugEnabled()) {
/*  784: 860 */           JdbcTemplate.access$1(JdbcTemplate.this).debug("SQL update affected " + rows + " rows and returned " + generatedKeys.size() + " keys");
/*  785:     */         }
/*  786: 862 */         return Integer.valueOf(rows);
/*  787:     */       }
/*  788:     */     })).intValue();
/*  789:     */   }
/*  790:     */   
/*  791:     */   public int update(String sql, PreparedStatementSetter pss)
/*  792:     */     throws DataAccessException
/*  793:     */   {
/*  794: 868 */     return update(new SimplePreparedStatementCreator(sql), pss);
/*  795:     */   }
/*  796:     */   
/*  797:     */   public int update(String sql, Object[] args, int[] argTypes)
/*  798:     */     throws DataAccessException
/*  799:     */   {
/*  800: 872 */     return update(sql, newArgTypePreparedStatementSetter(args, argTypes));
/*  801:     */   }
/*  802:     */   
/*  803:     */   public int update(String sql, Object... args)
/*  804:     */     throws DataAccessException
/*  805:     */   {
/*  806: 876 */     return update(sql, newArgPreparedStatementSetter(args));
/*  807:     */   }
/*  808:     */   
/*  809:     */   public int[] batchUpdate(String sql, final BatchPreparedStatementSetter pss)
/*  810:     */     throws DataAccessException
/*  811:     */   {
/*  812: 880 */     if (this.logger.isDebugEnabled()) {
/*  813: 881 */       this.logger.debug("Executing SQL batch update [" + sql + "]");
/*  814:     */     }
/*  815: 884 */     (int[])execute(sql, new PreparedStatementCallback()
/*  816:     */     {
/*  817:     */       public int[] doInPreparedStatement(PreparedStatement ps)
/*  818:     */         throws SQLException
/*  819:     */       {
/*  820:     */         try
/*  821:     */         {
/*  822: 887 */           int batchSize = pss.getBatchSize();
/*  823: 888 */           InterruptibleBatchPreparedStatementSetter ipss = 
/*  824: 889 */             (pss instanceof InterruptibleBatchPreparedStatementSetter) ? 
/*  825: 890 */             (InterruptibleBatchPreparedStatementSetter)pss : null;
/*  826:     */           int[] arrayOfInt1;
/*  827: 891 */           if (JdbcUtils.supportsBatchUpdates(ps.getConnection()))
/*  828:     */           {
/*  829: 892 */             for (int i = 0; i < batchSize; i++)
/*  830:     */             {
/*  831: 893 */               pss.setValues(ps, i);
/*  832: 894 */               if ((ipss != null) && (ipss.isBatchExhausted(i))) {
/*  833:     */                 break;
/*  834:     */               }
/*  835: 897 */               ps.addBatch();
/*  836:     */             }
/*  837: 899 */             return ps.executeBatch();
/*  838:     */           }
/*  839:     */           InterruptibleBatchPreparedStatementSetter ipss;
/*  840:     */           int batchSize;
/*  841: 902 */           List<Integer> rowsAffected = new ArrayList();
/*  842: 903 */           for (int i = 0; i < batchSize; i++)
/*  843:     */           {
/*  844: 904 */             pss.setValues(ps, i);
/*  845: 905 */             if ((ipss != null) && (ipss.isBatchExhausted(i))) {
/*  846:     */               break;
/*  847:     */             }
/*  848: 908 */             rowsAffected.add(Integer.valueOf(ps.executeUpdate()));
/*  849:     */           }
/*  850: 910 */           int[] rowsAffectedArray = new int[rowsAffected.size()];
/*  851: 911 */           for (int i = 0; i < rowsAffectedArray.length; i++) {
/*  852: 912 */             rowsAffectedArray[i] = ((Integer)rowsAffected.get(i)).intValue();
/*  853:     */           }
/*  854: 914 */           return rowsAffectedArray;
/*  855:     */         }
/*  856:     */         finally
/*  857:     */         {
/*  858: 918 */           if ((pss instanceof ParameterDisposer)) {
/*  859: 919 */             ((ParameterDisposer)pss).cleanupParameters();
/*  860:     */           }
/*  861:     */         }
/*  862:     */       }
/*  863:     */     });
/*  864:     */   }
/*  865:     */   
/*  866:     */   public int[] batchUpdate(String sql, List<Object[]> batchArgs)
/*  867:     */   {
/*  868: 927 */     return batchUpdate(sql, batchArgs, new int[0]);
/*  869:     */   }
/*  870:     */   
/*  871:     */   public int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes)
/*  872:     */   {
/*  873: 931 */     return BatchUpdateUtils.executeBatchUpdate(sql, batchArgs, argTypes, this);
/*  874:     */   }
/*  875:     */   
/*  876:     */   public <T> int[][] batchUpdate(String sql, final Collection<T> batchArgs, final int batchSize, final ParameterizedPreparedStatementSetter<T> pss)
/*  877:     */   {
/*  878: 941 */     if (this.logger.isDebugEnabled()) {
/*  879: 942 */       this.logger.debug("Executing SQL batch update [" + sql + "] with a batch size of " + batchSize);
/*  880:     */     }
/*  881: 944 */     (int[][])execute(sql, new PreparedStatementCallback()
/*  882:     */     {
/*  883:     */       public int[][] doInPreparedStatement(PreparedStatement ps)
/*  884:     */         throws SQLException
/*  885:     */       {
/*  886: 946 */         List<int[]> rowsAffected = new ArrayList();
/*  887:     */         try
/*  888:     */         {
/*  889: 948 */           boolean batchSupported = true;
/*  890: 949 */           if (!JdbcUtils.supportsBatchUpdates(ps.getConnection()))
/*  891:     */           {
/*  892: 950 */             batchSupported = false;
/*  893: 951 */             JdbcTemplate.access$1(JdbcTemplate.this).warn("JDBC Driver does not support Batch updates; resorting to single statement execution");
/*  894:     */           }
/*  895: 953 */           int n = 0;
/*  896: 954 */           for (T obj : batchArgs)
/*  897:     */           {
/*  898: 955 */             pss.setValues(ps, obj);
/*  899: 956 */             n++;
/*  900: 957 */             if (batchSupported)
/*  901:     */             {
/*  902: 958 */               ps.addBatch();
/*  903: 959 */               if ((n % batchSize == 0) || (n == batchArgs.size()))
/*  904:     */               {
/*  905: 960 */                 if (JdbcTemplate.access$1(JdbcTemplate.this).isDebugEnabled())
/*  906:     */                 {
/*  907: 961 */                   int batchIdx = n % batchSize == 0 ? n / batchSize : n / batchSize + 1;
/*  908: 962 */                   int items = n - (n % batchSize == 0 ? n / batchSize - 1 : n / batchSize) * batchSize;
/*  909: 963 */                   JdbcTemplate.access$1(JdbcTemplate.this).debug("Sending SQL batch update #" + batchIdx + " with " + items + " items");
/*  910:     */                 }
/*  911: 965 */                 rowsAffected.add(ps.executeBatch());
/*  912:     */               }
/*  913:     */             }
/*  914:     */             else
/*  915:     */             {
/*  916: 969 */               int i = ps.executeUpdate();
/*  917: 970 */               rowsAffected.add(new int[] { i });
/*  918:     */             }
/*  919:     */           }
/*  920: 973 */           int[][] result = new int[rowsAffected.size()][];
/*  921: 974 */           for (int i = 0; i < result.length; i++) {
/*  922: 975 */             result[i] = ((int[])rowsAffected.get(i));
/*  923:     */           }
/*  924: 977 */           return result;
/*  925:     */         }
/*  926:     */         finally
/*  927:     */         {
/*  928: 979 */           if ((pss instanceof ParameterDisposer)) {
/*  929: 980 */             ((ParameterDisposer)pss).cleanupParameters();
/*  930:     */           }
/*  931:     */         }
/*  932:     */       }
/*  933:     */     });
/*  934:     */   }
/*  935:     */   
/*  936:     */   public <T> T execute(CallableStatementCreator csc, CallableStatementCallback<T> action)
/*  937:     */     throws DataAccessException
/*  938:     */   {
/*  939: 994 */     Assert.notNull(csc, "CallableStatementCreator must not be null");
/*  940: 995 */     Assert.notNull(action, "Callback object must not be null");
/*  941: 996 */     if (this.logger.isDebugEnabled())
/*  942:     */     {
/*  943: 997 */       String sql = getSql(csc);
/*  944: 998 */       this.logger.debug("Calling stored procedure" + (sql != null ? " [" + sql + "]" : ""));
/*  945:     */     }
/*  946:1001 */     Connection con = DataSourceUtils.getConnection(getDataSource());
/*  947:1002 */     CallableStatement cs = null;
/*  948:     */     try
/*  949:     */     {
/*  950:1004 */       Connection conToUse = con;
/*  951:1005 */       if (this.nativeJdbcExtractor != null) {
/*  952:1006 */         conToUse = this.nativeJdbcExtractor.getNativeConnection(con);
/*  953:     */       }
/*  954:1008 */       cs = csc.createCallableStatement(conToUse);
/*  955:1009 */       applyStatementSettings(cs);
/*  956:1010 */       CallableStatement csToUse = cs;
/*  957:1011 */       if (this.nativeJdbcExtractor != null) {
/*  958:1012 */         csToUse = this.nativeJdbcExtractor.getNativeCallableStatement(cs);
/*  959:     */       }
/*  960:1014 */       T result = action.doInCallableStatement(csToUse);
/*  961:1015 */       handleWarnings(cs);
/*  962:1016 */       return result;
/*  963:     */     }
/*  964:     */     catch (SQLException ex)
/*  965:     */     {
/*  966:1021 */       if ((csc instanceof ParameterDisposer)) {
/*  967:1022 */         ((ParameterDisposer)csc).cleanupParameters();
/*  968:     */       }
/*  969:1024 */       String sql = getSql(csc);
/*  970:1025 */       csc = null;
/*  971:1026 */       JdbcUtils.closeStatement(cs);
/*  972:1027 */       cs = null;
/*  973:1028 */       DataSourceUtils.releaseConnection(con, getDataSource());
/*  974:1029 */       con = null;
/*  975:1030 */       throw getExceptionTranslator().translate("CallableStatementCallback", sql, ex);
/*  976:     */     }
/*  977:     */     finally
/*  978:     */     {
/*  979:1033 */       if ((csc instanceof ParameterDisposer)) {
/*  980:1034 */         ((ParameterDisposer)csc).cleanupParameters();
/*  981:     */       }
/*  982:1036 */       JdbcUtils.closeStatement(cs);
/*  983:1037 */       DataSourceUtils.releaseConnection(con, getDataSource());
/*  984:     */     }
/*  985:     */   }
/*  986:     */   
/*  987:     */   public <T> T execute(String callString, CallableStatementCallback<T> action)
/*  988:     */     throws DataAccessException
/*  989:     */   {
/*  990:1042 */     return execute(new SimpleCallableStatementCreator(callString), action);
/*  991:     */   }
/*  992:     */   
/*  993:     */   public Map<String, Object> call(CallableStatementCreator csc, List<SqlParameter> declaredParameters)
/*  994:     */     throws DataAccessException
/*  995:     */   {
/*  996:1048 */     final List<SqlParameter> updateCountParameters = new ArrayList();
/*  997:1049 */     final List<SqlParameter> resultSetParameters = new ArrayList();
/*  998:1050 */     final List<SqlParameter> callParameters = new ArrayList();
/*  999:1051 */     for (SqlParameter parameter : declaredParameters) {
/* 1000:1052 */       if (parameter.isResultsParameter())
/* 1001:     */       {
/* 1002:1053 */         if ((parameter instanceof SqlReturnResultSet)) {
/* 1003:1054 */           resultSetParameters.add(parameter);
/* 1004:     */         } else {
/* 1005:1057 */           updateCountParameters.add(parameter);
/* 1006:     */         }
/* 1007:     */       }
/* 1008:     */       else {
/* 1009:1061 */         callParameters.add(parameter);
/* 1010:     */       }
/* 1011:     */     }
/* 1012:1064 */     (Map)execute(csc, new CallableStatementCallback()
/* 1013:     */     {
/* 1014:     */       public Map<String, Object> doInCallableStatement(CallableStatement cs)
/* 1015:     */         throws SQLException
/* 1016:     */       {
/* 1017:1066 */         boolean retVal = cs.execute();
/* 1018:1067 */         int updateCount = cs.getUpdateCount();
/* 1019:1068 */         if (JdbcTemplate.access$1(JdbcTemplate.this).isDebugEnabled())
/* 1020:     */         {
/* 1021:1069 */           JdbcTemplate.access$1(JdbcTemplate.this).debug("CallableStatement.execute() returned '" + retVal + "'");
/* 1022:1070 */           JdbcTemplate.access$1(JdbcTemplate.this).debug("CallableStatement.getUpdateCount() returned " + updateCount);
/* 1023:     */         }
/* 1024:1072 */         Map<String, Object> returnedResults = JdbcTemplate.this.createResultsMap();
/* 1025:1073 */         if ((retVal) || (updateCount != -1)) {
/* 1026:1074 */           returnedResults.putAll(JdbcTemplate.this.extractReturnedResults(cs, updateCountParameters, resultSetParameters, updateCount));
/* 1027:     */         }
/* 1028:1076 */         returnedResults.putAll(JdbcTemplate.this.extractOutputParameters(cs, callParameters));
/* 1029:1077 */         return returnedResults;
/* 1030:     */       }
/* 1031:     */     });
/* 1032:     */   }
/* 1033:     */   
/* 1034:     */   protected Map<String, Object> extractReturnedResults(CallableStatement cs, List updateCountParameters, List resultSetParameters, int updateCount)
/* 1035:     */     throws SQLException
/* 1036:     */   {
/* 1037:1093 */     Map<String, Object> returnedResults = new HashMap();
/* 1038:1094 */     int rsIndex = 0;
/* 1039:1095 */     int updateIndex = 0;
/* 1040:1097 */     if (!this.skipResultsProcessing)
/* 1041:     */     {
/* 1042:     */       boolean moreResults;
/* 1043:     */       do
/* 1044:     */       {
/* 1045:1099 */         if (updateCount == -1)
/* 1046:     */         {
/* 1047:1100 */           if ((resultSetParameters != null) && (resultSetParameters.size() > rsIndex))
/* 1048:     */           {
/* 1049:1101 */             SqlReturnResultSet declaredRsParam = (SqlReturnResultSet)resultSetParameters.get(rsIndex);
/* 1050:1102 */             returnedResults.putAll(processResultSet(cs.getResultSet(), declaredRsParam));
/* 1051:1103 */             rsIndex++;
/* 1052:     */           }
/* 1053:1106 */           else if (!this.skipUndeclaredResults)
/* 1054:     */           {
/* 1055:1107 */             String rsName = "#result-set-" + (rsIndex + 1);
/* 1056:1108 */             SqlReturnResultSet undeclaredRsParam = new SqlReturnResultSet(rsName, new ColumnMapRowMapper());
/* 1057:1109 */             this.logger.info("Added default SqlReturnResultSet parameter named " + rsName);
/* 1058:1110 */             returnedResults.putAll(processResultSet(cs.getResultSet(), undeclaredRsParam));
/* 1059:1111 */             rsIndex++;
/* 1060:     */           }
/* 1061:     */         }
/* 1062:1116 */         else if ((updateCountParameters != null) && (updateCountParameters.size() > updateIndex))
/* 1063:     */         {
/* 1064:1117 */           SqlReturnUpdateCount ucParam = (SqlReturnUpdateCount)updateCountParameters.get(updateIndex);
/* 1065:1118 */           String declaredUcName = ucParam.getName();
/* 1066:1119 */           returnedResults.put(declaredUcName, Integer.valueOf(updateCount));
/* 1067:1120 */           updateIndex++;
/* 1068:     */         }
/* 1069:1123 */         else if (!this.skipUndeclaredResults)
/* 1070:     */         {
/* 1071:1124 */           String undeclaredUcName = "#update-count-" + (updateIndex + 1);
/* 1072:1125 */           this.logger.info("Added default SqlReturnUpdateCount parameter named " + undeclaredUcName);
/* 1073:1126 */           returnedResults.put(undeclaredUcName, Integer.valueOf(updateCount));
/* 1074:1127 */           updateIndex++;
/* 1075:     */         }
/* 1076:1131 */         moreResults = cs.getMoreResults();
/* 1077:1132 */         updateCount = cs.getUpdateCount();
/* 1078:1133 */         if (this.logger.isDebugEnabled()) {
/* 1079:1134 */           this.logger.debug("CallableStatement.getUpdateCount() returned " + updateCount);
/* 1080:     */         }
/* 1081:1137 */       } while ((moreResults) || (updateCount != -1));
/* 1082:     */     }
/* 1083:1139 */     return returnedResults;
/* 1084:     */   }
/* 1085:     */   
/* 1086:     */   protected Map<String, Object> extractOutputParameters(CallableStatement cs, List<SqlParameter> parameters)
/* 1087:     */     throws SQLException
/* 1088:     */   {
/* 1089:1151 */     Map<String, Object> returnedResults = new HashMap();
/* 1090:1152 */     int sqlColIndex = 1;
/* 1091:1153 */     for (SqlParameter param : parameters)
/* 1092:     */     {
/* 1093:1154 */       if ((param instanceof SqlOutParameter))
/* 1094:     */       {
/* 1095:1155 */         SqlOutParameter outParam = (SqlOutParameter)param;
/* 1096:1156 */         if (outParam.isReturnTypeSupported())
/* 1097:     */         {
/* 1098:1157 */           Object out = outParam.getSqlReturnType().getTypeValue(
/* 1099:1158 */             cs, sqlColIndex, outParam.getSqlType(), outParam.getTypeName());
/* 1100:1159 */           returnedResults.put(outParam.getName(), out);
/* 1101:     */         }
/* 1102:     */         else
/* 1103:     */         {
/* 1104:1162 */           Object out = cs.getObject(sqlColIndex);
/* 1105:1163 */           if ((out instanceof ResultSet))
/* 1106:     */           {
/* 1107:1164 */             if (outParam.isResultSetSupported())
/* 1108:     */             {
/* 1109:1165 */               returnedResults.putAll(processResultSet((ResultSet)out, outParam));
/* 1110:     */             }
/* 1111:     */             else
/* 1112:     */             {
/* 1113:1168 */               String rsName = outParam.getName();
/* 1114:1169 */               SqlReturnResultSet rsParam = new SqlReturnResultSet(rsName, new ColumnMapRowMapper());
/* 1115:1170 */               returnedResults.putAll(processResultSet(cs.getResultSet(), rsParam));
/* 1116:1171 */               this.logger.info("Added default SqlReturnResultSet parameter named " + rsName);
/* 1117:     */             }
/* 1118:     */           }
/* 1119:     */           else {
/* 1120:1175 */             returnedResults.put(outParam.getName(), out);
/* 1121:     */           }
/* 1122:     */         }
/* 1123:     */       }
/* 1124:1179 */       if (!param.isResultsParameter()) {
/* 1125:1180 */         sqlColIndex++;
/* 1126:     */       }
/* 1127:     */     }
/* 1128:1183 */     return returnedResults;
/* 1129:     */   }
/* 1130:     */   
/* 1131:     */   protected Map<String, Object> processResultSet(ResultSet rs, ResultSetSupportingSqlParameter param)
/* 1132:     */     throws SQLException
/* 1133:     */   {
/* 1134:1194 */     if (rs == null) {
/* 1135:1195 */       return Collections.emptyMap();
/* 1136:     */     }
/* 1137:1197 */     Map<String, Object> returnedResults = new HashMap();
/* 1138:     */     try
/* 1139:     */     {
/* 1140:1199 */       ResultSet rsToUse = rs;
/* 1141:1200 */       if (this.nativeJdbcExtractor != null) {
/* 1142:1201 */         rsToUse = this.nativeJdbcExtractor.getNativeResultSet(rs);
/* 1143:     */       }
/* 1144:1203 */       if (param.getRowMapper() != null)
/* 1145:     */       {
/* 1146:1204 */         RowMapper rowMapper = param.getRowMapper();
/* 1147:1205 */         Object result = new RowMapperResultSetExtractor(rowMapper).extractData(rsToUse);
/* 1148:1206 */         returnedResults.put(param.getName(), result);
/* 1149:     */       }
/* 1150:1208 */       else if (param.getRowCallbackHandler() != null)
/* 1151:     */       {
/* 1152:1209 */         RowCallbackHandler rch = param.getRowCallbackHandler();
/* 1153:1210 */         new RowCallbackHandlerResultSetExtractor(rch).extractData(rsToUse);
/* 1154:1211 */         returnedResults.put(param.getName(), "ResultSet returned from stored procedure was processed");
/* 1155:     */       }
/* 1156:1213 */       else if (param.getResultSetExtractor() != null)
/* 1157:     */       {
/* 1158:1214 */         Object result = param.getResultSetExtractor().extractData(rsToUse);
/* 1159:1215 */         returnedResults.put(param.getName(), result);
/* 1160:     */       }
/* 1161:     */     }
/* 1162:     */     finally
/* 1163:     */     {
/* 1164:1219 */       JdbcUtils.closeResultSet(rs);
/* 1165:     */     }
/* 1166:1221 */     return returnedResults;
/* 1167:     */   }
/* 1168:     */   
/* 1169:     */   protected RowMapper<Map<String, Object>> getColumnMapRowMapper()
/* 1170:     */   {
/* 1171:1235 */     return new ColumnMapRowMapper();
/* 1172:     */   }
/* 1173:     */   
/* 1174:     */   protected <T> RowMapper<T> getSingleColumnRowMapper(Class<T> requiredType)
/* 1175:     */   {
/* 1176:1245 */     return new SingleColumnRowMapper(requiredType);
/* 1177:     */   }
/* 1178:     */   
/* 1179:     */   protected Map<String, Object> createResultsMap()
/* 1180:     */   {
/* 1181:1256 */     if (isResultsMapCaseInsensitive()) {
/* 1182:1257 */       return new LinkedCaseInsensitiveMap();
/* 1183:     */     }
/* 1184:1260 */     return new LinkedHashMap();
/* 1185:     */   }
/* 1186:     */   
/* 1187:     */   protected void applyStatementSettings(Statement stmt)
/* 1188:     */     throws SQLException
/* 1189:     */   {
/* 1190:1275 */     int fetchSize = getFetchSize();
/* 1191:1276 */     if (fetchSize > 0) {
/* 1192:1277 */       stmt.setFetchSize(fetchSize);
/* 1193:     */     }
/* 1194:1279 */     int maxRows = getMaxRows();
/* 1195:1280 */     if (maxRows > 0) {
/* 1196:1281 */       stmt.setMaxRows(maxRows);
/* 1197:     */     }
/* 1198:1283 */     DataSourceUtils.applyTimeout(stmt, getDataSource(), getQueryTimeout());
/* 1199:     */   }
/* 1200:     */   
/* 1201:     */   protected PreparedStatementSetter newArgPreparedStatementSetter(Object[] args)
/* 1202:     */   {
/* 1203:1293 */     return new ArgPreparedStatementSetter(args);
/* 1204:     */   }
/* 1205:     */   
/* 1206:     */   protected PreparedStatementSetter newArgTypePreparedStatementSetter(Object[] args, int[] argTypes)
/* 1207:     */   {
/* 1208:1304 */     return new ArgTypePreparedStatementSetter(args, argTypes);
/* 1209:     */   }
/* 1210:     */   
/* 1211:     */   protected void handleWarnings(Statement stmt)
/* 1212:     */     throws SQLException
/* 1213:     */   {
/* 1214:1315 */     if (isIgnoreWarnings())
/* 1215:     */     {
/* 1216:1316 */       if (this.logger.isDebugEnabled())
/* 1217:     */       {
/* 1218:1317 */         SQLWarning warningToLog = stmt.getWarnings();
/* 1219:1318 */         while (warningToLog != null)
/* 1220:     */         {
/* 1221:1319 */           this.logger.debug("SQLWarning ignored: SQL state '" + warningToLog.getSQLState() + "', error code '" + 
/* 1222:1320 */             warningToLog.getErrorCode() + "', message [" + warningToLog.getMessage() + "]");
/* 1223:1321 */           warningToLog = warningToLog.getNextWarning();
/* 1224:     */         }
/* 1225:     */       }
/* 1226:     */     }
/* 1227:     */     else {
/* 1228:1326 */       handleWarnings(stmt.getWarnings());
/* 1229:     */     }
/* 1230:     */   }
/* 1231:     */   
/* 1232:     */   protected void handleWarnings(SQLWarning warning)
/* 1233:     */     throws SQLWarningException
/* 1234:     */   {
/* 1235:1337 */     if (warning != null) {
/* 1236:1338 */       throw new SQLWarningException("Warning not ignored", warning);
/* 1237:     */     }
/* 1238:     */   }
/* 1239:     */   
/* 1240:     */   private static String getSql(Object sqlProvider)
/* 1241:     */   {
/* 1242:1349 */     if ((sqlProvider instanceof SqlProvider)) {
/* 1243:1350 */       return ((SqlProvider)sqlProvider).getSql();
/* 1244:     */     }
/* 1245:1353 */     return null;
/* 1246:     */   }
/* 1247:     */   
/* 1248:     */   private class CloseSuppressingInvocationHandler
/* 1249:     */     implements InvocationHandler
/* 1250:     */   {
/* 1251:     */     private final Connection target;
/* 1252:     */     
/* 1253:     */     public CloseSuppressingInvocationHandler(Connection target)
/* 1254:     */     {
/* 1255:1368 */       this.target = target;
/* 1256:     */     }
/* 1257:     */     
/* 1258:     */     public Object invoke(Object proxy, Method method, Object[] args)
/* 1259:     */       throws Throwable
/* 1260:     */     {
/* 1261:1374 */       if (method.getName().equals("equals"))
/* 1262:     */       {
/* 1263:1376 */         if (proxy == args[0]) {
/* 1264:1376 */           return Boolean.valueOf(true);
/* 1265:     */         }
/* 1266:1376 */         return Boolean.valueOf(false);
/* 1267:     */       }
/* 1268:1378 */       if (method.getName().equals("hashCode")) {
/* 1269:1380 */         return Integer.valueOf(System.identityHashCode(proxy));
/* 1270:     */       }
/* 1271:1382 */       if (method.getName().equals("unwrap"))
/* 1272:     */       {
/* 1273:1383 */         if (((Class)args[0]).isInstance(proxy)) {
/* 1274:1384 */           return proxy;
/* 1275:     */         }
/* 1276:     */       }
/* 1277:1387 */       else if (method.getName().equals("isWrapperFor"))
/* 1278:     */       {
/* 1279:1388 */         if (((Class)args[0]).isInstance(proxy)) {
/* 1280:1389 */           return Boolean.valueOf(true);
/* 1281:     */         }
/* 1282:     */       }
/* 1283:     */       else
/* 1284:     */       {
/* 1285:1392 */         if (method.getName().equals("close")) {
/* 1286:1394 */           return null;
/* 1287:     */         }
/* 1288:1396 */         if (method.getName().equals("isClosed")) {
/* 1289:1397 */           return Boolean.valueOf(false);
/* 1290:     */         }
/* 1291:1399 */         if (method.getName().equals("getTargetConnection")) {
/* 1292:1401 */           return this.target;
/* 1293:     */         }
/* 1294:     */       }
/* 1295:     */       try
/* 1296:     */       {
/* 1297:1406 */         Object retVal = method.invoke(this.target, args);
/* 1298:1410 */         if ((retVal instanceof Statement)) {
/* 1299:1411 */           JdbcTemplate.this.applyStatementSettings((Statement)retVal);
/* 1300:     */         }
/* 1301:1414 */         return retVal;
/* 1302:     */       }
/* 1303:     */       catch (InvocationTargetException ex)
/* 1304:     */       {
/* 1305:1417 */         throw ex.getTargetException();
/* 1306:     */       }
/* 1307:     */     }
/* 1308:     */   }
/* 1309:     */   
/* 1310:     */   private static class SimplePreparedStatementCreator
/* 1311:     */     implements PreparedStatementCreator, SqlProvider
/* 1312:     */   {
/* 1313:     */     private final String sql;
/* 1314:     */     
/* 1315:     */     public SimplePreparedStatementCreator(String sql)
/* 1316:     */     {
/* 1317:1431 */       Assert.notNull(sql, "SQL must not be null");
/* 1318:1432 */       this.sql = sql;
/* 1319:     */     }
/* 1320:     */     
/* 1321:     */     public PreparedStatement createPreparedStatement(Connection con)
/* 1322:     */       throws SQLException
/* 1323:     */     {
/* 1324:1436 */       return con.prepareStatement(this.sql);
/* 1325:     */     }
/* 1326:     */     
/* 1327:     */     public String getSql()
/* 1328:     */     {
/* 1329:1440 */       return this.sql;
/* 1330:     */     }
/* 1331:     */   }
/* 1332:     */   
/* 1333:     */   private static class SimpleCallableStatementCreator
/* 1334:     */     implements CallableStatementCreator, SqlProvider
/* 1335:     */   {
/* 1336:     */     private final String callString;
/* 1337:     */     
/* 1338:     */     public SimpleCallableStatementCreator(String callString)
/* 1339:     */     {
/* 1340:1453 */       Assert.notNull(callString, "Call string must not be null");
/* 1341:1454 */       this.callString = callString;
/* 1342:     */     }
/* 1343:     */     
/* 1344:     */     public CallableStatement createCallableStatement(Connection con)
/* 1345:     */       throws SQLException
/* 1346:     */     {
/* 1347:1458 */       return con.prepareCall(this.callString);
/* 1348:     */     }
/* 1349:     */     
/* 1350:     */     public String getSql()
/* 1351:     */     {
/* 1352:1462 */       return this.callString;
/* 1353:     */     }
/* 1354:     */   }
/* 1355:     */   
/* 1356:     */   private static class RowCallbackHandlerResultSetExtractor
/* 1357:     */     implements ResultSetExtractor<Object>
/* 1358:     */   {
/* 1359:     */     private final RowCallbackHandler rch;
/* 1360:     */     
/* 1361:     */     public RowCallbackHandlerResultSetExtractor(RowCallbackHandler rch)
/* 1362:     */     {
/* 1363:1477 */       this.rch = rch;
/* 1364:     */     }
/* 1365:     */     
/* 1366:     */     public Object extractData(ResultSet rs)
/* 1367:     */       throws SQLException
/* 1368:     */     {
/* 1369:1481 */       while (rs.next()) {
/* 1370:1482 */         this.rch.processRow(rs);
/* 1371:     */       }
/* 1372:1484 */       return null;
/* 1373:     */     }
/* 1374:     */   }
/* 1375:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.JdbcTemplate
 * JD-Core Version:    0.7.0.1
 */