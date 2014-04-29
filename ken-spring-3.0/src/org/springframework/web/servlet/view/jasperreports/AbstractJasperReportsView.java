/*   1:    */ package org.springframework.web.servlet.view.jasperreports;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.lang.reflect.Field;
/*   6:    */ import java.sql.Connection;
/*   7:    */ import java.util.Collection;
/*   8:    */ import java.util.Enumeration;
/*   9:    */ import java.util.HashMap;
/*  10:    */ import java.util.Map;
/*  11:    */ import java.util.Map.Entry;
/*  12:    */ import java.util.Properties;
/*  13:    */ import javax.servlet.http.HttpServletRequest;
/*  14:    */ import javax.servlet.http.HttpServletResponse;
/*  15:    */ import javax.sql.DataSource;
/*  16:    */ import net.sf.jasperreports.engine.JRDataSource;
/*  17:    */ import net.sf.jasperreports.engine.JRDataSourceProvider;
/*  18:    */ import net.sf.jasperreports.engine.JRException;
/*  19:    */ import net.sf.jasperreports.engine.JRExporterParameter;
/*  20:    */ import net.sf.jasperreports.engine.JasperCompileManager;
/*  21:    */ import net.sf.jasperreports.engine.JasperFillManager;
/*  22:    */ import net.sf.jasperreports.engine.JasperPrint;
/*  23:    */ import net.sf.jasperreports.engine.JasperReport;
/*  24:    */ import net.sf.jasperreports.engine.design.JasperDesign;
/*  25:    */ import net.sf.jasperreports.engine.util.JRLoader;
/*  26:    */ import net.sf.jasperreports.engine.xml.JRXmlLoader;
/*  27:    */ import org.apache.commons.logging.Log;
/*  28:    */ import org.springframework.context.ApplicationContext;
/*  29:    */ import org.springframework.context.ApplicationContextException;
/*  30:    */ import org.springframework.context.support.MessageSourceResourceBundle;
/*  31:    */ import org.springframework.core.io.Resource;
/*  32:    */ import org.springframework.ui.jasperreports.JasperReportsUtils;
/*  33:    */ import org.springframework.util.ClassUtils;
/*  34:    */ import org.springframework.util.CollectionUtils;
/*  35:    */ import org.springframework.web.servlet.support.RequestContext;
/*  36:    */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*  37:    */ 
/*  38:    */ public abstract class AbstractJasperReportsView
/*  39:    */   extends AbstractUrlBasedView
/*  40:    */ {
/*  41:    */   protected static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
/*  42:    */   protected static final String CONTENT_DISPOSITION_INLINE = "inline";
/*  43:    */   private String reportDataKey;
/*  44:    */   private Properties subReportUrls;
/*  45:    */   private String[] subReportDataKeys;
/*  46:    */   private Properties headers;
/*  47:153 */   private Map<?, ?> exporterParameters = new HashMap();
/*  48:    */   private Map<JRExporterParameter, Object> convertedExporterParameters;
/*  49:    */   private DataSource jdbcDataSource;
/*  50:    */   private JasperReport report;
/*  51:    */   private Map<String, JasperReport> subReports;
/*  52:    */   
/*  53:    */   public void setReportDataKey(String reportDataKey)
/*  54:    */   {
/*  55:193 */     this.reportDataKey = reportDataKey;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setSubReportUrls(Properties subReports)
/*  59:    */   {
/*  60:206 */     this.subReportUrls = subReports;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setSubReportDataKeys(String[] subReportDataKeys)
/*  64:    */   {
/*  65:230 */     this.subReportDataKeys = subReportDataKeys;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setHeaders(Properties headers)
/*  69:    */   {
/*  70:238 */     this.headers = headers;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setExporterParameters(Map<?, ?> parameters)
/*  74:    */   {
/*  75:249 */     this.exporterParameters = parameters;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Map<?, ?> getExporterParameters()
/*  79:    */   {
/*  80:256 */     return this.exporterParameters;
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected void setConvertedExporterParameters(Map<JRExporterParameter, Object> convertedExporterParameters)
/*  84:    */   {
/*  85:263 */     this.convertedExporterParameters = convertedExporterParameters;
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected Map<JRExporterParameter, Object> getConvertedExporterParameters()
/*  89:    */   {
/*  90:270 */     return this.convertedExporterParameters;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setJdbcDataSource(DataSource jdbcDataSource)
/*  94:    */   {
/*  95:278 */     this.jdbcDataSource = jdbcDataSource;
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected DataSource getJdbcDataSource()
/*  99:    */   {
/* 100:285 */     return this.jdbcDataSource;
/* 101:    */   }
/* 102:    */   
/* 103:    */   protected boolean isUrlRequired()
/* 104:    */   {
/* 105:295 */     return false;
/* 106:    */   }
/* 107:    */   
/* 108:    */   protected final void initApplicationContext()
/* 109:    */     throws ApplicationContextException
/* 110:    */   {
/* 111:306 */     this.report = loadReport();
/* 112:309 */     if (this.subReportUrls != null)
/* 113:    */     {
/* 114:310 */       if ((this.subReportDataKeys != null) && (this.subReportDataKeys.length > 0) && (this.reportDataKey == null)) {
/* 115:311 */         throw new ApplicationContextException(
/* 116:312 */           "'reportDataKey' for main report is required when specifying a value for 'subReportDataKeys'");
/* 117:    */       }
/* 118:314 */       this.subReports = new HashMap(this.subReportUrls.size());
/* 119:315 */       for (Enumeration urls = this.subReportUrls.propertyNames(); urls.hasMoreElements();)
/* 120:    */       {
/* 121:316 */         String key = (String)urls.nextElement();
/* 122:317 */         String path = this.subReportUrls.getProperty(key);
/* 123:318 */         Resource resource = getApplicationContext().getResource(path);
/* 124:319 */         this.subReports.put(key, loadReport(resource));
/* 125:    */       }
/* 126:    */     }
/* 127:324 */     convertExporterParameters();
/* 128:326 */     if (this.headers == null) {
/* 129:327 */       this.headers = new Properties();
/* 130:    */     }
/* 131:329 */     if (!this.headers.containsKey("Content-Disposition")) {
/* 132:330 */       this.headers.setProperty("Content-Disposition", "inline");
/* 133:    */     }
/* 134:333 */     onInit();
/* 135:    */   }
/* 136:    */   
/* 137:    */   protected void onInit() {}
/* 138:    */   
/* 139:    */   protected final void convertExporterParameters()
/* 140:    */   {
/* 141:353 */     if (!CollectionUtils.isEmpty(this.exporterParameters))
/* 142:    */     {
/* 143:354 */       this.convertedExporterParameters = new HashMap(this.exporterParameters.size());
/* 144:355 */       for (Map.Entry<?, ?> entry : this.exporterParameters.entrySet())
/* 145:    */       {
/* 146:356 */         JRExporterParameter exporterParameter = getExporterParameter(entry.getKey());
/* 147:357 */         this.convertedExporterParameters.put(
/* 148:358 */           exporterParameter, convertParameterValue(exporterParameter, entry.getValue()));
/* 149:    */       }
/* 150:    */     }
/* 151:    */   }
/* 152:    */   
/* 153:    */   protected Object convertParameterValue(JRExporterParameter parameter, Object value)
/* 154:    */   {
/* 155:375 */     if ((value instanceof String))
/* 156:    */     {
/* 157:376 */       String str = (String)value;
/* 158:377 */       if ("true".equals(str)) {
/* 159:378 */         return Boolean.TRUE;
/* 160:    */       }
/* 161:380 */       if ("false".equals(str)) {
/* 162:381 */         return Boolean.FALSE;
/* 163:    */       }
/* 164:383 */       if ((str.length() > 0) && (Character.isDigit(str.charAt(0)))) {
/* 165:    */         try
/* 166:    */         {
/* 167:386 */           return new Integer(str);
/* 168:    */         }
/* 169:    */         catch (NumberFormatException localNumberFormatException)
/* 170:    */         {
/* 171:390 */           return str;
/* 172:    */         }
/* 173:    */       }
/* 174:    */     }
/* 175:394 */     return value;
/* 176:    */   }
/* 177:    */   
/* 178:    */   protected JRExporterParameter getExporterParameter(Object parameter)
/* 179:    */   {
/* 180:405 */     if ((parameter instanceof JRExporterParameter)) {
/* 181:406 */       return (JRExporterParameter)parameter;
/* 182:    */     }
/* 183:408 */     if ((parameter instanceof String)) {
/* 184:409 */       return convertToExporterParameter((String)parameter);
/* 185:    */     }
/* 186:411 */     throw new IllegalArgumentException(
/* 187:412 */       "Parameter [" + parameter + "] is invalid type. Should be either String or JRExporterParameter.");
/* 188:    */   }
/* 189:    */   
/* 190:    */   protected JRExporterParameter convertToExporterParameter(String fqFieldName)
/* 191:    */   {
/* 192:424 */     int index = fqFieldName.lastIndexOf('.');
/* 193:425 */     if ((index == -1) || (index == fqFieldName.length())) {
/* 194:426 */       throw new IllegalArgumentException(
/* 195:427 */         "Parameter name [" + fqFieldName + "] is not a valid static field. " + 
/* 196:428 */         "The parameter name must map to a static field such as " + 
/* 197:429 */         "[net.sf.jasperreports.engine.export.JRHtmlExporterParameter.IMAGES_URI]");
/* 198:    */     }
/* 199:431 */     String className = fqFieldName.substring(0, index);
/* 200:432 */     String fieldName = fqFieldName.substring(index + 1);
/* 201:    */     try
/* 202:    */     {
/* 203:435 */       Class cls = ClassUtils.forName(className, getApplicationContext().getClassLoader());
/* 204:436 */       Field field = cls.getField(fieldName);
/* 205:438 */       if (JRExporterParameter.class.isAssignableFrom(field.getType())) {
/* 206:    */         try
/* 207:    */         {
/* 208:440 */           return (JRExporterParameter)field.get(null);
/* 209:    */         }
/* 210:    */         catch (IllegalAccessException localIllegalAccessException)
/* 211:    */         {
/* 212:443 */           throw new IllegalArgumentException(
/* 213:444 */             "Unable to access field [" + fieldName + "] of class [" + className + "]. " + 
/* 214:445 */             "Check that it is static and accessible.");
/* 215:    */         }
/* 216:    */       }
/* 217:449 */       throw new IllegalArgumentException("Field [" + fieldName + "] on class [" + className + 
/* 218:450 */         "] is not assignable from JRExporterParameter - check the type of this field.");
/* 219:    */     }
/* 220:    */     catch (ClassNotFoundException localClassNotFoundException)
/* 221:    */     {
/* 222:454 */       throw new IllegalArgumentException(
/* 223:455 */         "Class [" + className + "] in key [" + fqFieldName + "] could not be found.");
/* 224:    */     }
/* 225:    */     catch (NoSuchFieldException localNoSuchFieldException)
/* 226:    */     {
/* 227:458 */       throw new IllegalArgumentException("Field [" + fieldName + "] in key [" + fqFieldName + 
/* 228:459 */         "] could not be found on class [" + className + "].");
/* 229:    */     }
/* 230:    */   }
/* 231:    */   
/* 232:    */   protected JasperReport loadReport()
/* 233:    */   {
/* 234:471 */     String url = getUrl();
/* 235:472 */     if (url == null) {
/* 236:473 */       return null;
/* 237:    */     }
/* 238:475 */     Resource mainReport = getApplicationContext().getResource(url);
/* 239:476 */     return loadReport(mainReport);
/* 240:    */   }
/* 241:    */   
/* 242:    */   protected final JasperReport loadReport(Resource resource)
/* 243:    */   {
/* 244:    */     try
/* 245:    */     {
/* 246:488 */       String fileName = resource.getFilename();
/* 247:489 */       if (fileName.endsWith(".jasper"))
/* 248:    */       {
/* 249:491 */         if (this.logger.isInfoEnabled()) {
/* 250:492 */           this.logger.info("Loading pre-compiled Jasper Report from " + resource);
/* 251:    */         }
/* 252:494 */         InputStream is = resource.getInputStream();
/* 253:    */         try
/* 254:    */         {
/* 255:496 */           return (JasperReport)JRLoader.loadObject(is);
/* 256:    */         }
/* 257:    */         finally
/* 258:    */         {
/* 259:499 */           is.close();
/* 260:    */         }
/* 261:    */       }
/* 262:502 */       if (fileName.endsWith(".jrxml"))
/* 263:    */       {
/* 264:504 */         if (this.logger.isInfoEnabled()) {
/* 265:505 */           this.logger.info("Compiling Jasper Report loaded from " + resource);
/* 266:    */         }
/* 267:507 */         InputStream is = resource.getInputStream();
/* 268:    */         try
/* 269:    */         {
/* 270:509 */           JasperDesign design = JRXmlLoader.load(is);
/* 271:510 */           return JasperCompileManager.compileReport(design);
/* 272:    */         }
/* 273:    */         finally
/* 274:    */         {
/* 275:513 */           is.close();
/* 276:    */         }
/* 277:    */       }
/* 278:517 */       throw new IllegalArgumentException(
/* 279:518 */         "Report filename [" + fileName + "] must end in either .jasper or .jrxml");
/* 280:    */     }
/* 281:    */     catch (IOException ex)
/* 282:    */     {
/* 283:522 */       throw new ApplicationContextException(
/* 284:523 */         "Could not load JasperReports report from " + resource, ex);
/* 285:    */     }
/* 286:    */     catch (JRException ex)
/* 287:    */     {
/* 288:526 */       throw new ApplicationContextException(
/* 289:527 */         "Could not parse JasperReports report from " + resource, ex);
/* 290:    */     }
/* 291:    */   }
/* 292:    */   
/* 293:    */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/* 294:    */     throws Exception
/* 295:    */   {
/* 296:543 */     if (this.subReports != null)
/* 297:    */     {
/* 298:545 */       model.putAll(this.subReports);
/* 299:548 */       if (this.subReportDataKeys != null) {
/* 300:549 */         for (String key : this.subReportDataKeys) {
/* 301:550 */           model.put(key, convertReportData(model.get(key)));
/* 302:    */         }
/* 303:    */       }
/* 304:    */     }
/* 305:556 */     exposeLocalizationContext(model, request);
/* 306:    */     
/* 307:    */ 
/* 308:559 */     JasperPrint filledReport = fillReport(model);
/* 309:560 */     postProcessReport(filledReport, model);
/* 310:    */     
/* 311:    */ 
/* 312:563 */     populateHeaders(response);
/* 313:564 */     renderReport(filledReport, model, response);
/* 314:    */   }
/* 315:    */   
/* 316:    */   protected void exposeLocalizationContext(Map<String, Object> model, HttpServletRequest request)
/* 317:    */   {
/* 318:582 */     RequestContext rc = new RequestContext(request, getServletContext());
/* 319:583 */     if (!model.containsKey("REPORT_LOCALE")) {
/* 320:584 */       model.put("REPORT_LOCALE", rc.getLocale());
/* 321:    */     }
/* 322:586 */     JasperReport report = getReport();
/* 323:587 */     if (((report == null) || (report.getResourceBundle() == null)) && 
/* 324:588 */       (!model.containsKey("REPORT_RESOURCE_BUNDLE"))) {
/* 325:589 */       model.put("REPORT_RESOURCE_BUNDLE", 
/* 326:590 */         new MessageSourceResourceBundle(rc.getMessageSource(), rc.getLocale()));
/* 327:    */     }
/* 328:    */   }
/* 329:    */   
/* 330:    */   protected JasperPrint fillReport(Map<String, Object> model)
/* 331:    */     throws Exception
/* 332:    */   {
/* 333:620 */     JasperReport report = getReport();
/* 334:621 */     if (report == null) {
/* 335:622 */       throw new IllegalStateException("No main report defined for 'fillReport' - specify a 'url' on this view or override 'getReport()' or 'fillReport(Map)'");
/* 336:    */     }
/* 337:626 */     JRDataSource jrDataSource = null;
/* 338:627 */     DataSource jdbcDataSourceToUse = null;
/* 339:630 */     if (this.reportDataKey != null)
/* 340:    */     {
/* 341:631 */       Object reportDataValue = model.get(this.reportDataKey);
/* 342:632 */       if ((reportDataValue instanceof DataSource)) {
/* 343:633 */         jdbcDataSourceToUse = (DataSource)reportDataValue;
/* 344:    */       } else {
/* 345:636 */         jrDataSource = convertReportData(reportDataValue);
/* 346:    */       }
/* 347:    */     }
/* 348:    */     else
/* 349:    */     {
/* 350:640 */       Collection values = model.values();
/* 351:641 */       jrDataSource = (JRDataSource)CollectionUtils.findValueOfType(values, JRDataSource.class);
/* 352:642 */       if (jrDataSource == null)
/* 353:    */       {
/* 354:643 */         JRDataSourceProvider provider = (JRDataSourceProvider)CollectionUtils.findValueOfType(values, JRDataSourceProvider.class);
/* 355:644 */         if (provider != null)
/* 356:    */         {
/* 357:645 */           jrDataSource = createReport(provider);
/* 358:    */         }
/* 359:    */         else
/* 360:    */         {
/* 361:648 */           jdbcDataSourceToUse = (DataSource)CollectionUtils.findValueOfType(values, DataSource.class);
/* 362:649 */           if (jdbcDataSourceToUse == null) {
/* 363:650 */             jdbcDataSourceToUse = this.jdbcDataSource;
/* 364:    */           }
/* 365:    */         }
/* 366:    */       }
/* 367:    */     }
/* 368:656 */     if (jdbcDataSourceToUse != null) {
/* 369:657 */       return doFillReport(report, model, jdbcDataSourceToUse);
/* 370:    */     }
/* 371:661 */     if (jrDataSource == null) {
/* 372:662 */       jrDataSource = getReportData(model);
/* 373:    */     }
/* 374:664 */     if (jrDataSource != null)
/* 375:    */     {
/* 376:666 */       if (this.logger.isDebugEnabled()) {
/* 377:667 */         this.logger.debug("Filling report with JRDataSource [" + jrDataSource + "]");
/* 378:    */       }
/* 379:669 */       return JasperFillManager.fillReport(report, model, jrDataSource);
/* 380:    */     }
/* 381:674 */     this.logger.debug("Filling report with plain model");
/* 382:675 */     return JasperFillManager.fillReport(report, model);
/* 383:    */   }
/* 384:    */   
/* 385:    */   private JasperPrint doFillReport(JasperReport report, Map<String, Object> model, DataSource ds)
/* 386:    */     throws Exception
/* 387:    */   {
/* 388:685 */     if (this.logger.isDebugEnabled()) {
/* 389:686 */       this.logger.debug("Filling report using JDBC DataSource [" + ds + "]");
/* 390:    */     }
/* 391:688 */     Connection con = ds.getConnection();
/* 392:    */     try
/* 393:    */     {
/* 394:690 */       return JasperFillManager.fillReport(report, model, con);
/* 395:    */     }
/* 396:    */     finally
/* 397:    */     {
/* 398:    */       try
/* 399:    */       {
/* 400:694 */         con.close();
/* 401:    */       }
/* 402:    */       catch (Throwable ex)
/* 403:    */       {
/* 404:697 */         this.logger.debug("Could not close JDBC Connection", ex);
/* 405:    */       }
/* 406:    */     }
/* 407:    */   }
/* 408:    */   
/* 409:    */   private void populateHeaders(HttpServletResponse response)
/* 410:    */   {
/* 411:708 */     for (Enumeration en = this.headers.propertyNames(); en.hasMoreElements();)
/* 412:    */     {
/* 413:709 */       String key = (String)en.nextElement();
/* 414:710 */       response.addHeader(key, this.headers.getProperty(key));
/* 415:    */     }
/* 416:    */   }
/* 417:    */   
/* 418:    */   protected JasperReport getReport()
/* 419:    */   {
/* 420:725 */     return this.report;
/* 421:    */   }
/* 422:    */   
/* 423:    */   protected JRDataSource getReportData(Map<String, Object> model)
/* 424:    */   {
/* 425:740 */     Object value = CollectionUtils.findValueOfType(model.values(), getReportDataTypes());
/* 426:741 */     return value != null ? convertReportData(value) : null;
/* 427:    */   }
/* 428:    */   
/* 429:    */   protected JRDataSource convertReportData(Object value)
/* 430:    */     throws IllegalArgumentException
/* 431:    */   {
/* 432:764 */     if ((value instanceof JRDataSourceProvider)) {
/* 433:765 */       return createReport((JRDataSourceProvider)value);
/* 434:    */     }
/* 435:768 */     return JasperReportsUtils.convertReportData(value);
/* 436:    */   }
/* 437:    */   
/* 438:    */   protected JRDataSource createReport(JRDataSourceProvider provider)
/* 439:    */   {
/* 440:    */     try
/* 441:    */     {
/* 442:779 */       JasperReport report = getReport();
/* 443:780 */       if (report == null) {
/* 444:781 */         throw new IllegalStateException("No main report defined for JRDataSourceProvider - specify a 'url' on this view or override 'getReport()'");
/* 445:    */       }
/* 446:784 */       return provider.create(report);
/* 447:    */     }
/* 448:    */     catch (JRException ex)
/* 449:    */     {
/* 450:787 */       throw new IllegalArgumentException("Supplied JRDataSourceProvider is invalid", ex);
/* 451:    */     }
/* 452:    */   }
/* 453:    */   
/* 454:    */   protected Class[] getReportDataTypes()
/* 455:    */   {
/* 456:799 */     return new Class[] { Collection.class, [Ljava.lang.Object.class };
/* 457:    */   }
/* 458:    */   
/* 459:    */   protected void postProcessReport(JasperPrint populatedReport, Map<String, Object> model)
/* 460:    */     throws Exception
/* 461:    */   {}
/* 462:    */   
/* 463:    */   protected abstract void renderReport(JasperPrint paramJasperPrint, Map<String, Object> paramMap, HttpServletResponse paramHttpServletResponse)
/* 464:    */     throws Exception;
/* 465:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView
 * JD-Core Version:    0.7.0.1
 */