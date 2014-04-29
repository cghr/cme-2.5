/*  1:   */ package org.springframework.web.jsf.el;
/*  2:   */ 
/*  3:   */ import javax.el.ELContext;
/*  4:   */ import javax.faces.context.FacesContext;
/*  5:   */ import org.springframework.beans.factory.BeanFactory;
/*  6:   */ import org.springframework.beans.factory.access.el.SpringBeanELResolver;
/*  7:   */ import org.springframework.web.context.WebApplicationContext;
/*  8:   */ import org.springframework.web.jsf.FacesContextUtils;
/*  9:   */ 
/* 10:   */ public class SpringBeanFacesELResolver
/* 11:   */   extends SpringBeanELResolver
/* 12:   */ {
/* 13:   */   protected BeanFactory getBeanFactory(ELContext elContext)
/* 14:   */   {
/* 15:79 */     return getWebApplicationContext(elContext);
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected WebApplicationContext getWebApplicationContext(ELContext elContext)
/* 19:   */   {
/* 20:90 */     FacesContext facesContext = FacesContext.getCurrentInstance();
/* 21:91 */     return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.jsf.el.SpringBeanFacesELResolver
 * JD-Core Version:    0.7.0.1
 */