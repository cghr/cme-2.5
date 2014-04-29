/*  1:   */ package org.springframework.util.xml;
/*  2:   */ 
/*  3:   */ import javax.xml.transform.ErrorListener;
/*  4:   */ import javax.xml.transform.TransformerException;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ 
/*  7:   */ public class SimpleTransformErrorListener
/*  8:   */   implements ErrorListener
/*  9:   */ {
/* 10:   */   private final Log logger;
/* 11:   */   
/* 12:   */   public SimpleTransformErrorListener(Log logger)
/* 13:   */   {
/* 14:42 */     this.logger = logger;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public void warning(TransformerException ex)
/* 18:   */     throws TransformerException
/* 19:   */   {
/* 20:47 */     this.logger.warn("XSLT transformation warning", ex);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void error(TransformerException ex)
/* 24:   */     throws TransformerException
/* 25:   */   {
/* 26:51 */     this.logger.error("XSLT transformation error", ex);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void fatalError(TransformerException ex)
/* 30:   */     throws TransformerException
/* 31:   */   {
/* 32:55 */     throw ex;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.SimpleTransformErrorListener
 * JD-Core Version:    0.7.0.1
 */