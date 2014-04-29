/*   1:    */ package org.springframework.jdbc.core.metadata;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.HashSet;
/*   6:    */ import java.util.LinkedHashMap;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Locale;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Set;
/*  11:    */ import javax.sql.DataSource;
/*  12:    */ import org.apache.commons.logging.Log;
/*  13:    */ import org.apache.commons.logging.LogFactory;
/*  14:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*  15:    */ import org.springframework.jdbc.core.RowMapper;
/*  16:    */ import org.springframework.jdbc.core.SqlOutParameter;
/*  17:    */ import org.springframework.jdbc.core.SqlParameter;
/*  18:    */ import org.springframework.jdbc.core.SqlParameterValue;
/*  19:    */ import org.springframework.jdbc.core.SqlReturnResultSet;
/*  20:    */ import org.springframework.jdbc.core.namedparam.SqlParameterSource;
/*  21:    */ import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
/*  22:    */ import org.springframework.jdbc.support.JdbcUtils;
/*  23:    */ import org.springframework.util.StringUtils;
/*  24:    */ 
/*  25:    */ public class CallMetaDataContext
/*  26:    */ {
/*  27: 54 */   protected final Log logger = LogFactory.getLog(getClass());
/*  28:    */   private String procedureName;
/*  29:    */   private String catalogName;
/*  30:    */   private String schemaName;
/*  31: 66 */   private List<SqlParameter> callParameters = new ArrayList();
/*  32: 69 */   private String defaultFunctionReturnName = "return";
/*  33: 72 */   private String actualFunctionReturnName = null;
/*  34: 75 */   private Set<String> limitedInParameterNames = new HashSet();
/*  35: 78 */   private List<String> outParameterNames = new ArrayList();
/*  36: 81 */   private boolean accessCallParameterMetaData = true;
/*  37:    */   private boolean function;
/*  38:    */   private boolean returnValueRequired;
/*  39:    */   private CallMetaDataProvider metaDataProvider;
/*  40:    */   
/*  41:    */   public void setFunctionReturnName(String functionReturnName)
/*  42:    */   {
/*  43: 97 */     this.actualFunctionReturnName = functionReturnName;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public String getFunctionReturnName()
/*  47:    */   {
/*  48:104 */     return this.actualFunctionReturnName != null ? this.actualFunctionReturnName : this.defaultFunctionReturnName;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setLimitedInParameterNames(Set<String> limitedInParameterNames)
/*  52:    */   {
/*  53:111 */     this.limitedInParameterNames = limitedInParameterNames;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public Set<String> getLimitedInParameterNames()
/*  57:    */   {
/*  58:118 */     return this.limitedInParameterNames;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setOutParameterNames(List<String> outParameterNames)
/*  62:    */   {
/*  63:125 */     this.outParameterNames = outParameterNames;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public List<String> getOutParameterNames()
/*  67:    */   {
/*  68:132 */     return this.outParameterNames;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setProcedureName(String procedureName)
/*  72:    */   {
/*  73:139 */     this.procedureName = procedureName;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public String getProcedureName()
/*  77:    */   {
/*  78:146 */     return this.procedureName;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setCatalogName(String catalogName)
/*  82:    */   {
/*  83:153 */     this.catalogName = catalogName;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public String getCatalogName()
/*  87:    */   {
/*  88:160 */     return this.catalogName;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setSchemaName(String schemaName)
/*  92:    */   {
/*  93:167 */     this.schemaName = schemaName;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public String getSchemaName()
/*  97:    */   {
/*  98:174 */     return this.schemaName;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setFunction(boolean function)
/* 102:    */   {
/* 103:181 */     this.function = function;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public boolean isFunction()
/* 107:    */   {
/* 108:188 */     return this.function;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void setReturnValueRequired(boolean returnValueRequired)
/* 112:    */   {
/* 113:195 */     this.returnValueRequired = returnValueRequired;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public boolean isReturnValueRequired()
/* 117:    */   {
/* 118:202 */     return this.returnValueRequired;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void setAccessCallParameterMetaData(boolean accessCallParameterMetaData)
/* 122:    */   {
/* 123:209 */     this.accessCallParameterMetaData = accessCallParameterMetaData;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public boolean isAccessCallParameterMetaData()
/* 127:    */   {
/* 128:216 */     return this.accessCallParameterMetaData;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public SqlParameter createReturnResultSetParameter(String parameterName, RowMapper rowMapper)
/* 132:    */   {
/* 133:228 */     if (this.metaDataProvider.isReturnResultSetSupported()) {
/* 134:229 */       return new SqlReturnResultSet(parameterName, rowMapper);
/* 135:    */     }
/* 136:232 */     if (this.metaDataProvider.isRefCursorSupported()) {
/* 137:233 */       return new SqlOutParameter(parameterName, this.metaDataProvider.getRefCursorSqlType(), rowMapper);
/* 138:    */     }
/* 139:236 */     throw new InvalidDataAccessApiUsageException("Return of a ResultSet from a stored procedure is not supported.");
/* 140:    */   }
/* 141:    */   
/* 142:    */   public String getScalarOutParameterName()
/* 143:    */   {
/* 144:246 */     if (isFunction()) {
/* 145:247 */       return getFunctionReturnName();
/* 146:    */     }
/* 147:250 */     if (this.outParameterNames.size() > 1) {
/* 148:251 */       this.logger.warn("Accessing single output value when procedure has more than one output parameter");
/* 149:    */     }
/* 150:253 */     return this.outParameterNames.size() > 0 ? (String)this.outParameterNames.get(0) : null;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public List<SqlParameter> getCallParameters()
/* 154:    */   {
/* 155:261 */     return this.callParameters;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public void initializeMetaData(DataSource dataSource)
/* 159:    */   {
/* 160:269 */     this.metaDataProvider = CallMetaDataProviderFactory.createMetaDataProvider(dataSource, this);
/* 161:    */   }
/* 162:    */   
/* 163:    */   public void processParameters(List<SqlParameter> parameters)
/* 164:    */   {
/* 165:279 */     this.callParameters = reconcileParameters(parameters);
/* 166:    */   }
/* 167:    */   
/* 168:    */   protected List<SqlParameter> reconcileParameters(List<SqlParameter> parameters)
/* 169:    */   {
/* 170:286 */     List<SqlParameter> declaredReturnParameters = new ArrayList();
/* 171:287 */     Map<String, SqlParameter> declaredParameters = new LinkedHashMap();
/* 172:288 */     boolean returnDeclared = false;
/* 173:289 */     List<String> outParameterNames = new ArrayList();
/* 174:290 */     List<String> metaDataParameterNames = new ArrayList();
/* 175:293 */     for (CallParameterMetaData meta : this.metaDataProvider.getCallParameterMetaData()) {
/* 176:294 */       if (meta.getParameterType() != 5) {
/* 177:295 */         metaDataParameterNames.add(meta.getParameterName().toLowerCase());
/* 178:    */       }
/* 179:    */     }
/* 180:300 */     for (SqlParameter parameter : parameters) {
/* 181:301 */       if (parameter.isResultsParameter())
/* 182:    */       {
/* 183:302 */         declaredReturnParameters.add(parameter);
/* 184:    */       }
/* 185:    */       else
/* 186:    */       {
/* 187:305 */         String parameterNameToMatch = this.metaDataProvider.parameterNameToUse(parameter.getName()).toLowerCase();
/* 188:306 */         declaredParameters.put(parameterNameToMatch, parameter);
/* 189:307 */         if ((parameter instanceof SqlOutParameter))
/* 190:    */         {
/* 191:308 */           outParameterNames.add(parameter.getName());
/* 192:309 */           if ((isFunction()) && (!metaDataParameterNames.contains(parameterNameToMatch)) && 
/* 193:310 */             (!returnDeclared))
/* 194:    */           {
/* 195:311 */             if (this.logger.isDebugEnabled()) {
/* 196:312 */               this.logger.debug("Using declared out parameter '" + parameter.getName() + "' for function return value");
/* 197:    */             }
/* 198:314 */             setFunctionReturnName(parameter.getName());
/* 199:315 */             returnDeclared = true;
/* 200:    */           }
/* 201:    */         }
/* 202:    */       }
/* 203:    */     }
/* 204:321 */     setOutParameterNames(outParameterNames);
/* 205:    */     
/* 206:323 */     List<SqlParameter> workParameters = new ArrayList();
/* 207:324 */     workParameters.addAll(declaredReturnParameters);
/* 208:326 */     if (!this.metaDataProvider.isProcedureColumnMetaDataUsed())
/* 209:    */     {
/* 210:327 */       workParameters.addAll(declaredParameters.values());
/* 211:328 */       return workParameters;
/* 212:    */     }
/* 213:331 */     Object limitedInParamNamesMap = new HashMap(this.limitedInParameterNames.size());
/* 214:332 */     for (String limitedParameterName : this.limitedInParameterNames) {
/* 215:333 */       ((Map)limitedInParamNamesMap).put(
/* 216:334 */         this.metaDataProvider.parameterNameToUse(limitedParameterName).toLowerCase(), limitedParameterName);
/* 217:    */     }
/* 218:337 */     for (CallParameterMetaData meta : this.metaDataProvider.getCallParameterMetaData())
/* 219:    */     {
/* 220:338 */       String parNameToCheck = null;
/* 221:339 */       if (meta.getParameterName() != null) {
/* 222:340 */         parNameToCheck = this.metaDataProvider.parameterNameToUse(meta.getParameterName()).toLowerCase();
/* 223:    */       }
/* 224:342 */       String parNameToUse = this.metaDataProvider.parameterNameToUse(meta.getParameterName());
/* 225:343 */       if ((declaredParameters.containsKey(parNameToCheck)) || (
/* 226:344 */         (meta.getParameterType() == 5) && (returnDeclared)))
/* 227:    */       {
/* 228:    */         SqlParameter parameter;
/* 229:346 */         if (meta.getParameterType() == 5)
/* 230:    */         {
/* 231:347 */           SqlParameter parameter = (SqlParameter)declaredParameters.get(getFunctionReturnName());
/* 232:348 */           if ((parameter == null) && (getOutParameterNames().size() > 0)) {
/* 233:349 */             parameter = (SqlParameter)declaredParameters.get(((String)getOutParameterNames().get(0)).toLowerCase());
/* 234:    */           }
/* 235:351 */           if (parameter == null) {
/* 236:352 */             throw new InvalidDataAccessApiUsageException(
/* 237:353 */               "Unable to locate declared parameter for function return value -  add a SqlOutParameter with name \"" + 
/* 238:354 */               getFunctionReturnName() + "\"");
/* 239:    */           }
/* 240:357 */           setFunctionReturnName(parameter.getName());
/* 241:    */         }
/* 242:    */         else
/* 243:    */         {
/* 244:361 */           parameter = (SqlParameter)declaredParameters.get(parNameToCheck);
/* 245:    */         }
/* 246:363 */         if (parameter != null)
/* 247:    */         {
/* 248:364 */           workParameters.add(parameter);
/* 249:365 */           if (this.logger.isDebugEnabled()) {
/* 250:366 */             this.logger.debug("Using declared parameter for: " + (
/* 251:367 */               parNameToUse == null ? getFunctionReturnName() : parNameToUse));
/* 252:    */           }
/* 253:    */         }
/* 254:    */       }
/* 255:372 */       else if (meta.getParameterType() == 5)
/* 256:    */       {
/* 257:373 */         if ((!isFunction()) && (!isReturnValueRequired()) && 
/* 258:374 */           (this.metaDataProvider.byPassReturnParameter(meta.getParameterName())))
/* 259:    */         {
/* 260:375 */           if (this.logger.isDebugEnabled()) {
/* 261:376 */             this.logger.debug("Bypassing metadata return parameter for: " + meta.getParameterName());
/* 262:    */           }
/* 263:    */         }
/* 264:    */         else
/* 265:    */         {
/* 266:380 */           String returnNameToUse = StringUtils.hasLength(meta.getParameterName()) ? 
/* 267:381 */             parNameToUse : getFunctionReturnName();
/* 268:382 */           workParameters.add(new SqlOutParameter(returnNameToUse, meta.getSqlType()));
/* 269:383 */           if (isFunction())
/* 270:    */           {
/* 271:384 */             setFunctionReturnName(returnNameToUse);
/* 272:385 */             outParameterNames.add(returnNameToUse);
/* 273:    */           }
/* 274:387 */           if (this.logger.isDebugEnabled()) {
/* 275:388 */             this.logger.debug("Added metadata return parameter for: " + returnNameToUse);
/* 276:    */           }
/* 277:    */         }
/* 278:    */       }
/* 279:393 */       else if (meta.getParameterType() == 4)
/* 280:    */       {
/* 281:394 */         workParameters.add(this.metaDataProvider.createDefaultOutParameter(parNameToUse, meta));
/* 282:395 */         outParameterNames.add(parNameToUse);
/* 283:396 */         if (this.logger.isDebugEnabled()) {
/* 284:397 */           this.logger.debug("Added metadata out parameter for: " + parNameToUse);
/* 285:    */         }
/* 286:    */       }
/* 287:400 */       else if (meta.getParameterType() == 2)
/* 288:    */       {
/* 289:401 */         workParameters.add(this.metaDataProvider.createDefaultInOutParameter(parNameToUse, meta));
/* 290:402 */         outParameterNames.add(parNameToUse);
/* 291:403 */         if (this.logger.isDebugEnabled()) {
/* 292:404 */           this.logger.debug("Added metadata in out parameter for: " + parNameToUse);
/* 293:    */         }
/* 294:    */       }
/* 295:408 */       else if ((this.limitedInParameterNames.isEmpty()) || 
/* 296:409 */         (((Map)limitedInParamNamesMap).containsKey(parNameToUse.toLowerCase())))
/* 297:    */       {
/* 298:410 */         workParameters.add(this.metaDataProvider.createDefaultInParameter(parNameToUse, meta));
/* 299:411 */         if (this.logger.isDebugEnabled()) {
/* 300:412 */           this.logger.debug("Added metadata in parameter for: " + parNameToUse);
/* 301:    */         }
/* 302:    */       }
/* 303:416 */       else if (this.logger.isDebugEnabled())
/* 304:    */       {
/* 305:417 */         this.logger.debug("Limited set of parameters " + ((Map)limitedInParamNamesMap).keySet() + 
/* 306:418 */           " skipped parameter for: " + parNameToUse);
/* 307:    */       }
/* 308:    */     }
/* 309:426 */     return workParameters;
/* 310:    */   }
/* 311:    */   
/* 312:    */   public Map<String, Object> matchInParameterValuesWithCallParameters(SqlParameterSource parameterSource)
/* 313:    */   {
/* 314:438 */     Map caseInsensitiveParameterNames = 
/* 315:439 */       SqlParameterSourceUtils.extractCaseInsensitiveParameterNames(parameterSource);
/* 316:    */     
/* 317:441 */     Map<String, String> callParameterNames = new HashMap(this.callParameters.size());
/* 318:442 */     Map<String, Object> matchedParameters = new HashMap(this.callParameters.size());
/* 319:443 */     for (SqlParameter parameter : this.callParameters) {
/* 320:444 */       if (parameter.isInputValueProvided())
/* 321:    */       {
/* 322:445 */         String parameterName = parameter.getName();
/* 323:446 */         String parameterNameToMatch = this.metaDataProvider.parameterNameToUse(parameterName);
/* 324:447 */         if (parameterNameToMatch != null) {
/* 325:448 */           callParameterNames.put(parameterNameToMatch.toLowerCase(), parameterName);
/* 326:    */         }
/* 327:450 */         if (parameterName != null) {
/* 328:451 */           if (parameterSource.hasValue(parameterName))
/* 329:    */           {
/* 330:452 */             matchedParameters.put(parameterName, SqlParameterSourceUtils.getTypedValue(parameterSource, parameterName));
/* 331:    */           }
/* 332:    */           else
/* 333:    */           {
/* 334:455 */             String lowerCaseName = parameterName.toLowerCase();
/* 335:456 */             if (parameterSource.hasValue(lowerCaseName))
/* 336:    */             {
/* 337:457 */               matchedParameters.put(parameterName, SqlParameterSourceUtils.getTypedValue(parameterSource, lowerCaseName));
/* 338:    */             }
/* 339:    */             else
/* 340:    */             {
/* 341:460 */               String englishLowerCaseName = parameterName.toLowerCase(Locale.ENGLISH);
/* 342:461 */               if (parameterSource.hasValue(englishLowerCaseName))
/* 343:    */               {
/* 344:462 */                 matchedParameters.put(parameterName, SqlParameterSourceUtils.getTypedValue(parameterSource, englishLowerCaseName));
/* 345:    */               }
/* 346:    */               else
/* 347:    */               {
/* 348:465 */                 String propertyName = JdbcUtils.convertUnderscoreNameToPropertyName(parameterName);
/* 349:466 */                 if (parameterSource.hasValue(propertyName))
/* 350:    */                 {
/* 351:467 */                   matchedParameters.put(parameterName, SqlParameterSourceUtils.getTypedValue(parameterSource, propertyName));
/* 352:    */                 }
/* 353:470 */                 else if (caseInsensitiveParameterNames.containsKey(lowerCaseName))
/* 354:    */                 {
/* 355:471 */                   String sourceName = (String)caseInsensitiveParameterNames.get(lowerCaseName);
/* 356:472 */                   matchedParameters.put(parameterName, SqlParameterSourceUtils.getTypedValue(parameterSource, sourceName));
/* 357:    */                 }
/* 358:    */                 else
/* 359:    */                 {
/* 360:475 */                   this.logger.warn("Unable to locate the corresponding parameter value for '" + parameterName + 
/* 361:476 */                     "' within the parameter values provided: " + caseInsensitiveParameterNames.values());
/* 362:    */                 }
/* 363:    */               }
/* 364:    */             }
/* 365:    */           }
/* 366:    */         }
/* 367:    */       }
/* 368:    */     }
/* 369:486 */     if (this.logger.isDebugEnabled())
/* 370:    */     {
/* 371:487 */       this.logger.debug("Matching " + caseInsensitiveParameterNames.values() + " with " + callParameterNames.values());
/* 372:488 */       this.logger.debug("Found match for " + matchedParameters.keySet());
/* 373:    */     }
/* 374:490 */     return matchedParameters;
/* 375:    */   }
/* 376:    */   
/* 377:    */   public Map<String, ?> matchInParameterValuesWithCallParameters(Map<String, ?> inParameters)
/* 378:    */   {
/* 379:499 */     if (!this.metaDataProvider.isProcedureColumnMetaDataUsed()) {
/* 380:500 */       return inParameters;
/* 381:    */     }
/* 382:502 */     Map<String, String> callParameterNames = new HashMap(this.callParameters.size());
/* 383:    */     String parameterName;
/* 384:503 */     for (SqlParameter parameter : this.callParameters) {
/* 385:504 */       if (parameter.isInputValueProvided())
/* 386:    */       {
/* 387:505 */         parameterName = parameter.getName();
/* 388:506 */         String parameterNameToMatch = this.metaDataProvider.parameterNameToUse(parameterName);
/* 389:507 */         if (parameterNameToMatch != null) {
/* 390:508 */           callParameterNames.put(parameterNameToMatch.toLowerCase(), parameterName);
/* 391:    */         }
/* 392:    */       }
/* 393:    */     }
/* 394:512 */     Map<String, Object> matchedParameters = new HashMap(inParameters.size());
/* 395:513 */     for (String parameterName : inParameters.keySet())
/* 396:    */     {
/* 397:514 */       String parameterNameToMatch = this.metaDataProvider.parameterNameToUse(parameterName);
/* 398:515 */       String callParameterName = (String)callParameterNames.get(parameterNameToMatch.toLowerCase());
/* 399:516 */       if (callParameterName == null)
/* 400:    */       {
/* 401:517 */         if (this.logger.isDebugEnabled())
/* 402:    */         {
/* 403:518 */           Object value = inParameters.get(parameterName);
/* 404:519 */           if ((value instanceof SqlParameterValue)) {
/* 405:520 */             value = ((SqlParameterValue)value).getValue();
/* 406:    */           }
/* 407:522 */           if (value != null) {
/* 408:523 */             this.logger.debug("Unable to locate the corresponding IN or IN-OUT parameter for \"" + parameterName + 
/* 409:524 */               "\" in the parameters used: " + callParameterNames.keySet());
/* 410:    */           }
/* 411:    */         }
/* 412:    */       }
/* 413:    */       else {
/* 414:529 */         matchedParameters.put(callParameterName, inParameters.get(parameterName));
/* 415:    */       }
/* 416:    */     }
/* 417:532 */     if (matchedParameters.size() < callParameterNames.size()) {
/* 418:533 */       for (String parameterName : callParameterNames.keySet())
/* 419:    */       {
/* 420:534 */         String parameterNameToMatch = this.metaDataProvider.parameterNameToUse(parameterName);
/* 421:535 */         String callParameterName = (String)callParameterNames.get(parameterNameToMatch.toLowerCase());
/* 422:536 */         if (!matchedParameters.containsKey(callParameterName)) {
/* 423:537 */           this.logger.warn("Unable to locate the corresponding parameter value for '" + parameterName + 
/* 424:538 */             "' within the parameter values provided: " + inParameters.keySet());
/* 425:    */         }
/* 426:    */       }
/* 427:    */     }
/* 428:542 */     if (this.logger.isDebugEnabled())
/* 429:    */     {
/* 430:543 */       this.logger.debug("Matching " + inParameters.keySet() + " with " + callParameterNames.values());
/* 431:544 */       this.logger.debug("Found match for " + matchedParameters.keySet());
/* 432:    */     }
/* 433:546 */     return matchedParameters;
/* 434:    */   }
/* 435:    */   
/* 436:    */   public Map<String, ?> matchInParameterValuesWithCallParameters(Object[] parameterValues)
/* 437:    */   {
/* 438:550 */     Map<String, Object> matchedParameters = new HashMap(parameterValues.length);
/* 439:551 */     int i = 0;
/* 440:552 */     for (SqlParameter parameter : this.callParameters) {
/* 441:553 */       if (parameter.isInputValueProvided())
/* 442:    */       {
/* 443:554 */         String parameterName = parameter.getName();
/* 444:555 */         matchedParameters.put(parameterName, parameterValues[(i++)]);
/* 445:    */       }
/* 446:    */     }
/* 447:558 */     return matchedParameters;
/* 448:    */   }
/* 449:    */   
/* 450:    */   public String createCallString()
/* 451:    */   {
/* 452:567 */     int parameterCount = 0;
/* 453:568 */     String catalogNameToUse = null;
/* 454:569 */     String schemaNameToUse = null;
/* 455:573 */     if ((this.metaDataProvider.isSupportsSchemasInProcedureCalls()) && 
/* 456:574 */       (!this.metaDataProvider.isSupportsCatalogsInProcedureCalls()))
/* 457:    */     {
/* 458:575 */       schemaNameToUse = this.metaDataProvider.catalogNameToUse(getCatalogName());
/* 459:576 */       catalogNameToUse = this.metaDataProvider.schemaNameToUse(getSchemaName());
/* 460:    */     }
/* 461:    */     else
/* 462:    */     {
/* 463:579 */       catalogNameToUse = this.metaDataProvider.catalogNameToUse(getCatalogName());
/* 464:580 */       schemaNameToUse = this.metaDataProvider.schemaNameToUse(getSchemaName());
/* 465:    */     }
/* 466:582 */     String procedureNameToUse = this.metaDataProvider.procedureNameToUse(getProcedureName());
/* 467:583 */     if ((isFunction()) || (isReturnValueRequired()))
/* 468:    */     {
/* 469:584 */       String callString = "{? = call " + (
/* 470:585 */         StringUtils.hasLength(catalogNameToUse) ? catalogNameToUse + "." : "") + (
/* 471:586 */         StringUtils.hasLength(schemaNameToUse) ? schemaNameToUse + "." : "") + 
/* 472:587 */         procedureNameToUse + "(";
/* 473:588 */       parameterCount = -1;
/* 474:    */     }
/* 475:    */     else
/* 476:    */     {
/* 477:591 */       callString = 
/* 478:    */       
/* 479:    */ 
/* 480:594 */         "{call " + (StringUtils.hasLength(catalogNameToUse) ? catalogNameToUse + "." : "") + (StringUtils.hasLength(schemaNameToUse) ? schemaNameToUse + "." : "") + procedureNameToUse + "(";
/* 481:    */     }
/* 482:596 */     for (SqlParameter parameter : this.callParameters) {
/* 483:597 */       if (!parameter.isResultsParameter())
/* 484:    */       {
/* 485:598 */         if (parameterCount > 0) {
/* 486:599 */           callString = callString + ", ";
/* 487:    */         }
/* 488:601 */         if (parameterCount >= 0) {
/* 489:602 */           callString = callString + "?";
/* 490:    */         }
/* 491:604 */         parameterCount++;
/* 492:    */       }
/* 493:    */     }
/* 494:607 */     String callString = callString + ")}";
/* 495:    */     
/* 496:609 */     return callString;
/* 497:    */   }
/* 498:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.CallMetaDataContext
 * JD-Core Version:    0.7.0.1
 */