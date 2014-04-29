/*  1:   */ package org.springframework.web.context.support;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletContext;
/*  4:   */ import org.springframework.beans.factory.FactoryBean;
/*  5:   */ import org.springframework.web.context.ServletContextAware;
/*  6:   */ 
/*  7:   */ public class ServletContextAttributeFactoryBean
/*  8:   */   implements FactoryBean<Object>, ServletContextAware
/*  9:   */ {
/* 10:   */   private String attributeName;
/* 11:   */   private Object attribute;
/* 12:   */   
/* 13:   */   public void setAttributeName(String attributeName)
/* 14:   */   {
/* 15:55 */     this.attributeName = attributeName;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void setServletContext(ServletContext servletContext)
/* 19:   */   {
/* 20:59 */     if (this.attributeName == null) {
/* 21:60 */       throw new IllegalArgumentException("Property 'attributeName' is required");
/* 22:   */     }
/* 23:62 */     this.attribute = servletContext.getAttribute(this.attributeName);
/* 24:63 */     if (this.attribute == null) {
/* 25:64 */       throw new IllegalStateException("No ServletContext attribute '" + this.attributeName + "' found");
/* 26:   */     }
/* 27:   */   }
/* 28:   */   
/* 29:   */   public Object getObject()
/* 30:   */     throws Exception
/* 31:   */   {
/* 32:70 */     return this.attribute;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public Class<?> getObjectType()
/* 36:   */   {
/* 37:74 */     return this.attribute != null ? this.attribute.getClass() : null;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public boolean isSingleton()
/* 41:   */   {
/* 42:78 */     return true;
/* 43:   */   }
/* 44:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.ServletContextAttributeFactoryBean
 * JD-Core Version:    0.7.0.1
 */