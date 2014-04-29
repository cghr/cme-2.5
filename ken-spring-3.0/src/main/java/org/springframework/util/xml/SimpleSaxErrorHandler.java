/*  1:   */ package org.springframework.util.xml;
/*  2:   */ 
/*  3:   */ import org.apache.commons.logging.Log;
/*  4:   */ import org.xml.sax.ErrorHandler;
/*  5:   */ import org.xml.sax.SAXException;
/*  6:   */ import org.xml.sax.SAXParseException;
/*  7:   */ 
/*  8:   */ public class SimpleSaxErrorHandler
/*  9:   */   implements ErrorHandler
/* 10:   */ {
/* 11:   */   private final Log logger;
/* 12:   */   
/* 13:   */   public SimpleSaxErrorHandler(Log logger)
/* 14:   */   {
/* 15:42 */     this.logger = logger;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void warning(SAXParseException ex)
/* 19:   */     throws SAXException
/* 20:   */   {
/* 21:47 */     this.logger.warn("Ignored XML validation warning", ex);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void error(SAXParseException ex)
/* 25:   */     throws SAXException
/* 26:   */   {
/* 27:51 */     throw ex;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void fatalError(SAXParseException ex)
/* 31:   */     throws SAXException
/* 32:   */   {
/* 33:55 */     throw ex;
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.SimpleSaxErrorHandler
 * JD-Core Version:    0.7.0.1
 */