/*  1:   */ package org.springframework.web.jsf;
/*  2:   */ 
/*  3:   */ import javax.faces.context.FacesContext;
/*  4:   */ import javax.faces.el.EvaluationException;
/*  5:   */ import javax.faces.el.VariableResolver;
/*  6:   */ 
/*  7:   */ public class SpringBeanVariableResolver
/*  8:   */   extends DelegatingVariableResolver
/*  9:   */ {
/* 10:   */   public SpringBeanVariableResolver(VariableResolver originalVariableResolver)
/* 11:   */   {
/* 12:38 */     super(originalVariableResolver);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public Object resolveVariable(FacesContext facesContext, String name)
/* 16:   */     throws EvaluationException
/* 17:   */   {
/* 18:43 */     Object bean = resolveSpringBean(facesContext, name);
/* 19:44 */     if (bean != null) {
/* 20:45 */       return bean;
/* 21:   */     }
/* 22:47 */     Object value = resolveOriginal(facesContext, name);
/* 23:48 */     if (value != null) {
/* 24:49 */       return value;
/* 25:   */     }
/* 26:51 */     return null;
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.jsf.SpringBeanVariableResolver
 * JD-Core Version:    0.7.0.1
 */