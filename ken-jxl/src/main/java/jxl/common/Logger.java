/*  1:   */ package jxl.common;
/*  2:   */ 
/*  3:   */ import java.security.AccessControlException;
/*  4:   */ import jxl.common.log.LoggerName;
/*  5:   */ import jxl.common.log.SimpleLogger;
/*  6:   */ 
/*  7:   */ public abstract class Logger
/*  8:   */ {
/*  9:33 */   private static Logger logger = null;
/* 10:   */   
/* 11:   */   public static final Logger getLogger(Class cl)
/* 12:   */   {
/* 13:40 */     if (logger == null) {
/* 14:42 */       initializeLogger();
/* 15:   */     }
/* 16:45 */     return logger.getLoggerImpl(cl);
/* 17:   */   }
/* 18:   */   
/* 19:   */   private static synchronized void initializeLogger()
/* 20:   */   {
/* 21:53 */     if (logger != null) {
/* 22:55 */       return;
/* 23:   */     }
/* 24:58 */     String loggerName = LoggerName.NAME;
/* 25:   */     try
/* 26:   */     {
/* 27:63 */       loggerName = System.getProperty("logger");
/* 28:65 */       if (loggerName == null) {
/* 29:68 */         loggerName = LoggerName.NAME;
/* 30:   */       }
/* 31:71 */       logger = (Logger)Class.forName(loggerName).newInstance();
/* 32:   */     }
/* 33:   */     catch (IllegalAccessException e)
/* 34:   */     {
/* 35:75 */       logger = new SimpleLogger();
/* 36:76 */       logger.warn("Could not instantiate logger " + loggerName + 
/* 37:77 */         " using default");
/* 38:   */     }
/* 39:   */     catch (InstantiationException e)
/* 40:   */     {
/* 41:81 */       logger = new SimpleLogger();
/* 42:82 */       logger.warn("Could not instantiate logger " + loggerName + 
/* 43:83 */         " using default");
/* 44:   */     }
/* 45:   */     catch (AccessControlException e)
/* 46:   */     {
/* 47:87 */       logger = new SimpleLogger();
/* 48:88 */       logger.warn("Could not instantiate logger " + loggerName + 
/* 49:89 */         " using default");
/* 50:   */     }
/* 51:   */     catch (ClassNotFoundException e)
/* 52:   */     {
/* 53:93 */       logger = new SimpleLogger();
/* 54:94 */       logger.warn("Could not instantiate logger " + loggerName + 
/* 55:95 */         " using default");
/* 56:   */     }
/* 57:   */   }
/* 58:   */   
/* 59:   */   public abstract void debug(Object paramObject);
/* 60:   */   
/* 61:   */   public abstract void debug(Object paramObject, Throwable paramThrowable);
/* 62:   */   
/* 63:   */   public abstract void error(Object paramObject);
/* 64:   */   
/* 65:   */   public abstract void error(Object paramObject, Throwable paramThrowable);
/* 66:   */   
/* 67:   */   public abstract void fatal(Object paramObject);
/* 68:   */   
/* 69:   */   public abstract void fatal(Object paramObject, Throwable paramThrowable);
/* 70:   */   
/* 71:   */   public abstract void info(Object paramObject);
/* 72:   */   
/* 73:   */   public abstract void info(Object paramObject, Throwable paramThrowable);
/* 74:   */   
/* 75:   */   public abstract void warn(Object paramObject);
/* 76:   */   
/* 77:   */   public abstract void warn(Object paramObject, Throwable paramThrowable);
/* 78:   */   
/* 79:   */   protected abstract Logger getLoggerImpl(Class paramClass);
/* 80:   */   
/* 81:   */   public void setSuppressWarnings(boolean w) {}
/* 82:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.common.Logger
 * JD-Core Version:    0.7.0.1
 */