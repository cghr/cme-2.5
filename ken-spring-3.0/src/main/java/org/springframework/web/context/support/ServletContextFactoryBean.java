/*  1:   */ package org.springframework.web.context.support;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletContext;
/*  4:   */ import org.springframework.beans.factory.FactoryBean;
/*  5:   */ import org.springframework.web.context.ServletContextAware;
/*  6:   */ 
/*  7:   */ @Deprecated
/*  8:   */ public class ServletContextFactoryBean
/*  9:   */   implements FactoryBean<ServletContext>, ServletContextAware
/* 10:   */ {
/* 11:   */   private ServletContext servletContext;
/* 12:   */   
/* 13:   */   public void setServletContext(ServletContext servletContext)
/* 14:   */   {
/* 15:53 */     this.servletContext = servletContext;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public ServletContext getObject()
/* 19:   */   {
/* 20:58 */     return this.servletContext;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public Class<? extends ServletContext> getObjectType()
/* 24:   */   {
/* 25:62 */     return this.servletContext != null ? this.servletContext.getClass() : ServletContext.class;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public boolean isSingleton()
/* 29:   */   {
/* 30:66 */     return true;
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.ServletContextFactoryBean
 * JD-Core Version:    0.7.0.1
 */