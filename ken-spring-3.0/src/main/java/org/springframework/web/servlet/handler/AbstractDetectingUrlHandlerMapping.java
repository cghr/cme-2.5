/*  1:   */ package org.springframework.web.servlet.handler;
/*  2:   */ 
/*  3:   */ import org.apache.commons.logging.Log;
/*  4:   */ import org.springframework.beans.BeansException;
/*  5:   */ import org.springframework.beans.factory.BeanFactoryUtils;
/*  6:   */ import org.springframework.context.ApplicationContext;
/*  7:   */ import org.springframework.context.ApplicationContextException;
/*  8:   */ import org.springframework.util.ObjectUtils;
/*  9:   */ 
/* 10:   */ public abstract class AbstractDetectingUrlHandlerMapping
/* 11:   */   extends AbstractUrlHandlerMapping
/* 12:   */ {
/* 13:35 */   private boolean detectHandlersInAncestorContexts = false;
/* 14:   */   
/* 15:   */   public void setDetectHandlersInAncestorContexts(boolean detectHandlersInAncestorContexts)
/* 16:   */   {
/* 17:47 */     this.detectHandlersInAncestorContexts = detectHandlersInAncestorContexts;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void initApplicationContext()
/* 21:   */     throws ApplicationContextException
/* 22:   */   {
/* 23:57 */     super.initApplicationContext();
/* 24:58 */     detectHandlers();
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected void detectHandlers()
/* 28:   */     throws BeansException
/* 29:   */   {
/* 30:70 */     if (this.logger.isDebugEnabled()) {
/* 31:71 */       this.logger.debug("Looking for URL mappings in application context: " + getApplicationContext());
/* 32:   */     }
/* 33:73 */     String[] beanNames = this.detectHandlersInAncestorContexts ? 
/* 34:74 */       BeanFactoryUtils.beanNamesForTypeIncludingAncestors(getApplicationContext(), Object.class) : 
/* 35:75 */       getApplicationContext().getBeanNamesForType(Object.class);
/* 36:78 */     for (String beanName : beanNames)
/* 37:   */     {
/* 38:79 */       String[] urls = determineUrlsForHandler(beanName);
/* 39:80 */       if (!ObjectUtils.isEmpty(urls)) {
/* 40:82 */         registerHandler(urls, beanName);
/* 41:85 */       } else if (this.logger.isDebugEnabled()) {
/* 42:86 */         this.logger.debug("Rejected bean name '" + beanName + "': no URL paths identified");
/* 43:   */       }
/* 44:   */     }
/* 45:   */   }
/* 46:   */   
/* 47:   */   protected abstract String[] determineUrlsForHandler(String paramString);
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping
 * JD-Core Version:    0.7.0.1
 */