/*  1:   */ package org.springframework.web.context.support;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import java.util.Map.Entry;
/*  5:   */ import javax.servlet.ServletContext;
/*  6:   */ import org.apache.commons.logging.Log;
/*  7:   */ import org.apache.commons.logging.LogFactory;
/*  8:   */ import org.springframework.web.context.ServletContextAware;
/*  9:   */ 
/* 10:   */ public class ServletContextAttributeExporter
/* 11:   */   implements ServletContextAware
/* 12:   */ {
/* 13:49 */   protected final Log logger = LogFactory.getLog(getClass());
/* 14:   */   private Map<String, Object> attributes;
/* 15:   */   
/* 16:   */   public void setAttributes(Map<String, Object> attributes)
/* 17:   */   {
/* 18:63 */     this.attributes = attributes;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setServletContext(ServletContext servletContext)
/* 22:   */   {
/* 23:67 */     for (Map.Entry<String, Object> entry : this.attributes.entrySet())
/* 24:   */     {
/* 25:68 */       String attributeName = (String)entry.getKey();
/* 26:69 */       if ((this.logger.isWarnEnabled()) && 
/* 27:70 */         (servletContext.getAttribute(attributeName) != null)) {
/* 28:71 */         this.logger.warn("Replacing existing ServletContext attribute with name '" + attributeName + "'");
/* 29:   */       }
/* 30:74 */       servletContext.setAttribute(attributeName, entry.getValue());
/* 31:75 */       if (this.logger.isInfoEnabled()) {
/* 32:76 */         this.logger.info("Exported ServletContext attribute with name '" + attributeName + "'");
/* 33:   */       }
/* 34:   */     }
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.ServletContextAttributeExporter
 * JD-Core Version:    0.7.0.1
 */