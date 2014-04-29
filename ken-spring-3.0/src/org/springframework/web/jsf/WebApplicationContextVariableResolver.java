/*   1:    */ package org.springframework.web.jsf;
/*   2:    */ 
/*   3:    */ import javax.faces.context.FacesContext;
/*   4:    */ import javax.faces.el.EvaluationException;
/*   5:    */ import javax.faces.el.VariableResolver;
/*   6:    */ import org.springframework.util.Assert;
/*   7:    */ import org.springframework.web.context.WebApplicationContext;
/*   8:    */ 
/*   9:    */ public class WebApplicationContextVariableResolver
/*  10:    */   extends VariableResolver
/*  11:    */ {
/*  12:    */   public static final String WEB_APPLICATION_CONTEXT_VARIABLE_NAME = "webApplicationContext";
/*  13:    */   protected final VariableResolver originalVariableResolver;
/*  14:    */   
/*  15:    */   public WebApplicationContextVariableResolver(VariableResolver originalVariableResolver)
/*  16:    */   {
/*  17: 71 */     Assert.notNull(originalVariableResolver, "Original JSF VariableResolver must not be null");
/*  18: 72 */     this.originalVariableResolver = originalVariableResolver;
/*  19:    */   }
/*  20:    */   
/*  21:    */   protected final VariableResolver getOriginalVariableResolver()
/*  22:    */   {
/*  23: 80 */     return this.originalVariableResolver;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public Object resolveVariable(FacesContext context, String name)
/*  27:    */     throws EvaluationException
/*  28:    */   {
/*  29: 92 */     Object value = null;
/*  30: 93 */     if ("webApplicationContext".equals(name)) {
/*  31: 94 */       value = getWebApplicationContext(context);
/*  32:    */     }
/*  33: 96 */     if (value == null) {
/*  34: 97 */       value = getOriginalVariableResolver().resolveVariable(context, name);
/*  35:    */     }
/*  36: 99 */     return value;
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected WebApplicationContext getWebApplicationContext(FacesContext facesContext)
/*  40:    */   {
/*  41:111 */     return FacesContextUtils.getWebApplicationContext(facesContext);
/*  42:    */   }
/*  43:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.jsf.WebApplicationContextVariableResolver
 * JD-Core Version:    0.7.0.1
 */