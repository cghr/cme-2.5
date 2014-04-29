/*   1:    */ package org.springframework.jdbc.support.lob;
/*   2:    */ 
/*   3:    */ import java.io.InputStream;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.io.Reader;
/*   6:    */ import java.io.Writer;
/*   7:    */ import java.lang.reflect.Field;
/*   8:    */ import java.lang.reflect.InvocationTargetException;
/*   9:    */ import java.lang.reflect.Method;
/*  10:    */ import java.sql.Blob;
/*  11:    */ import java.sql.Clob;
/*  12:    */ import java.sql.Connection;
/*  13:    */ import java.sql.PreparedStatement;
/*  14:    */ import java.sql.ResultSet;
/*  15:    */ import java.sql.SQLException;
/*  16:    */ import java.sql.Statement;
/*  17:    */ import java.util.HashMap;
/*  18:    */ import java.util.Iterator;
/*  19:    */ import java.util.LinkedList;
/*  20:    */ import java.util.List;
/*  21:    */ import java.util.Map;
/*  22:    */ import org.apache.commons.logging.Log;
/*  23:    */ import org.apache.commons.logging.LogFactory;
/*  24:    */ import org.springframework.dao.DataAccessResourceFailureException;
/*  25:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*  26:    */ import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
/*  27:    */ import org.springframework.util.FileCopyUtils;
/*  28:    */ 
/*  29:    */ public class OracleLobHandler
/*  30:    */   extends AbstractLobHandler
/*  31:    */ {
/*  32:    */   private static final String BLOB_CLASS_NAME = "oracle.sql.BLOB";
/*  33:    */   private static final String CLOB_CLASS_NAME = "oracle.sql.CLOB";
/*  34:    */   private static final String DURATION_SESSION_FIELD_NAME = "DURATION_SESSION";
/*  35:    */   private static final String MODE_READWRITE_FIELD_NAME = "MODE_READWRITE";
/*  36:    */   private static final String MODE_READONLY_FIELD_NAME = "MODE_READONLY";
/*  37: 92 */   protected final Log logger = LogFactory.getLog(getClass());
/*  38:    */   private NativeJdbcExtractor nativeJdbcExtractor;
/*  39: 96 */   private Boolean cache = Boolean.TRUE;
/*  40: 98 */   private Boolean releaseResourcesAfterRead = Boolean.FALSE;
/*  41:    */   private Class blobClass;
/*  42:    */   private Class clobClass;
/*  43:104 */   private final Map<Class, Integer> durationSessionConstants = new HashMap(2);
/*  44:106 */   private final Map<Class, Integer> modeReadWriteConstants = new HashMap(2);
/*  45:108 */   private final Map<Class, Integer> modeReadOnlyConstants = new HashMap(2);
/*  46:    */   
/*  47:    */   public void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor)
/*  48:    */   {
/*  49:129 */     this.nativeJdbcExtractor = nativeJdbcExtractor;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setCache(boolean cache)
/*  53:    */   {
/*  54:141 */     this.cache = Boolean.valueOf(cache);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setReleaseResourcesAfterRead(boolean releaseResources)
/*  58:    */   {
/*  59:160 */     this.releaseResourcesAfterRead = Boolean.valueOf(releaseResources);
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected synchronized void initOracleDriverClasses(Connection con)
/*  63:    */   {
/*  64:178 */     if (this.blobClass == null) {
/*  65:    */       try
/*  66:    */       {
/*  67:181 */         this.blobClass = con.getClass().getClassLoader().loadClass("oracle.sql.BLOB");
/*  68:182 */         this.durationSessionConstants.put(
/*  69:183 */           this.blobClass, Integer.valueOf(this.blobClass.getField("DURATION_SESSION").getInt(null)));
/*  70:184 */         this.modeReadWriteConstants.put(
/*  71:185 */           this.blobClass, Integer.valueOf(this.blobClass.getField("MODE_READWRITE").getInt(null)));
/*  72:186 */         this.modeReadOnlyConstants.put(
/*  73:187 */           this.blobClass, Integer.valueOf(this.blobClass.getField("MODE_READONLY").getInt(null)));
/*  74:    */         
/*  75:    */ 
/*  76:190 */         this.clobClass = con.getClass().getClassLoader().loadClass("oracle.sql.CLOB");
/*  77:191 */         this.durationSessionConstants.put(
/*  78:192 */           this.clobClass, Integer.valueOf(this.clobClass.getField("DURATION_SESSION").getInt(null)));
/*  79:193 */         this.modeReadWriteConstants.put(
/*  80:194 */           this.clobClass, Integer.valueOf(this.clobClass.getField("MODE_READWRITE").getInt(null)));
/*  81:195 */         this.modeReadOnlyConstants.put(
/*  82:196 */           this.clobClass, Integer.valueOf(this.clobClass.getField("MODE_READONLY").getInt(null)));
/*  83:    */       }
/*  84:    */       catch (Exception ex)
/*  85:    */       {
/*  86:199 */         throw new InvalidDataAccessApiUsageException(
/*  87:200 */           "Couldn't initialize OracleLobHandler because Oracle driver classes are not available. Note that OracleLobHandler requires Oracle JDBC driver 9i or higher!", 
/*  88:201 */           ex);
/*  89:    */       }
/*  90:    */     }
/*  91:    */   }
/*  92:    */   
/*  93:    */   public byte[] getBlobAsBytes(ResultSet rs, int columnIndex)
/*  94:    */     throws SQLException
/*  95:    */   {
/*  96:208 */     this.logger.debug("Returning Oracle BLOB as bytes");
/*  97:209 */     Blob blob = rs.getBlob(columnIndex);
/*  98:210 */     initializeResourcesBeforeRead(rs.getStatement().getConnection(), blob);
/*  99:211 */     byte[] retVal = blob != null ? blob.getBytes(1L, (int)blob.length()) : null;
/* 100:212 */     releaseResourcesAfterRead(rs.getStatement().getConnection(), blob);
/* 101:213 */     return retVal;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public InputStream getBlobAsBinaryStream(ResultSet rs, int columnIndex)
/* 105:    */     throws SQLException
/* 106:    */   {
/* 107:217 */     this.logger.debug("Returning Oracle BLOB as binary stream");
/* 108:218 */     Blob blob = rs.getBlob(columnIndex);
/* 109:219 */     initializeResourcesBeforeRead(rs.getStatement().getConnection(), blob);
/* 110:220 */     InputStream retVal = blob != null ? blob.getBinaryStream() : null;
/* 111:221 */     releaseResourcesAfterRead(rs.getStatement().getConnection(), blob);
/* 112:222 */     return retVal;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public String getClobAsString(ResultSet rs, int columnIndex)
/* 116:    */     throws SQLException
/* 117:    */   {
/* 118:226 */     this.logger.debug("Returning Oracle CLOB as string");
/* 119:227 */     Clob clob = rs.getClob(columnIndex);
/* 120:228 */     initializeResourcesBeforeRead(rs.getStatement().getConnection(), clob);
/* 121:229 */     String retVal = clob != null ? clob.getSubString(1L, (int)clob.length()) : null;
/* 122:230 */     releaseResourcesAfterRead(rs.getStatement().getConnection(), clob);
/* 123:231 */     return retVal;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public InputStream getClobAsAsciiStream(ResultSet rs, int columnIndex)
/* 127:    */     throws SQLException
/* 128:    */   {
/* 129:235 */     this.logger.debug("Returning Oracle CLOB as ASCII stream");
/* 130:236 */     Clob clob = rs.getClob(columnIndex);
/* 131:237 */     initializeResourcesBeforeRead(rs.getStatement().getConnection(), clob);
/* 132:238 */     InputStream retVal = clob != null ? clob.getAsciiStream() : null;
/* 133:239 */     releaseResourcesAfterRead(rs.getStatement().getConnection(), clob);
/* 134:240 */     return retVal;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public Reader getClobAsCharacterStream(ResultSet rs, int columnIndex)
/* 138:    */     throws SQLException
/* 139:    */   {
/* 140:244 */     this.logger.debug("Returning Oracle CLOB as character stream");
/* 141:245 */     Clob clob = rs.getClob(columnIndex);
/* 142:246 */     initializeResourcesBeforeRead(rs.getStatement().getConnection(), clob);
/* 143:247 */     Reader retVal = clob != null ? clob.getCharacterStream() : null;
/* 144:248 */     releaseResourcesAfterRead(rs.getStatement().getConnection(), clob);
/* 145:249 */     return retVal;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public LobCreator getLobCreator()
/* 149:    */   {
/* 150:253 */     return new OracleLobCreator();
/* 151:    */   }
/* 152:    */   
/* 153:    */   protected void initializeResourcesBeforeRead(Connection con, Object lob)
/* 154:    */   {
/* 155:266 */     if (this.releaseResourcesAfterRead.booleanValue())
/* 156:    */     {
/* 157:267 */       initOracleDriverClasses(con);
/* 158:    */       try
/* 159:    */       {
/* 160:272 */         Method isTemporary = lob.getClass().getMethod("isTemporary", new Class[0]);
/* 161:273 */         Boolean temporary = (Boolean)isTemporary.invoke(lob, new Object[0]);
/* 162:274 */         if (!temporary.booleanValue())
/* 163:    */         {
/* 164:278 */           Method open = lob.getClass().getMethod("open", new Class[] { Integer.TYPE });
/* 165:279 */           open.invoke(lob, new Object[] { this.modeReadOnlyConstants.get(lob.getClass()) });
/* 166:    */         }
/* 167:    */       }
/* 168:    */       catch (InvocationTargetException ex)
/* 169:    */       {
/* 170:283 */         this.logger.error("Could not open Oracle LOB", ex.getTargetException());
/* 171:    */       }
/* 172:    */       catch (Exception ex)
/* 173:    */       {
/* 174:286 */         throw new DataAccessResourceFailureException("Could not open Oracle LOB", ex);
/* 175:    */       }
/* 176:    */     }
/* 177:    */   }
/* 178:    */   
/* 179:    */   protected void releaseResourcesAfterRead(Connection con, Object lob)
/* 180:    */   {
/* 181:304 */     if (this.releaseResourcesAfterRead.booleanValue())
/* 182:    */     {
/* 183:305 */       initOracleDriverClasses(con);
/* 184:306 */       Boolean temporary = Boolean.FALSE;
/* 185:    */       try
/* 186:    */       {
/* 187:311 */         Method isTemporary = lob.getClass().getMethod("isTemporary", new Class[0]);
/* 188:312 */         temporary = (Boolean)isTemporary.invoke(lob, new Object[0]);
/* 189:313 */         if (temporary.booleanValue())
/* 190:    */         {
/* 191:317 */           Method freeTemporary = lob.getClass().getMethod("freeTemporary", new Class[0]);
/* 192:318 */           freeTemporary.invoke(lob, new Object[0]);
/* 193:    */         }
/* 194:    */         else
/* 195:    */         {
/* 196:324 */           Method isOpen = lob.getClass().getMethod("isOpen", new Class[0]);
/* 197:325 */           Boolean open = (Boolean)isOpen.invoke(lob, new Object[0]);
/* 198:326 */           if (open.booleanValue())
/* 199:    */           {
/* 200:330 */             Method close = lob.getClass().getMethod("close", new Class[0]);
/* 201:331 */             close.invoke(lob, new Object[0]);
/* 202:    */           }
/* 203:    */         }
/* 204:    */       }
/* 205:    */       catch (InvocationTargetException ex)
/* 206:    */       {
/* 207:336 */         if (temporary.booleanValue()) {
/* 208:337 */           this.logger.error("Could not free Oracle LOB", ex.getTargetException());
/* 209:    */         } else {
/* 210:340 */           this.logger.error("Could not close Oracle LOB", ex.getTargetException());
/* 211:    */         }
/* 212:    */       }
/* 213:    */       catch (Exception ex)
/* 214:    */       {
/* 215:344 */         if (temporary.booleanValue()) {
/* 216:345 */           throw new DataAccessResourceFailureException("Could not free Oracle LOB", ex);
/* 217:    */         }
/* 218:348 */         throw new DataAccessResourceFailureException("Could not close Oracle LOB", ex);
/* 219:    */       }
/* 220:    */     }
/* 221:    */   }
/* 222:    */   
/* 223:    */   protected static abstract interface LobCallback
/* 224:    */   {
/* 225:    */     public abstract void populateLob(Object paramObject)
/* 226:    */       throws Exception;
/* 227:    */   }
/* 228:    */   
/* 229:    */   protected class OracleLobCreator
/* 230:    */     implements LobCreator
/* 231:    */   {
/* 232:362 */     private final List createdLobs = new LinkedList();
/* 233:    */     
/* 234:    */     protected OracleLobCreator() {}
/* 235:    */     
/* 236:    */     public void setBlobAsBytes(PreparedStatement ps, int paramIndex, final byte[] content)
/* 237:    */       throws SQLException
/* 238:    */     {
/* 239:367 */       if (content != null)
/* 240:    */       {
/* 241:368 */         Blob blob = (Blob)createLob(ps, false, new OracleLobHandler.LobCallback()
/* 242:    */         {
/* 243:    */           public void populateLob(Object lob)
/* 244:    */             throws Exception
/* 245:    */           {
/* 246:370 */             Method methodToInvoke = lob.getClass().getMethod("getBinaryOutputStream", new Class[0]);
/* 247:371 */             OutputStream out = (OutputStream)methodToInvoke.invoke(lob, new Object[0]);
/* 248:372 */             FileCopyUtils.copy(content, out);
/* 249:    */           }
/* 250:374 */         });
/* 251:375 */         ps.setBlob(paramIndex, blob);
/* 252:376 */         if (OracleLobHandler.this.logger.isDebugEnabled()) {
/* 253:377 */           OracleLobHandler.this.logger.debug("Set bytes for Oracle BLOB with length " + blob.length());
/* 254:    */         }
/* 255:    */       }
/* 256:    */       else
/* 257:    */       {
/* 258:381 */         ps.setBlob(paramIndex, null);
/* 259:382 */         OracleLobHandler.this.logger.debug("Set Oracle BLOB to null");
/* 260:    */       }
/* 261:    */     }
/* 262:    */     
/* 263:    */     public void setBlobAsBinaryStream(PreparedStatement ps, int paramIndex, final InputStream binaryStream, int contentLength)
/* 264:    */       throws SQLException
/* 265:    */     {
/* 266:390 */       if (binaryStream != null)
/* 267:    */       {
/* 268:391 */         Blob blob = (Blob)createLob(ps, false, new OracleLobHandler.LobCallback()
/* 269:    */         {
/* 270:    */           public void populateLob(Object lob)
/* 271:    */             throws Exception
/* 272:    */           {
/* 273:393 */             Method methodToInvoke = lob.getClass().getMethod("getBinaryOutputStream", null);
/* 274:394 */             OutputStream out = (OutputStream)methodToInvoke.invoke(lob, null);
/* 275:395 */             FileCopyUtils.copy(binaryStream, out);
/* 276:    */           }
/* 277:397 */         });
/* 278:398 */         ps.setBlob(paramIndex, blob);
/* 279:399 */         if (OracleLobHandler.this.logger.isDebugEnabled()) {
/* 280:400 */           OracleLobHandler.this.logger.debug("Set binary stream for Oracle BLOB with length " + blob.length());
/* 281:    */         }
/* 282:    */       }
/* 283:    */       else
/* 284:    */       {
/* 285:404 */         ps.setBlob(paramIndex, null);
/* 286:405 */         OracleLobHandler.this.logger.debug("Set Oracle BLOB to null");
/* 287:    */       }
/* 288:    */     }
/* 289:    */     
/* 290:    */     public void setClobAsString(PreparedStatement ps, int paramIndex, final String content)
/* 291:    */       throws SQLException
/* 292:    */     {
/* 293:412 */       if (content != null)
/* 294:    */       {
/* 295:413 */         Clob clob = (Clob)createLob(ps, true, new OracleLobHandler.LobCallback()
/* 296:    */         {
/* 297:    */           public void populateLob(Object lob)
/* 298:    */             throws Exception
/* 299:    */           {
/* 300:415 */             Method methodToInvoke = lob.getClass().getMethod("getCharacterOutputStream", null);
/* 301:416 */             Writer writer = (Writer)methodToInvoke.invoke(lob, null);
/* 302:417 */             FileCopyUtils.copy(content, writer);
/* 303:    */           }
/* 304:419 */         });
/* 305:420 */         ps.setClob(paramIndex, clob);
/* 306:421 */         if (OracleLobHandler.this.logger.isDebugEnabled()) {
/* 307:422 */           OracleLobHandler.this.logger.debug("Set string for Oracle CLOB with length " + clob.length());
/* 308:    */         }
/* 309:    */       }
/* 310:    */       else
/* 311:    */       {
/* 312:426 */         ps.setClob(paramIndex, null);
/* 313:427 */         OracleLobHandler.this.logger.debug("Set Oracle CLOB to null");
/* 314:    */       }
/* 315:    */     }
/* 316:    */     
/* 317:    */     public void setClobAsAsciiStream(PreparedStatement ps, int paramIndex, final InputStream asciiStream, int contentLength)
/* 318:    */       throws SQLException
/* 319:    */     {
/* 320:435 */       if (asciiStream != null)
/* 321:    */       {
/* 322:436 */         Clob clob = (Clob)createLob(ps, true, new OracleLobHandler.LobCallback()
/* 323:    */         {
/* 324:    */           public void populateLob(Object lob)
/* 325:    */             throws Exception
/* 326:    */           {
/* 327:438 */             Method methodToInvoke = lob.getClass().getMethod("getAsciiOutputStream", null);
/* 328:439 */             OutputStream out = (OutputStream)methodToInvoke.invoke(lob, null);
/* 329:440 */             FileCopyUtils.copy(asciiStream, out);
/* 330:    */           }
/* 331:442 */         });
/* 332:443 */         ps.setClob(paramIndex, clob);
/* 333:444 */         if (OracleLobHandler.this.logger.isDebugEnabled()) {
/* 334:445 */           OracleLobHandler.this.logger.debug("Set ASCII stream for Oracle CLOB with length " + clob.length());
/* 335:    */         }
/* 336:    */       }
/* 337:    */       else
/* 338:    */       {
/* 339:449 */         ps.setClob(paramIndex, null);
/* 340:450 */         OracleLobHandler.this.logger.debug("Set Oracle CLOB to null");
/* 341:    */       }
/* 342:    */     }
/* 343:    */     
/* 344:    */     public void setClobAsCharacterStream(PreparedStatement ps, int paramIndex, final Reader characterStream, int contentLength)
/* 345:    */       throws SQLException
/* 346:    */     {
/* 347:458 */       if (characterStream != null)
/* 348:    */       {
/* 349:459 */         Clob clob = (Clob)createLob(ps, true, new OracleLobHandler.LobCallback()
/* 350:    */         {
/* 351:    */           public void populateLob(Object lob)
/* 352:    */             throws Exception
/* 353:    */           {
/* 354:461 */             Method methodToInvoke = lob.getClass().getMethod("getCharacterOutputStream", null);
/* 355:462 */             Writer writer = (Writer)methodToInvoke.invoke(lob, null);
/* 356:463 */             FileCopyUtils.copy(characterStream, writer);
/* 357:    */           }
/* 358:465 */         });
/* 359:466 */         ps.setClob(paramIndex, clob);
/* 360:467 */         if (OracleLobHandler.this.logger.isDebugEnabled()) {
/* 361:468 */           OracleLobHandler.this.logger.debug("Set character stream for Oracle CLOB with length " + clob.length());
/* 362:    */         }
/* 363:    */       }
/* 364:    */       else
/* 365:    */       {
/* 366:472 */         ps.setClob(paramIndex, null);
/* 367:473 */         OracleLobHandler.this.logger.debug("Set Oracle CLOB to null");
/* 368:    */       }
/* 369:    */     }
/* 370:    */     
/* 371:    */     protected Object createLob(PreparedStatement ps, boolean clob, OracleLobHandler.LobCallback callback)
/* 372:    */       throws SQLException
/* 373:    */     {
/* 374:484 */       Connection con = null;
/* 375:    */       try
/* 376:    */       {
/* 377:486 */         con = getOracleConnection(ps);
/* 378:487 */         OracleLobHandler.this.initOracleDriverClasses(con);
/* 379:488 */         Object lob = prepareLob(con, clob ? OracleLobHandler.this.clobClass : OracleLobHandler.this.blobClass);
/* 380:489 */         callback.populateLob(lob);
/* 381:490 */         lob.getClass().getMethod("close", null).invoke(lob, null);
/* 382:491 */         this.createdLobs.add(lob);
/* 383:492 */         if (OracleLobHandler.this.logger.isDebugEnabled()) {
/* 384:493 */           OracleLobHandler.this.logger.debug("Created new Oracle " + (clob ? "CLOB" : "BLOB"));
/* 385:    */         }
/* 386:495 */         return lob;
/* 387:    */       }
/* 388:    */       catch (SQLException ex)
/* 389:    */       {
/* 390:498 */         throw ex;
/* 391:    */       }
/* 392:    */       catch (InvocationTargetException ex)
/* 393:    */       {
/* 394:501 */         if ((ex.getTargetException() instanceof SQLException)) {
/* 395:502 */           throw ((SQLException)ex.getTargetException());
/* 396:    */         }
/* 397:504 */         if ((con != null) && ((ex.getTargetException() instanceof ClassCastException))) {
/* 398:505 */           throw new InvalidDataAccessApiUsageException(
/* 399:506 */             "OracleLobCreator needs to work on [oracle.jdbc.OracleConnection], not on [" + 
/* 400:507 */             con.getClass().getName() + "]: specify a corresponding NativeJdbcExtractor", 
/* 401:508 */             ex.getTargetException());
/* 402:    */         }
/* 403:511 */         throw new DataAccessResourceFailureException("Could not create Oracle LOB", 
/* 404:512 */           ex.getTargetException());
/* 405:    */       }
/* 406:    */       catch (Exception ex)
/* 407:    */       {
/* 408:516 */         throw new DataAccessResourceFailureException("Could not create Oracle LOB", ex);
/* 409:    */       }
/* 410:    */     }
/* 411:    */     
/* 412:    */     protected Connection getOracleConnection(PreparedStatement ps)
/* 413:    */       throws SQLException, ClassNotFoundException
/* 414:    */     {
/* 415:526 */       return OracleLobHandler.this.nativeJdbcExtractor != null ? 
/* 416:527 */         OracleLobHandler.this.nativeJdbcExtractor.getNativeConnectionFromStatement(ps) : ps.getConnection();
/* 417:    */     }
/* 418:    */     
/* 419:    */     protected Object prepareLob(Connection con, Class lobClass)
/* 420:    */       throws Exception
/* 421:    */     {
/* 422:539 */       Method createTemporary = lobClass.getMethod(
/* 423:540 */         "createTemporary", new Class[] { Connection.class, Boolean.TYPE, Integer.TYPE });
/* 424:541 */       Object lob = createTemporary.invoke(null, new Object[] { con, OracleLobHandler.this.cache, OracleLobHandler.this.durationSessionConstants.get(lobClass) });
/* 425:542 */       Method open = lobClass.getMethod("open", new Class[] { Integer.TYPE });
/* 426:543 */       open.invoke(lob, new Object[] { OracleLobHandler.this.modeReadWriteConstants.get(lobClass) });
/* 427:544 */       return lob;
/* 428:    */     }
/* 429:    */     
/* 430:    */     public void close()
/* 431:    */     {
/* 432:    */       try
/* 433:    */       {
/* 434:552 */         for (Iterator it = this.createdLobs.iterator(); it.hasNext();)
/* 435:    */         {
/* 436:557 */           Object lob = it.next();
/* 437:558 */           Method freeTemporary = lob.getClass().getMethod("freeTemporary", new Class[0]);
/* 438:559 */           freeTemporary.invoke(lob, new Object[0]);
/* 439:560 */           it.remove();
/* 440:    */         }
/* 441:    */       }
/* 442:    */       catch (InvocationTargetException ex)
/* 443:    */       {
/* 444:564 */         OracleLobHandler.this.logger.error("Could not free Oracle LOB", ex.getTargetException());
/* 445:    */       }
/* 446:    */       catch (Exception ex)
/* 447:    */       {
/* 448:567 */         throw new DataAccessResourceFailureException("Could not free Oracle LOB", ex);
/* 449:    */       }
/* 450:    */     }
/* 451:    */   }
/* 452:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.lob.OracleLobHandler
 * JD-Core Version:    0.7.0.1
 */