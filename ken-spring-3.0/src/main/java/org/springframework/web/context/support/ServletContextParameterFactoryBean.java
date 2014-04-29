/*  1:   */ package org.springframework.web.context.support;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletContext;
/*  4:   */ import org.springframework.beans.factory.FactoryBean;
/*  5:   */ import org.springframework.web.context.ServletContextAware;
/*  6:   */ 
/*  7:   */ public class ServletContextParameterFactoryBean
/*  8:   */   implements FactoryBean<String>, ServletContextAware
/*  9:   */ {
/* 10:   */   private String initParamName;
/* 11:   */   private String paramValue;
/* 12:   */   
/* 13:   */   public void setInitParamName(String initParamName)
/* 14:   */   {
/* 15:50 */     this.initParamName = initParamName;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void setServletContext(ServletContext servletContext)
/* 19:   */   {
/* 20:54 */     if (this.initParamName == null) {
/* 21:55 */       throw new IllegalArgumentException("initParamName is required");
/* 22:   */     }
/* 23:57 */     this.paramValue = servletContext.getInitParameter(this.initParamName);
/* 24:58 */     if (this.paramValue == null) {
/* 25:59 */       throw new IllegalStateException("No ServletContext init parameter '" + this.initParamName + "' found");
/* 26:   */     }
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String getObject()
/* 30:   */   {
/* 31:65 */     return this.paramValue;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public Class<String> getObjectType()
/* 35:   */   {
/* 36:69 */     return String.class;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public boolean isSingleton()
/* 40:   */   {
/* 41:73 */     return true;
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.ServletContextParameterFactoryBean
 * JD-Core Version:    0.7.0.1
 */