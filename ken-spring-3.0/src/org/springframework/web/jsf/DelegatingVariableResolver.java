/*   1:    */ package org.springframework.web.jsf;
/*   2:    */ 
/*   3:    */ import javax.faces.context.FacesContext;
/*   4:    */ import javax.faces.el.EvaluationException;
/*   5:    */ import javax.faces.el.VariableResolver;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.apache.commons.logging.LogFactory;
/*   8:    */ import org.springframework.beans.factory.BeanFactory;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ import org.springframework.web.context.WebApplicationContext;
/*  11:    */ 
/*  12:    */ public class DelegatingVariableResolver
/*  13:    */   extends VariableResolver
/*  14:    */ {
/*  15: 76 */   protected final Log logger = LogFactory.getLog(getClass());
/*  16:    */   protected final VariableResolver originalVariableResolver;
/*  17:    */   
/*  18:    */   public DelegatingVariableResolver(VariableResolver originalVariableResolver)
/*  19:    */   {
/*  20: 89 */     Assert.notNull(originalVariableResolver, "Original JSF VariableResolver must not be null");
/*  21: 90 */     this.originalVariableResolver = originalVariableResolver;
/*  22:    */   }
/*  23:    */   
/*  24:    */   protected final VariableResolver getOriginalVariableResolver()
/*  25:    */   {
/*  26: 98 */     return this.originalVariableResolver;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public Object resolveVariable(FacesContext facesContext, String name)
/*  30:    */     throws EvaluationException
/*  31:    */   {
/*  32:108 */     Object value = resolveOriginal(facesContext, name);
/*  33:109 */     if (value != null) {
/*  34:110 */       return value;
/*  35:    */     }
/*  36:112 */     Object bean = resolveSpringBean(facesContext, name);
/*  37:113 */     if (bean != null) {
/*  38:114 */       return bean;
/*  39:    */     }
/*  40:116 */     return null;
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected Object resolveOriginal(FacesContext facesContext, String name)
/*  44:    */   {
/*  45:123 */     Object value = getOriginalVariableResolver().resolveVariable(facesContext, name);
/*  46:124 */     if ((value != null) && (this.logger.isTraceEnabled())) {
/*  47:125 */       this.logger.trace("Successfully resolved variable '" + name + "' via original VariableResolver");
/*  48:    */     }
/*  49:127 */     return value;
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected Object resolveSpringBean(FacesContext facesContext, String name)
/*  53:    */   {
/*  54:134 */     BeanFactory bf = getBeanFactory(facesContext);
/*  55:135 */     if (bf.containsBean(name))
/*  56:    */     {
/*  57:136 */       if (this.logger.isTraceEnabled()) {
/*  58:137 */         this.logger.trace("Successfully resolved variable '" + name + "' in Spring BeanFactory");
/*  59:    */       }
/*  60:139 */       return bf.getBean(name);
/*  61:    */     }
/*  62:142 */     return null;
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected BeanFactory getBeanFactory(FacesContext facesContext)
/*  66:    */   {
/*  67:156 */     return getWebApplicationContext(facesContext);
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected WebApplicationContext getWebApplicationContext(FacesContext facesContext)
/*  71:    */   {
/*  72:167 */     return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
/*  73:    */   }
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.jsf.DelegatingVariableResolver
 * JD-Core Version:    0.7.0.1
 */