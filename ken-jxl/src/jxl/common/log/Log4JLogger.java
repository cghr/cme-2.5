/*   1:    */ package jxl.common.log;
/*   2:    */ 
/*   3:    */ public class Log4JLogger
/*   4:    */   extends jxl.common.Logger
/*   5:    */ {
/*   6:    */   private org.apache.log4j.Logger log4jLogger;
/*   7:    */   
/*   8:    */   public Log4JLogger() {}
/*   9:    */   
/*  10:    */   private Log4JLogger(org.apache.log4j.Logger l)
/*  11:    */   {
/*  12: 50 */     this.log4jLogger = l;
/*  13:    */   }
/*  14:    */   
/*  15:    */   public void debug(Object message)
/*  16:    */   {
/*  17: 58 */     this.log4jLogger.debug(message);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public void debug(Object message, Throwable t)
/*  21:    */   {
/*  22: 66 */     this.log4jLogger.debug(message, t);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void error(Object message)
/*  26:    */   {
/*  27: 74 */     this.log4jLogger.error(message);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void error(Object message, Throwable t)
/*  31:    */   {
/*  32: 82 */     this.log4jLogger.error(message, t);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void fatal(Object message)
/*  36:    */   {
/*  37: 90 */     this.log4jLogger.fatal(message);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void fatal(Object message, Throwable t)
/*  41:    */   {
/*  42: 98 */     this.log4jLogger.fatal(message, t);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void info(Object message)
/*  46:    */   {
/*  47:106 */     this.log4jLogger.info(message);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void info(Object message, Throwable t)
/*  51:    */   {
/*  52:115 */     this.log4jLogger.info(message, t);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void warn(Object message)
/*  56:    */   {
/*  57:123 */     this.log4jLogger.warn(message);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void warn(Object message, Throwable t)
/*  61:    */   {
/*  62:131 */     this.log4jLogger.warn(message, t);
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected jxl.common.Logger getLoggerImpl(Class cl)
/*  66:    */   {
/*  67:139 */     org.apache.log4j.Logger l = org.apache.log4j.Logger.getLogger(cl);
/*  68:140 */     return new Log4JLogger(l);
/*  69:    */   }
/*  70:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.common.log.Log4JLogger
 * JD-Core Version:    0.7.0.1
 */